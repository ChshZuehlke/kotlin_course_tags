package ch.zuehlke.sbb.reddit.data.source.remote.model.posts

import com.google.gson.annotations.Expose

/**
 * Created by chsc on 13.11.17.
 */


abstract class RedditPostElement private constructor() {


    class DataRedditPostElement : RedditPostElement() {
        @Expose
        val kind: String? = null
        @Expose
        val data: RedditPost? = null
    }

    // This is a special wrapper because we cannot make java.util.String to be a subclass of the RedditPostElement class
    // Additionally, you can add more methods if necessary
    class ReferenceRedditPostElement constructor(@Expose val reference: String) : RedditPostElement()

    companion object {
        fun reference(reference: String): RedditPostElement {
            return ReferenceRedditPostElement(reference)
        }
    }

}