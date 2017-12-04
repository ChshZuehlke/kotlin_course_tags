package ch.zuehlke.sbb.reddit.data.source.remote

import android.content.Context
import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.data.source.remote.model.posts.RedditPostElement
import ch.zuehlke.sbb.reddit.extensions.logD
import ch.zuehlke.sbb.reddit.extensions.logE
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import com.google.common.base.Preconditions.checkNotNull
import com.google.common.base.Strings
import com.google.gson.Gson
import io.reactivex.Emitter
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type

class RedditNewsDataRemoteDataSource(context: Context, redditAPI: RedditAPI, gson: Gson, type: Type, val ioScheduler: Scheduler = Schedulers.io()) : RedditDataSource {
    private var order = -1
    private val mRedditAPI: RedditAPI
    private val mGson: Gson
    private val mType: Type

    init {
        checkNotNull(context)
        mRedditAPI = checkNotNull(redditAPI, "The reddit api cannot be null")
        mGson = gson
        mType = type

    }

    override val news: Flowable<List<RedditNewsData>> = Flowable.generate(
            fun(): String {
                logD("New subscription, starting with tag \"\"")
                return ""
            },
            fun(currentTag: String, emitter: Emitter<List<RedditNewsData>>): String {
                try {
                    val response = redditAPI.getSortedNews("hot", currentTag, "10").execute().body()
                    val newsList = response?.data?.children?.map { child ->
                        child.data?.let { data ->
                            RedditNewsData(data.author, data.title, data.num_comments, data.created, data.thumbnail, data.url, data.id!!, data.permalink!!)
                        }
                    } ?: emptyList()
                    val nextTag = response?.data?.after ?: ""
                    logD("Received page $currentTag, next is $nextTag")
                    emitter.onNext(newsList.mapNotNull { it })
                    return nextTag
                } catch (error: Exception) {
                    logE("Problem when receiving the currentTag pages tag: $error")
                    emitter.onError(error);
                    return ""
                }
            }
    )

    override fun getPosts(callback: RedditDataSource.LoadPostsCallback, title: String) {
        val call = mRedditAPI.getRedditPosts(title, "new")
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var redditPosts: List<RedditPostsData> = ArrayList()
                val elements = parseResponseToPostElements(response.body()!!)
                order = 0
                redditPosts = flattenRetrofitResponse(elements, title)
                callback.onPostsLoaded(redditPosts)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onDataNotAvailable()
            }
        })

    }

    override fun savePosts(data: List<RedditPostsData>) {
        //Remotely its not used
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
            redditPostElements = mGson.fromJson<List<RedditPostElement>>(response.string(), mType)
        } catch (e: IOException) {
            logE("Error while parsing respone $e")
        }

        return redditPostElements!!

    }

    override fun deleteAllNews() {
        // Not supported by Reddit :)
    }

    override fun saveRedditNews(data: List<RedditNewsData>) {
        // In this demo app we do not support posting of news, therefore not implemented.
    }
}
