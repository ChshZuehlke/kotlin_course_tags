package ch.zuehlke.sbb.reddit


import android.content.Context
import ch.zuehlke.sbb.reddit.data.source.RedditRepository
import ch.zuehlke.sbb.reddit.data.source.local.RedditNewsLocalDataSource
import ch.zuehlke.sbb.reddit.data.source.remote.RedditAPI
import ch.zuehlke.sbb.reddit.data.source.remote.RedditElementTypeAdapterFactory.Companion.elementTypeAdapterFactory
import ch.zuehlke.sbb.reddit.data.source.remote.RedditNewsDataRemoteDataSource
import ch.zuehlke.sbb.reddit.data.source.remote.model.posts.RedditPostElement
import com.google.common.base.Preconditions.checkNotNull
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier

/**
 * Enables injection of production implementations for
 * [ch.zuehlke.sbb.reddit.data.source.RedditDataSource] at compile time.
 */

//TODO: kodein_exercise1: This class can be completely removed by the end of this exercise

object Injection {

    private val REDDIT_END_POINT = "https://www.reddit.com/r/dota2/"

    private var redditAPI: RedditAPI? = null
    private var retrofit: Retrofit? = null

    val type = object : TypeToken<List<RedditPostElement>>() {

    }.type

    val gson = GsonBuilder()
            .registerTypeAdapterFactory(elementTypeAdapterFactory)
            .create()

    fun provideRedditNewsRepository(context: Context): RedditRepository {
        checkNotNull(context)
        return RedditRepository.getInstance(RedditNewsDataRemoteDataSource.getInstance(context, getRedditAPI(retroFit), gson, type),
                RedditNewsLocalDataSource.getInstance(context), context)
    }

    fun getRedditAPI(retrofit: Retrofit): RedditAPI {
        if (redditAPI == null) {
            redditAPI = retrofit.create<RedditAPI>(RedditAPI::class.java!!)
        }
        return redditAPI!!
    }

    val retroFit: Retrofit
        get() {
            if (retrofit == null) {
                val gson = GsonBuilder()
                        .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                        .excludeFieldsWithoutExposeAnnotation()
                        .registerTypeAdapterFactory(elementTypeAdapterFactory)
                        .create()

                retrofit = Retrofit.Builder()
                        .baseUrl(REDDIT_END_POINT)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }
            return retrofit!!
        }
}
