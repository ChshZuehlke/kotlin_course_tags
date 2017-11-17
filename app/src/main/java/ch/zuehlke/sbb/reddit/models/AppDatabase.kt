package de.dabotz.shoppinglist.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ch.zuehlke.sbb.reddit.data.source.local.RedditNewsDataDao
import ch.zuehlke.sbb.reddit.data.source.local.RedditPostsDataDao
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData

/**
 * Created by Botz on 05.07.17.
 */
@Database(entities = arrayOf(RedditNewsData::class, RedditPostsData::class), version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun redditNewsDataDao() : RedditNewsDataDao
    abstract fun reditPostsDataDao() : RedditPostsDataDao
}
