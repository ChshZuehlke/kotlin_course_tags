package ch.zuehlke.sbb.reddit.data.source

import android.content.Context
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import io.reactivex.Flowable
import io.reactivex.Observable

/**
 * Created by chsc on 08.11.17.
 */

class RedditRepository (val newsRemoteDataSource: RedditDataSource, val newsLocalDataSource: RedditDataSource, private val context: Context) : RedditDataSource {

    val sources = listOf(newsLocalDataSource, newsRemoteDataSource)



    override val news: Flowable<List<RedditNewsData>>
        get() = Flowable.merge(sources.map{it.news}, 1, 1)

    private fun convertURLToRemote(url: String): String {
        val parsedUrl = url.substring(url.indexOf(COMMENT_SECION) + COMMENT_SECION.length)
        return parsedUrl.substring(0, parsedUrl.length - 1)
    }

    override fun posts(permalink: String): Observable<List<RedditPostsData>> {
        val convertedPermalink = convertURLToRemote(permalink)
        return Observable.merge(sources.map{it.posts(convertedPermalink)}, 1, 1)
    }

    override fun savePosts(data: RedditPostsData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePostsWithPermaLink(permaLink: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refreshNews() {
        mCacheIsDirty = true
        mRedditNewsRemoteDataSource.refreshNews()
    }

    override fun deleteAllNews() {
        mRedditNewsRemoteDataSource.deleteAllNews() // Although we call deleteAllNews() on the remote datasource, it is not implemented.
        mRedditNewsLocalDataSource.deleteAllNews()
    }

    override fun saveRedditNews(data: List<RedditNewsData>) {
        checkNotNull(data)

        mRedditNewsLocalDataSource.saveRedditNews(data)
        mRedditNewsRemoteDataSource.saveRedditNews(data) // Although we call saveRedditNews() on the remote datasource, it is not implemented.
        // Do in memory cache update to keep the app UI up to date
        for (elem in data){
            if (mCacheNews == null) {
                mCacheNews = LinkedHashMap<String, RedditNewsData>()
            }
            mCacheNews!!.put(elem.id!!, elem)
        }

    }

    private fun getNewsFromRemoteDataSource(callback: RedditDataSource.LoadNewsCallback) {
        mRedditNewsRemoteDataSource.getNews(object : RedditDataSource.LoadNewsCallback {
            override fun onNewsLoaded(news: List<RedditNewsData>) {
                refreshCache(news)
                refreshLocalDataSource(news)
                callback.onNewsLoaded(ArrayList(mCacheNews!!.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun refreshCache(news: List<RedditNewsData>) {
        if (mCacheNews == null) {
            mCacheNews = LinkedHashMap<String, RedditNewsData>()
        }
        mCacheNews!!.clear()
        for (data in news) {
            mCacheNews!!.put(data.id!!, data)
        }
        mCacheIsDirty = false
    }

    private fun updateLocalDataSource(news: List<RedditNewsData>) {
        mRedditNewsLocalDataSource.saveRedditNews(news)

    }

    private fun refreshLocalDataSource(news: List<RedditNewsData>) {
        mRedditNewsLocalDataSource.deleteAllNews()

        mRedditNewsLocalDataSource.saveRedditNews(news)

    }

    companion object {

        private val TAG = "RemoteDataSource"

        private val COMMENT_SECION = "comments/"
    }
}
