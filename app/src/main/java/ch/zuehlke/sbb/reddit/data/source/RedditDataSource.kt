package ch.zuehlke.sbb.reddit.data.source

import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import io.reactivex.Flowable

/**
 * Created by chsc on 08.11.17.
 */

interface RedditDataSource {

    val news: Flowable<List<RedditNewsData>>

    interface LoadPostsCallback {

        fun onPostsLoaded(posts: List<RedditPostsData>)

        fun onDataNotAvailable()
    }

    fun getPosts(callback: LoadPostsCallback, title: String)

    fun savePosts(data: List<RedditPostsData>)

    fun deletePostsWithPermaLink(permaLink: String)

    fun deleteAllNews()

    fun saveRedditNews(data: List<RedditNewsData>)
}
