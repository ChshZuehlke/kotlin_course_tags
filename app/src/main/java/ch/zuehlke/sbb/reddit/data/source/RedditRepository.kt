package ch.zuehlke.sbb.reddit.data.source

import android.content.Context
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import io.reactivex.Flowable
import io.reactivex.Observable

/**
 * Created by chsc on 08.11.17.
 */

class RedditRepository (val newsRemoteDataSource: RedditDataSource, val newsLocalDataSource: RedditDataSource, context: Context) : RedditDataSource {

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
        checkNotNull(data)

        newsLocalDataSource.saveRedditNews(data)
        newsRemoteDataSource.saveRedditNews(data) // Although we call saveRedditNews() on the remote datasource, it is not implemented.
    }

    private fun updateLocalDataSource(news: List<RedditNewsData>) {
        for (data in news) {
            newsLocalDataSource.saveRedditNews(data)
        }
    }

    private fun refreshLocalDataSource(news: List<RedditNewsData>) {
        newsLocalDataSource.deleteAllNews()
        for (data in news) {
            newsLocalDataSource.saveRedditNews(data)
        }
    }

    companion object {

        private val TAG = "RemoteDataSource"

        private val COMMENT_SECION = "comments/"
    }
}
