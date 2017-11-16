package ch.zuehlke.sbb.reddit.data.source.local

import android.content.ContentValues
import android.content.Context

import java.util.ArrayList

import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData

import com.google.common.base.Preconditions.checkNotNull
import de.dabotz.shoppinglist.database.AppDatabase

/**
 * Created by chsc on 08.11.17.
 */

class RedditNewsLocalDataSource constructor(context: Context, db: AppDatabase) : RedditDataSource {

    private val mDbHelper: RedditNewsDataHelper
    private val mDb: AppDatabase

    init {
        checkNotNull(context)
        mDbHelper = RedditNewsDataHelper(context)
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
        val redditNews = ArrayList<RedditPostsData>()
        val db = mDbHelper.readableDatabase

        val projection = arrayOf(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_PARENT_ID, RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_ID, RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_DEPTH, RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_CREATED, RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_BODY_HTML, RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_BODY, RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_PARENTPERMALINK, RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_ORDERING, RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_AUTHOR)

        val selection = RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_PARENTPERMALINK + " like '" + permalink + "'"

        val c = db.query(
                RedditNewsPersistenceContract.RedditPostEntry.TABLE_NAME, projection, selection, null, null, null, RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_ORDERING)

        if (c != null && c.count > 0) {
            while (c.moveToNext()) {
                val parentId = c.getString(c.getColumnIndexOrThrow(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_PARENT_ID))
                val postId = c.getString(c.getColumnIndexOrThrow(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_ID))
                val depth = c.getInt(c.getColumnIndexOrThrow(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_DEPTH))
                val created = c.getLong(c.getColumnIndexOrThrow(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_CREATED))
                val bodyHtml = c.getString(c.getColumnIndexOrThrow(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_BODY_HTML))
                val body = c.getString(c.getColumnIndexOrThrow(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_BODY))
                val permaLink = c.getString(c.getColumnIndexOrThrow(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_PARENTPERMALINK))
                val ordering = c.getLong(c.getColumnIndexOrThrow(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_ORDERING))
                val author = c.getString(c.getColumnIndexOrThrow(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_AUTHOR))

                val data = RedditPostsData(postId, parentId, author, body, created, depth, bodyHtml, permaLink, ordering)
                redditNews.add(data)
            }
        }
        c?.close()

        db.close()

        if (redditNews.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable()
        } else {
            callback.onPostsLoaded(redditNews)
        }
    }

    override fun savePosts(data: RedditPostsData) {

        checkNotNull(data)
        val db = mDbHelper.writableDatabase

        val values = ContentValues()
        values.put(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_AUTHOR, data.author)
        values.put(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_BODY, data.body)
        values.put(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_CREATED, data.createdUtc)
        values.put(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_ID, data.id)
        values.put(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_PARENTPERMALINK, data.parentPermaLink)
        values.put(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_DEPTH, data.depth)
        values.put(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_PARENT_ID, data.parentId)
        values.put(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_BODY_HTML, data.body_html)
        values.put(RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_ORDERING, data.ordering)


        db.insert(RedditNewsPersistenceContract.RedditPostEntry.TABLE_NAME, null, values)

        db.close()

    }

    override fun deletePostsWithPermaLink(permaLink: String) {

        val where = RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_PARENTPERMALINK + " like '" + permaLink + "'"

        val db = mDbHelper.writableDatabase
        db.delete(RedditNewsPersistenceContract.RedditPostEntry.TABLE_NAME, where, null)
        db.close()
    }

    override fun refreshNews() {
        // Not required because the {@link RedditRepository} handles the logic of refreshing the
        // news from all the available redditPost sources.
    }

    override fun deleteAllNews() {
        val db = mDbHelper.writableDatabase
        db.delete(RedditNewsPersistenceContract.RedditNewsEntry.TABLE_NAME, null, null)
        db.close()
    }

    override fun saveRedditNews(data: List<RedditNewsData>) {
        checkNotNull(data)
        mDb.redditNewsDataDao().addNewsItem(data)
    }

}
