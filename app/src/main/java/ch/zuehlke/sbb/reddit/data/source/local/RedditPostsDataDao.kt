package ch.zuehlke.sbb.reddit.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import ch.zuehlke.sbb.reddit.models.RedditPostsData

/**
 * Created by celineheldner on 16.11.17.
 */
@Dao
interface RedditPostsDataDao{

    @Query("select * from redditPosts where parentPermaLink = :arg0 order by ordering")
    fun getPosts(permalink: String?): List<RedditPostsData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPostItems(item: List<RedditPostsData>)

    @Query("delete from redditPosts where parentPermaLink = :arg0")
    fun deletePosts(permalink: String?)

}