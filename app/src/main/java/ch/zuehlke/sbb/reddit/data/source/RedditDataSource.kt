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

    fun posts(permalink: String): Observable<List<RedditPostsData>>

    fun savePosts(data: List<RedditPostsData>)

    fun deletePostsWithPermaLink(permaLink: String)

    fun refreshNews()

    fun deleteAllNews()

    fun saveRedditNews(data: List<RedditNewsData>)
}
