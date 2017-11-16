package ch.zuehlke.sbb.reddit.data.source

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import ch.zuehlke.sbb.reddit.data.source.remote.RedditAPI
import ch.zuehlke.sbb.reddit.data.source.remote.RedditNewsDataRemoteDataSource

import java.util.ArrayList
import java.util.LinkedHashMap

import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData

import com.google.common.base.Preconditions.checkNotNull
import io.reactivex.Flowable
import io.reactivex.Observable

/**
 * Created by chsc on 08.11.17.
 */

class RedditRepository// Prevent direct instantiation.
private constructor(val newsRemoteDataSource: RedditDataSource, val newsLocalDataSource: RedditDataSource) : RedditDataSource {

    override val news: Flowable<List<RedditNewsData>>
        get() = Flowable.merge(listOf(newsLocalDataSource.news, newsRemoteDataSource.news), 1, 1)

    override fun posts(title: String): Observable<List<RedditPostsData>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPosts(callback: RedditDataSource.LoadPostsCallback, title: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun savePosts(data: RedditPostsData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePostsWithPermaLink(permaLink: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refreshNews() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllNews() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveRedditNews(data: RedditNewsData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    companion object {

        private val TAG = "RemoteDataSource"

        private var INSTANCE: RedditRepository? = null

        fun getInstance(newsRemoteDataSource: RedditDataSource, newsLocalDataSource: RedditDataSource): RedditRepository {
            if (INSTANCE == null) {
                INSTANCE = RedditRepository(newsRemoteDataSource, newsLocalDataSource)
            }
            return INSTANCE!!
        }
    }
}
