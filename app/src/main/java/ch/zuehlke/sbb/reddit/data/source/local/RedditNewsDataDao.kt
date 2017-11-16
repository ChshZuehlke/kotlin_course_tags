package ch.zuehlke.sbb.reddit.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.models.RedditNewsData

/**
 * Created by celineheldner on 16.11.17.
 */
@Dao
interface RedditNewsDataDao{


    @Query("select * from RedditNewsData ")
    fun getNews(): List<RedditNewsData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewsItem(item: List<RedditNewsData>)

    /*fun getPosts(callback: RedditDataSource.LoadPostsCallback, title: String)

    fun savePosts(data: RedditPostsData)

    fun deletePostsWithPermaLink(permaLink: String)

    fun refreshNews()

    fun deleteAllNews()

    fun saveRedditNews(data: RedditNewsData)*/

}