package ch.zuehlke.sbb.reddit.data.source

import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData

/**
 * Created by chsc on 08.11.17.
 */

interface RedditDataSource {

    interface LoadNewsCallback {

        fun onNewsLoaded(data: List<RedditNewsData>)

        fun onDataNotAvailable()
    }

    interface LoadPostsCallback {

        fun onPostsLoaded(posts: List<RedditPostsData>)

        fun onDataNotAvailable()
    }


    fun getMoreNews(callback: LoadNewsCallback)

    fun getNews(callback: LoadNewsCallback)

    fun getPosts(callback: LoadPostsCallback, title: String)

    fun savePosts(data: List<RedditPostsData>)

    fun deletePostsWithPermaLink(permaLink: String)

    fun refreshNews()

    fun deleteAllNews()

    fun saveRedditNews(data: List<RedditNewsData>)
}
