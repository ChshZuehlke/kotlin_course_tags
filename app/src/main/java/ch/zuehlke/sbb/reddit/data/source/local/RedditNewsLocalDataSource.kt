package ch.zuehlke.sbb.reddit.data.source.local

import android.content.Context
import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import com.google.common.base.Preconditions.checkNotNull
import de.dabotz.shoppinglist.database.AppDatabase

/**
 * Created by chsc on 08.11.17.
 */

class RedditNewsLocalDataSource constructor(context: Context, db: AppDatabase) : RedditDataSource {

    private val mDb: AppDatabase

    init {
        checkNotNull(context)
        mDb = db
    }

    override fun getMoreNews(callback: RedditDataSource.LoadNewsCallback) {
        throw UnsupportedOperationException("Not supported by local datasource")
    }

    override fun getNews(callback: RedditDataSource.LoadNewsCallback) {
        val redditNews = mDb.redditNewsDataDao().getNews();


        if (redditNews.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable()
        } else {
            callback.onNewsLoaded(redditNews)
        }
    }

    override fun getPosts(callback: RedditDataSource.LoadPostsCallback, permalink: String) {

        val redditNews = mDb.reditPostsDataDao().getPosts(permalink)
        if (redditNews.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable()
        } else {
            callback.onPostsLoaded(redditNews)
        }
    }

    override fun savePosts(data: List<RedditPostsData>) {

        checkNotNull(data)

        mDb.reditPostsDataDao().addPostItems(data)

    }

    override fun deletePostsWithPermaLink(permaLink: String) {
        mDb.reditPostsDataDao().deletePosts(permaLink)
    }

    override fun refreshNews() {
        // Not required because the {@link RedditRepository} handles the logic of refreshing the
        // news from all the available redditPost sources.
    }

    override fun deleteAllNews() {
        mDb.redditNewsDataDao().deleteNews()
    }

    override fun saveRedditNews(data: List<RedditNewsData>) {
        checkNotNull(data)
        mDb.redditNewsDataDao().addNewsItem(data)
    }

}
