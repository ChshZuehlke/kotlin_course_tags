package ch.zuehlke.sbb.reddit.data.source

import android.content.Context
import android.net.ConnectivityManager

import java.util.ArrayList
import java.util.LinkedHashMap

import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData

import com.google.common.base.Preconditions.checkNotNull

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


    override fun getMoreNews(callback: RedditDataSource.LoadNewsCallback) {
        checkNotNull(callback)
        addNewsFromRemoteDataSource(callback)
    }

    private fun addNewsFromRemoteDataSource(callback: RedditDataSource.LoadNewsCallback) {
        mRedditNewsRemoteDataSource.getMoreNews(object : RedditDataSource.LoadNewsCallback {
            override fun onNewsLoaded(news: List<RedditNewsData>) {
                refreshCache(news)
                updateLocalDataSource(news)
                callback.onNewsLoaded(ArrayList(mCacheNews!!.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    override fun getNews(callback: RedditDataSource.LoadNewsCallback) {
        checkNotNull(callback)

        mRedditNewsRemoteDataSource.refreshNews()
        // Respond immediately with cache if available and not dirty
        if (mCacheNews != null && !mCacheIsDirty) {
            callback.onNewsLoaded(ArrayList(mCacheNews!!.values))
            return
        }

        if (!isNetworkAvailable) {
            // Query the local storage if available. If not, query the network.
            mRedditNewsLocalDataSource.getNews(object : RedditDataSource.LoadNewsCallback {
                override fun onNewsLoaded(tasks: List<RedditNewsData>) {
                    refreshCache(tasks)
                    callback.onNewsLoaded(ArrayList(mCacheNews!!.values))
                }

                override fun onDataNotAvailable() {
                    callback.onDataNotAvailable()
                }
            })

        } else {
            if (mCacheIsDirty) {
                // If the cache is dirty we need to fetch new data from the network. The Cache is only dirty, when a refreshNews is going on
                getNewsFromRemoteDataSource(object : RedditDataSource.LoadNewsCallback {
                    override fun onNewsLoaded(data: List<RedditNewsData>) {
                        saveRedditNews(data)
                        refreshCache(data)
                        callback.onNewsLoaded(ArrayList(mCacheNews!!.values))
                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
            } else {
                // Query the local storage if available. If not, query the network.
                mRedditNewsLocalDataSource.getNews(object : RedditDataSource.LoadNewsCallback {
                    override fun onNewsLoaded(tasks: List<RedditNewsData>) {
                        refreshCache(tasks)
                        callback.onNewsLoaded(ArrayList(mCacheNews!!.values))
                    }

                    override fun onDataNotAvailable() {
                        getNewsFromRemoteDataSource(callback)
                    }
                })
            }
        }


    }

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
                for (data in posts) {
                    mRedditNewsLocalDataSource.savePosts(data)
                }
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

    override fun savePosts(data: RedditPostsData) {

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

}
