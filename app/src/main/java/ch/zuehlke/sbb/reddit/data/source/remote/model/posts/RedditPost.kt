package ch.zuehlke.sbb.reddit.data.source.remote.model.posts

import com.google.gson.annotations.Expose

/**
 * Created by chsc on 13.11.17.
 */

class RedditPost {

    @Expose
    var id: String? = null
    @Expose
    var children: List<RedditPostElement>? = null
    @Expose
    var replies: RedditPostElement? = null
    @Expose
    var author: String? = null
    @Expose
    var body: String? = null
    @Expose
    var created_utc: Long = 0
    @Expose
    var depth = 0
    @Expose
    var body_html: String? = null
    @Expose
    var permalink: String? = null

}