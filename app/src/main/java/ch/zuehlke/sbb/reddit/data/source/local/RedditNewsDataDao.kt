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


    //TODO: room_exercise1: Füge die nötige Room Annotation hinzu
    fun getNews(): List<RedditNewsData>

    //TODO: room_exercise1: Füge die nötige Room Annotation hinzu
    fun addNewsItem(item: List<RedditNewsData>)

    //TODO: room_exercise1: Füge die nötige Room Annotation hinzu
    fun deleteNews()


}