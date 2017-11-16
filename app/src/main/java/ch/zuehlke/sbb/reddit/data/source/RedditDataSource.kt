package ch.zuehlke.sbb.reddit.data.source

import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import io.reactivex.Flowable
import io.reactivex.Observable

/**
 * Created by chsc on 08.11.17.
 */

interface RedditDataSource {

    val news: Flowable<List<RedditNewsData>>

    fun posts(title: String): Observable<List<RedditPostsData>>

    interface LoadPostsCallback {

        fun onPostsLoaded(posts: List<RedditPostsData>)

        fun onDataNotAvailable()
    }


    fun getPosts(callback: LoadPostsCallback, title: String)

    fun savePosts(data: RedditPostsData)

    fun deletePostsWithPermaLink(permaLink: String)

    fun refreshNews()

    fun deleteAllNews()

    fun saveRedditNews(data: RedditNewsData)
}
