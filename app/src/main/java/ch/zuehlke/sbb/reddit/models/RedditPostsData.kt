package ch.zuehlke.sbb.reddit.models

/**
 * Created by celineheldner on 13.11.17.
 */

data class RedditPostsData(var id: String,
                           var parentId: String?,
                           var author: String?,
                           var body: String?,
                           var createdUtc: Long,
                           var depth: Int,
                           var body_html: String?,
                           var parentPermaLink: String?,
                           var ordering: Long)

