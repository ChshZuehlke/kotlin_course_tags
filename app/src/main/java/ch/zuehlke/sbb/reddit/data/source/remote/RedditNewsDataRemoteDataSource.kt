package ch.zuehlke.sbb.reddit.data.source.remote

import android.content.Context
import android.util.Log
import ch.zuehlke.sbb.reddit.Injection
import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.data.source.remote.model.posts.RedditPostElement
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import com.google.common.base.Strings
import io.reactivex.Emitter
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.IOException
import java.util.*

class RedditNewsDataRemoteDataSource// Prevent direct instantiation.
private constructor(val context: Context, val redditAPI: RedditAPI) : RedditDataSource {
    private var order = -1

    init {
        checkNotNull(context)
        checkNotNull(redditAPI, { "The reddit API cannot be null" })
    }

    override val news: Flowable<List<RedditNewsData>>
        get() {
            Log.d(TAG, "Creating a reddit news data flowable")
            return Flowable.generate(
                    {
                        Log.d(TAG, "Subscription started")
                        ""
                    },
                    fun(next: String, emitter: Emitter<List<RedditNewsData>>): String {
                        Log.d(TAG, "Next page id available: $next")
                        val requestNews = requestNews(next)

                        Log.d(TAG, "Waiting for the response")
                        val (news, nextTag) = requestNews.blockingGet()
                        emitter.onNext(news.mapNotNull { it })
                        return nextTag?:""
                    })
        }

    private fun requestNews(after: String): Single<Pair<List<RedditNewsData?>, String?>> {
        val newsSource = redditAPI.getSortedNews("hot", after, "10")
        val subscribeOnIo = newsSource.subscribeOn(Schedulers.io())
        return subscribeOnIo.map { response ->
            val news = response.data?.children?.map { child ->
                child.data?.let { d ->
                    d.author?.let { author ->
                        d.title?.let { title ->
                            d.thumbnail?.let { thumbnail ->
                                d.url?.let { url ->
                                    d.id?.let { id ->
                                        d.permalink?.let { permalink ->
                                            RedditNewsData(author, title, d.num_comments, d.created, thumbnail, url, id, permalink)
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            } ?: emptyList()
            Pair(news, response.data?.after)
        }
    }

    override fun posts(title: String): Observable<List<RedditPostsData>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPosts(callback: RedditDataSource.LoadPostsCallback, title: String) {
        /*val call = mRedditAPI.getRedditPosts(title, "new")
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var redditPosts: List<RedditPostsData> = ArrayList()
                val parentId: String? = null
                val elements = parseResponseToPostElements(response.body())
                order = 0
                redditPosts = flattenRetrofitResponse(elements, title)
                callback.onPostsLoaded(redditPosts)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onDataNotAvailable()
            }
        })
*/
    }

    override fun savePosts(data: RedditPostsData) {
        //Remotly its not used
    }

    override fun deletePostsWithPermaLink(permaLink: String) {

    }

    private fun flattenRetrofitResponse(response: List<RedditPostElement>, parentPermaLink: String): List<RedditPostsData> {
        val flatten = ArrayList<RedditPostsData>()
        for (redditPostElement in response) {
            if (redditPostElement is RedditPostElement.DataRedditPostElement) {
                val dataElement = redditPostElement
                val data = dataElement.data
                if (dataElement.data != null) {
                    if (!Strings.isNullOrEmpty(dataElement.data.body_html)) {
                        data?.let {
                            val postData = RedditPostsData(data.id!!, null, data.author!!, data.body!!, data.created_utc, data.depth, data.body_html!!, data.permalink!!, order++.toLong())
                            flatten.add(postData)
                        }

                    } else {
                        data?.let {
                            flatten.addAll(recursivlyParseResponse(dataElement, data!!.id, parentPermaLink))
                        }

                    }
                }
            }
        }
        return flatten
    }

    private fun recursivlyParseResponse(dataRedditPostElement: RedditPostElement.DataRedditPostElement, parentId: String?, parentPermaLink: String): List<RedditPostsData> {
        val posts = ArrayList<RedditPostsData>()
        for (child in dataRedditPostElement.data!!.children!!) {
            if (child is RedditPostElement.DataRedditPostElement) {
                val data = child.data
                if (data != null) {
                    data?.let {
                        val postData = RedditPostsData(data.id!!, parentId, data.author, data.body, data.created_utc, data.depth, data.body_html, parentPermaLink, order++.toLong())
                        posts.add(postData)
                        if (data.replies != null && data.replies is RedditPostElement.DataRedditPostElement) {
                            posts.addAll(recursivlyParseResponse(data.replies as RedditPostElement.DataRedditPostElement, data.id!!, parentPermaLink))
                        }
                    }

                }
            }

        }
        return posts
    }


    private fun parseResponseToPostElements(response: ResponseBody): List<RedditPostElement> {
        var redditPostElements: List<RedditPostElement>? = null
        try {
            redditPostElements = Injection.gson.fromJson<List<RedditPostElement>>(response.string(), Injection.type)
        } catch (e: IOException) {
            Log.e(TAG, "Error while parsing respone $e")
        }

        return redditPostElements!!

    }

    override fun refreshNews() {
        //after = ""
    }

    override fun deleteAllNews() {
        // Not supported by Reddit :)
    }

    override fun saveRedditNews(data: RedditNewsData) {
        // In this demo app we do not support posting of news, therefore not implemented.
    }

    companion object {

        private val TAG = "RemoteDataSource"

        private var INSTANCE: RedditNewsDataRemoteDataSource? = null

        fun getInstance(context: Context, redditAPI: RedditAPI): RedditNewsDataRemoteDataSource {
            if (INSTANCE == null) {
                INSTANCE = RedditNewsDataRemoteDataSource(context, redditAPI)
            }
            return INSTANCE!!
        }
    }
}
