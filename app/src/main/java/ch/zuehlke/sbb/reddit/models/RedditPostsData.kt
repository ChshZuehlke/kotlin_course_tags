package ch.zuehlke.sbb.reddit.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by celineheldner on 13.11.17.
 */
@Entity(tableName = "redditPosts")
data class RedditPostsData(@PrimaryKey var id: String = "",
                           var parentId: String? = null,
                           var author: String? = null,
                           var body: String? = null,
                           var createdUtc: Long = 0,
                           var depth: Int = 0,
                           var body_html: String? = null,
                           var parentPermaLink: String? = null,
                           var ordering: Long = 0)

