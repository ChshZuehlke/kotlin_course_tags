package ch.zuehlke.sbb.reddit.data.source.remote

import ch.zuehlke.sbb.reddit.data.source.remote.model.news.RedditNewsAPIResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by chsc on 12.11.17.
 */

interface RedditAPI {

    @GET("{sortOrder}.json")
    fun getSortedNews(@Path("sortOrder") sortOrder: String, @Query("after") after: String,
                      @Query("limit") limit: String): Call<RedditNewsAPIResponse>


    @GET("/comments/{title}.json")
    fun getRedditPosts(@Path("title") title: String, @Query("sort") sortOrder: String): Call<ResponseBody>

}
