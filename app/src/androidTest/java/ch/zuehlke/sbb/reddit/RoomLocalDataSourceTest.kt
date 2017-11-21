package ch.zuehlke.sbb.reddit

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import de.dabotz.shoppinglist.database.AppDatabase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by celineheldner on 21.11.17.
 */
@RunWith(AndroidJUnit4::class)
open class RoomLocalDataSourceTest{

    private lateinit var redditDatabase: AppDatabase

    @Before
    fun initDb(){
        redditDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                AppDatabase::class.java
        ).build()
    }

    @Test
    fun retrieveRedditNewsData(){
        //when
        redditDatabase.redditNewsDataDao().addNewsItem(TestData.initialNews)
        val retrievedNews = redditDatabase.redditNewsDataDao().getNews()

        //then
        assert(retrievedNews.equals(TestData.initialNews.sortedWith(compareBy({it.id},{it.id}))))
    }
}