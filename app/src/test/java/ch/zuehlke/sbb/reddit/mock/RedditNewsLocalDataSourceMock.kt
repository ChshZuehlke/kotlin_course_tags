package ch.zuehlke.sbb.reddit.mock

import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData

/**
 * Created by celineheldner on 20.11.17.
 */
class RedditNewsLocalDataSourceMock: RedditDataSourceMock(), RedditDataSource{

    init {
        posts.clear()
        news.clear()
    }

    override fun getNews(callback: RedditDataSource.LoadNewsCallback) {
        callback.onNewsLoaded(news)
    }

    override fun getPosts(callback: RedditDataSource.LoadPostsCallback, title: String) {
        callback.onPostsLoaded(posts)
    }

    override fun savePosts(data: List<RedditPostsData>) {
        posts.addAll(data)
    }

    override fun deletePostsWithPermaLink(permaLink: String) {
        posts.filter { it.parentPermaLink.equals(permaLink) }
    }

    override fun deleteAllNews() {
        news.clear()
    }

    override fun saveRedditNews(data: List<RedditNewsData>) {
        news.addAll(data)
    }

    override fun refreshNews() {
        TODO("not implemented in local") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMoreNews(callback: RedditDataSource.LoadNewsCallback) {
        TODO("not implemented in local") //To change body of created functions use File | Settings | File Templates.
    }


}