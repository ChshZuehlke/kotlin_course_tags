package ch.zuehlke.sbb.reddit.data.source.remote

import android.content.Context
import android.util.Log
import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.data.source.remote.model.posts.RedditPost
import ch.zuehlke.sbb.reddit.data.source.remote.model.posts.RedditPostElement
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import com.google.common.base.Strings
import com.google.gson.Gson
import io.reactivex.Emitter
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.exceptions.Exceptions
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by chsc on 08.11.17.
 */

class RedditNewsDataRemoteDataSource constructor(val context: Context, val redditAPI: RedditAPI, gson: Gson, type: Type) : RedditDataSource {
    private var order = -1
    private val mGson: Gson
    private val mType: Type

    init {
        checkNotNull(context)
        checkNotNull(redditAPI, { "The reddit API cannot be null" })
        mGson = gson
        mType = type
    }

    override val news: Flowable<List<RedditNewsData>>
        get() {
            Log.d(TAG, "Creating a reddit news data flowable")
            return Flowable.generate(
                    {
                        Log.d(TAG, "Subscription started on thread ${Thread.currentThread().name}")
                        ""
                    },
                    fun(next: String, emitter: Emitter<List<RedditNewsData>>): String {
                        Log.d(TAG, "Next page id available: '$next'")

                        val requestNews = requestNews(next).subscribeOn(Schedulers.io())

                        Log.d(TAG, "Waiting for the response on thread ${Thread.currentThread().name}")
                        requestNews.subscribe { (news, _) ->
                            Log.d(TAG, "Received page")
                            emitter.onNext(news.mapNotNull { it })
                        }
                        return requestNews.blockingGet().second ?: ""
                    })
        }

    private fun requestNews(after: String): Single<Pair<List<RedditNewsData?>, String?>> {
        val newsSource = redditAPI.getSortedNews("hot", after, "10")
        return newsSource.map { response ->
            val news = response.data?.children?.map { child ->
                child.data?.let { d ->
                    RedditNewsData(d.author, d.title, d.num_comments, d.created, d.thumbnail, d.url, d.id, d.permalink)
                }
            } ?: emptyList()
            Pair(news, response.data?.after)
        }
    }

    override fun posts(title: String): Observable<List<RedditPostsData>> {
        return redditAPI.getRedditPosts(title, "new")
                .map { response ->
                    Log.d(TAG, "Loading posts on ${Thread.currentThread().name}")
                    try {
                        val redditPostElements: List<RedditPostElement> = mGson.fromJson<List<RedditPostElement>>(response.string(), mType)
                        flattenRetrofitResponse(redditPostElements, title)
                    } catch (e: IOException) {
                        throw Exceptions.propagate(e)
                    }
                }.toObservable()
    }

    override fun savePosts(data: RedditPostsData) {
        //Remotly its not used
    }

    override fun deletePostsWithPermaLink(permaLink: String) {

    }

    private fun flattenRetrofitResponse(response: List<RedditPostElement>, parentPermaLink: String): List<RedditPostsData> {
        return response
                .filter { it is RedditPostElement.DataRedditPostElement }
                .map { it as RedditPostElement.DataRedditPostElement }
                .flatMap { redditPostElement ->
                    redditPostElement.data?.let { data ->
                        if (!Strings.isNullOrEmpty(redditPostElement.data.body_html))
                            listOf(RedditPostsData(data.id!!, null, data.author, data.body, data.created_utc, data.depth, data.body_html, data.permalink, order++.toLong()))
                        else
                            recursivlyParseResponse(data.id, parentPermaLink, data)
                    } ?: emptyList()
                }
    }

    private fun recursivlyParseResponse(parentId: String?, parentPermaLink: String, redditPost: RedditPost): List<RedditPostsData> {
        val children = redditPost.children ?: emptyList()
        return children
                .filter { it is RedditPostElement.DataRedditPostElement }
                .map { it as RedditPostElement.DataRedditPostElement }
                .flatMap { redditPostElement ->
                    redditPostElement.data?.let { data ->
                        val post = RedditPostsData(data.id!!, parentId, data.author, data.body, data.created_utc, data.depth, data.body_html, data.permalink, order++.toLong())
                        val subPosts = data.replies?.let { replies ->
                            if (replies is RedditPostElement.DataRedditPostElement && replies.data != null)
                                recursivlyParseResponse(data.id!!, parentPermaLink, replies.data)
                            else
                                emptyList()
                        } ?: emptyList()
                        listOf(post) + subPosts
                    } ?: emptyList()
                }
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

    }
}
