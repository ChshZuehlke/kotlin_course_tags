package ch.zuehlke.sbb.reddit.models

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration

/**
 * Created by celineheldner on 16.11.17.
 */

private val TEXT_TYPE = " TEXT "

private val LONG_TYPE = " INTEGER "

private val COMMA_SEP = ","
private val NOT_NULL = " NOT NULL "

val MIGRATION_1_2 = object: Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        val SQL_CREATE_POST_ENTRIES = "CREATE TABLE redditPosts (" +
                "id" + TEXT_TYPE + " PRIMARY KEY"+ NOT_NULL  + COMMA_SEP  +
                "parentId" + TEXT_TYPE + COMMA_SEP +
                "author" + TEXT_TYPE + COMMA_SEP +
                "createdUtc" + LONG_TYPE  + NOT_NULL+ COMMA_SEP +
                "body" + TEXT_TYPE + COMMA_SEP +
                "depth" + LONG_TYPE +  NOT_NULL +COMMA_SEP +
                "body_html" + TEXT_TYPE + COMMA_SEP +
                "parentPermaLink" + TEXT_TYPE + COMMA_SEP +
                "ordering" + LONG_TYPE + NOT_NULL +
                " )"
        database.execSQL(SQL_CREATE_POST_ENTRIES)
    }
}