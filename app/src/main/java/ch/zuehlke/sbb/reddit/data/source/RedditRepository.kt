package ch.zuehlke.sbb.reddit.data.source

import android.content.Context
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import com.google.common.base.Preconditions.checkNotNull
import io.reactivex.Flowable
import java.util.*

/**
 * Created by chsc on 08.11.17.
 */

class RedditRepository constructor(newsRemoteDataSource: RedditDataSource,
                                   newsLocalDataSource: RedditDataSource, private val mContext: Context) : RedditDataSource {

    private val mRedditNewsRemoteDataSource: RedditDataSource

    private val mRedditNewsLocalDataSource: RedditDataSource

    private val COMMENT_SECION = "comments/"

    /**
     * This variable has package local visibility so it can be accessed from tests.H
     */
    internal var mCacheNews: MutableMap<String, RedditNewsData>? = null

    /**
     * Marks the cache as invalid, to force an update the next time redditPost is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    internal var mCacheIsDirty = false


    init {
        mRedditNewsRemoteDataSource = checkNotNull(newsRemoteDataSource)
        mRedditNewsLocalDataSource = checkNotNull(newsLocalDataSource)
    }

    override val news: Flowable<List<RedditNewsData>> = Flowable.concat(
            mRedditNewsLocalDataSource.news, mRedditNewsRemoteDataSource.news
    ).cache()

    override fun getPosts(callback: RedditDataSource.LoadPostsCallback, permalink: String) {
        val convertedPermaLink = convertURLToRemote(permalink)
        mRedditNewsLocalDataSource.getPosts(object : RedditDataSource.LoadPostsCallback {
            override fun onPostsLoaded(posts: List<RedditPostsData>) {
                callback.onPostsLoaded(posts)
            }

            override fun onDataNotAvailable() {

            }
        }, convertedPermaLink)

        mRedditNewsRemoteDataSource.getPosts(object : RedditDataSource.LoadPostsCallback {
            override fun onPostsLoaded(posts: List<RedditPostsData>) {
                mRedditNewsLocalDataSource.deletePostsWithPermaLink(convertedPermaLink)
                mRedditNewsLocalDataSource.savePosts(posts)

                callback.onPostsLoaded(posts)
            }

            override fun onDataNotAvailable() {

            }
        }, convertedPermaLink)

    }

    private fun convertURLToRemote(url: String): String {
        val parsedUrl = url.substring(url.indexOf(COMMENT_SECION) + COMMENT_SECION.length)
        return parsedUrl.substring(0, parsedUrl.length - 1)
    }

    override fun savePosts(data: List<RedditPostsData>) {

    }

    override fun deletePostsWithPermaLink(permaLink: String) {

    }

    override fun refreshNews() {
        mCacheIsDirty = true
        mRedditNewsRemoteDataSource.refreshNews()
    }

    override fun deleteAllNews() {
        mRedditNewsRemoteDataSource.deleteAllNews() // Although we call deleteAllNews() on the remote datasource, it is not implemented.
        mRedditNewsLocalDataSource.deleteAllNews()

        if (mCacheNews == null) {
            mCacheNews = LinkedHashMap<String, RedditNewsData>()
        }
        mCacheNews!!.clear()
    }

    override fun saveRedditNews(data: List<RedditNewsData>) {
        checkNotNull(data)

        mRedditNewsLocalDataSource.saveRedditNews(data)
        mRedditNewsRemoteDataSource.saveRedditNews(data) // Although we call saveRedditNews() on the remote datasource, it is not implemented.
    }

}
