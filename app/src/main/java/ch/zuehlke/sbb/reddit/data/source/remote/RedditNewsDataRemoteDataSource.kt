package ch.zuehlke.sbb.reddit.data.source.remote

import android.content.Context
import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.data.source.remote.model.news.RedditNewsAPIResponse
import ch.zuehlke.sbb.reddit.data.source.remote.model.posts.RedditPostElement
import ch.zuehlke.sbb.reddit.extensions.logE
import ch.zuehlke.sbb.reddit.extensions.logI
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import com.google.common.base.Preconditions.checkNotNull
import com.google.common.base.Strings
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type
import java.util.*

/**
 * Created by chsc on 08.11.17.
 */

class RedditNewsDataRemoteDataSource constructor(context: Context, redditAPI: RedditAPI, gson: Gson, type: Type) : RedditDataSource {
    private var after = ""
    private var order = -1
    private val mRedditAPI: RedditAPI
    private val mGson: Gson
    private val mType: Type
    private val TAG = "RemoteDataSource"

    init {
        checkNotNull(context)
        mRedditAPI = checkNotNull(redditAPI, "The reddit api cannot be null")
        mGson = gson
        mType = type

    }


    override fun getMoreNews(callback: RedditDataSource.LoadNewsCallback) {
        val call = mRedditAPI.getSortedNews("hot", after, "10")
        call.enqueue(object : Callback<RedditNewsAPIResponse> {
            override fun onResponse(call: Call<RedditNewsAPIResponse>, response: Response<RedditNewsAPIResponse>) {
                after = response.body().data!!.after!!
                logI("Recieved reddit response: " + response.body())
                val redditNewsDataList = ArrayList<RedditNewsData>()
                for (child in response.body().data!!.children!!) {
                    val data = child.data
                    logI("child date: " + Date(data!!.created))
                    data?.let {
                        redditNewsDataList.add(RedditNewsData(data.author!!, data.title!!, data.num_comments, data.created, data.thumbnail!!, data.url!!, data.id!!, data.permalink!!))

                    }
                     }
                callback.onNewsLoaded(redditNewsDataList)
            }

            override fun onFailure(call: Call<RedditNewsAPIResponse>, t: Throwable) {
                logE("Error while requesting reddit news: $t")
                callback.onDataNotAvailable()
            }
        })
    }

    override fun getNews(callback: RedditDataSource.LoadNewsCallback) {


        val call = mRedditAPI.getSortedNews("hot", "", "10")
        call.enqueue(object : Callback<RedditNewsAPIResponse> {
            override fun onResponse(call: Call<RedditNewsAPIResponse>, response: Response<RedditNewsAPIResponse>) {
                after = response.body().data!!.after!!
                logI("Recieved reddit response: ${response.body()}")
                val redditNewsDataList = ArrayList<RedditNewsData>()
                for (child in response.body().data!!.children!!) {
                    val data = child.data
                    data?.let {
                        redditNewsDataList.add(RedditNewsData(data.author!!, data.title!!, data.num_comments, data.created, data.thumbnail!!, data.url!!, data.id!!, data.permalink!!))
                    }

                }
                callback.onNewsLoaded(redditNewsDataList)
            }

            override fun onFailure(call: Call<RedditNewsAPIResponse>, t: Throwable) {
                logE("Error while requesting reddit news: $t"  )
                callback.onDataNotAvailable()
            }
        })
    }

    override fun getPosts(callback: RedditDataSource.LoadPostsCallback, title: String) {
        val call = mRedditAPI.getRedditPosts(title, "new")
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var redditPosts: List<RedditPostsData> = ArrayList()
                val elements = parseResponseToPostElements(response.body())
                order = 0
                redditPosts = flattenRetrofitResponse(elements, title)
                callback.onPostsLoaded(redditPosts)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onDataNotAvailable()
            }
        })

    }

    override fun savePosts(data: List<RedditPostsData>) {
        //Remotly its not used
    }

    override fun deletePostsWithPermaLink(permaLink: String) {

    }

    private fun flattenRetrofitResponse(response: List<RedditPostElement>, parentPermaLink: String): List<RedditPostsData> {
        val flatten = ArrayList<RedditPostsData>()
        for (redditPostElement in response) { // Use map for this loop
            if (redditPostElement is RedditPostElement.DataRedditPostElement) { // Use filter instead of this if
                val dataElement = redditPostElement
                val data = dataElement.data
                if (dataElement.data != null) {
                    if (!Strings.isNullOrEmpty(dataElement.data.body_html)) {
                        data?.let {
                            val postData = RedditPostsData(data.id!!, null, data.author!!, data.body!!, data.created_utc, data.depth, data.body_html!!, data.permalink!!, order++.toLong())
                            flatten.add(postData) // Use flatMap instead off
                        }

                    } else {
                        data?.let {
                            flatten.addAll(recursivlyParseResponse(dataElement, data!!.id, parentPermaLink))
                        }

                    }
                }
            }
        }
        return flatten
    }

    private fun recursivlyParseResponse(dataRedditPostElement: RedditPostElement.DataRedditPostElement, parentId: String?, parentPermaLink: String): List<RedditPostsData> {
        val posts = ArrayList<RedditPostsData>()
        for (child in dataRedditPostElement.data!!.children!!) { // Use map for this
            if (child is RedditPostElement.DataRedditPostElement) { // Use filter for this
                val data = child.data
                if (data != null) {
                    data?.let {
                        val postData = RedditPostsData(data.id!!, parentId, data.author, data.body, data.created_utc, data.depth, data.body_html, parentPermaLink, order++.toLong())
                        posts.add(postData)
                        if (data.replies != null && data.replies is RedditPostElement.DataRedditPostElement) {
                            posts.addAll(recursivlyParseResponse(data.replies as RedditPostElement.DataRedditPostElement, data.id!!, parentPermaLink)) // Use flatMap for this
                        }
                    }

                }
            }

        }
        return posts
    }


    private fun parseResponseToPostElements(response: ResponseBody): List<RedditPostElement> {
        var redditPostElements: List<RedditPostElement>? = null
        try {
            redditPostElements = mGson.fromJson<List<RedditPostElement>>(response.string(), mType)
        } catch (e: IOException) {
            logE("Error while parsing respone $e")
        }

        return redditPostElements!!

    }

    override fun refreshNews() {
        after = ""
    }

    override fun deleteAllNews() {
        // Not supported by Reddit :)
    }

    override fun saveRedditNews(data: List<RedditNewsData>) {
        // In this demo app we do not support posting of news, therefore not implemented.
    }
}
