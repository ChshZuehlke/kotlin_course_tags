package ch.zuehlke.sbb.reddit.data.source.local

import android.content.Context
import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import com.google.common.base.Preconditions.checkNotNull
import de.dabotz.shoppinglist.database.AppDatabase
import io.reactivex.Observable

/**
 * Created by chsc on 08.11.17.
 */

class RedditNewsLocalDataSource constructor(context: Context, db: AppDatabase) : RedditDataSource {

    private val mDb: AppDatabase

    init {
        checkNotNull(context)
        checkNotNull(db)
        mDb = db
    }

    override val news = mDb.redditNewsDataDao().getNewsSingle().toFlowable()

    override fun posts(permalink: String): Observable<List<RedditPostsData>> = mDb.reditPostsDataDao().getPostsFlowable(permalink).toObservable()

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
