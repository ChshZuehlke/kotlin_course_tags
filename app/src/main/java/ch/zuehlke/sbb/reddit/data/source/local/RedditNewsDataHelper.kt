package ch.zuehlke.sbb.reddit.data.source.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by chsc on 08.11.17.
 */

class RedditNewsDataHelper(context: Context) : SQLiteOpenHelper(context, RedditNewsDataHelper.DATABASE_NAME, null, RedditNewsDataHelper.DATABASE_VERSION) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_POST_ENTRIES)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        // Not needed for version 1
    }

    companion object {

        val DATABASE_VERSION = 1

        val DATABASE_NAME = "RedditNews.db"

        private val TEXT_TYPE = " TEXT"

        private val LONG_TYPE = " INTEGER "

        private val COMMA_SEP = ","



        private val SQL_CREATE_POST_ENTRIES = "CREATE TABLE " + RedditNewsPersistenceContract.RedditPostEntry.TABLE_NAME + " (" +
                RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_ID + TEXT_TYPE + " PRIMARY_KEY" + COMMA_SEP +
                RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_AUTHOR + TEXT_TYPE + COMMA_SEP +
                RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_PARENT_ID + TEXT_TYPE + COMMA_SEP +
                RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_CREATED + LONG_TYPE + COMMA_SEP +
                RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_BODY + TEXT_TYPE + COMMA_SEP +
                RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_DEPTH + LONG_TYPE + COMMA_SEP +
                RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_BODY_HTML + TEXT_TYPE + COMMA_SEP +
                RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_PARENTPERMALINK + TEXT_TYPE + COMMA_SEP +
                RedditNewsPersistenceContract.RedditPostEntry.COLUMN_NAME_ORDERING + LONG_TYPE +
                " )"
    }
}
