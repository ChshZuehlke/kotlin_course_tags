package ch.zuehlke.sbb.reddit.data.source.local

import android.content.Context
import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import com.google.common.base.Preconditions.checkNotNull
import de.dabotz.shoppinglist.database.AppDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Created by chsc on 08.11.17.
 */

class RedditNewsLocalDataSource constructor(context: Context, db: AppDatabase, private val ioScheduler:Scheduler = Schedulers.io()) : RedditDataSource {

    private val mDb: AppDatabase

    init {
        checkNotNull(context)
        checkNotNull(db)
        mDb = db
    }

    override val news = mDb.redditNewsDataDao().getNewsSingle().toFlowable()

    override fun posts(permalink: String): Observable<List<RedditPostsData>> =
            mDb.reditPostsDataDao().getPostsSingle(permalink).toObservable().subscribeOn(ioScheduler)

    override fun savePosts(data: List<RedditPostsData>) {
        checkNotNull(data)
        Completable.create {
            mDb.reditPostsDataDao().addPostItems(data)
        }.subscribeOn(ioScheduler).subscribe()
    }

    override fun deletePostsWithPermaLink(permaLink: String) {
        Completable.create {
            mDb.reditPostsDataDao().deletePosts(permaLink)
        }.subscribeOn(ioScheduler).subscribe()
    }

    override fun refreshNews() {
        // Not required because the {@link RedditRepository} handles the logic of refreshing the
        // news from all the available redditPost sources.
    }

    override fun deleteAllNews() {
        Completable.create {
            mDb.redditNewsDataDao().deleteNews()
        }.subscribeOn(ioScheduler).subscribe()
    }

    override fun saveRedditNews(data: List<RedditNewsData>) {
        checkNotNull(data)
        Completable.create {
            mDb.redditNewsDataDao().addNewsItem(data)
        }.subscribeOn(ioScheduler).subscribe()
    }
}
