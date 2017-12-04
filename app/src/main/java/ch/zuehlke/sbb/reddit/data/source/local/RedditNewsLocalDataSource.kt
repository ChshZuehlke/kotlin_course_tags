package ch.zuehlke.sbb.reddit.data.source.local

import android.content.Context
import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import com.google.common.base.Preconditions.checkNotNull
import de.dabotz.shoppinglist.database.AppDatabase
import io.reactivex.Flowable

/**
 * Created by chsc on 08.11.17.
 */

class RedditNewsLocalDataSource constructor(context: Context, db: AppDatabase) : RedditDataSource {

    private val mDb: AppDatabase

    init {
        checkNotNull(context)
        mDb = db
    }

    override val news: Flowable<List<RedditNewsData>> = mDb.redditNewsDataDao().getNews().toFlowable()

    override fun getPosts(callback: RedditDataSource.LoadPostsCallback, permalink: String) {
        val redditPostsAccessor = AsyncDBAccessor<List<RedditPostsData>>(
                {mDb.reditPostsDataDao().getPosts(permalink)},
                {result ->
                    if (result.isEmpty()) {
                        // This will be called if the table is new or just empty.
                        callback.onDataNotAvailable()
                    } else {
                        callback.onPostsLoaded(result)
                    }
                }
        )
        redditPostsAccessor.execute()
    }

    override fun savePosts(data: List<RedditPostsData>) {
        checkNotNull(data)
        val asyncPostSaver = AsyncDBAccessor(
                {mDb.reditPostsDataDao().addPostItems(data)},
                {})
        asyncPostSaver.go()
    }

    override fun deletePostsWithPermaLink(permaLink: String) {
        AsyncDBAccessor({
        mDb.reditPostsDataDao().deletePosts(permaLink)},{}).execute()

    }

    override fun deleteAllNews() {
        AsyncDBAccessor({
        mDb.redditNewsDataDao().deleteNews()},{}).execute()
    }

    override fun saveRedditNews(data: List<RedditNewsData>) {
        checkNotNull(data)
        AsyncDBAccessor({
        mDb.redditNewsDataDao().addNewsItem(data)},{}).execute()
    }

}
