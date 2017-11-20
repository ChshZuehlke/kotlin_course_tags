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
        TODO("not implemented for remote") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePostsWithPermaLink(permaLink: String) {
        TODO("not implemented for remote") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllNews() {
        TODO("not implemented for remote") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveRedditNews(data: List<RedditNewsData>) {
    }
}