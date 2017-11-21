package ch.zuehlke.sbb.reddit.mock

import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData

/**
 * Created by celineheldner on 20.11.17.
 */
class RedditNewsDataRemoteDataSourceMock: RedditDataSourceMock(), RedditDataSource {


    override fun getNews(callback: RedditDataSource.LoadNewsCallback) {
        Thread.sleep(500L)
        callback.onNewsLoaded(news)
    }

    override fun getPosts(callback: RedditDataSource.LoadPostsCallback, title: String) {
        Thread.sleep(500L)
        callback.onPostsLoaded(posts)
    }

    override fun getMoreNews(callback: RedditDataSource.LoadNewsCallback) {
        Thread.sleep(500L)
        callback.onNewsLoaded(moreNews)
    }

    override fun refreshNews() {

    }

    override fun savePosts(data: List<RedditPostsData>) {
        // not implemented for remote
    }

    override fun deletePostsWithPermaLink(permaLink: String) {
        // not implemented for remote
    }

    override fun deleteAllNews() {
        // not implemented for remote
    }

    override fun saveRedditNews(data: List<RedditNewsData>) {
        // not implemented for remote
    }
}