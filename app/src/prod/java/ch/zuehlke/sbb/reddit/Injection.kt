package ch.zuehlke.sbb.reddit


import android.content.Context

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Modifier
import java.lang.reflect.Type

import ch.zuehlke.sbb.reddit.data.source.RedditRepository
import ch.zuehlke.sbb.reddit.data.source.local.RedditNewsLocalDataSource
import ch.zuehlke.sbb.reddit.data.source.remote.RedditAPI
import ch.zuehlke.sbb.reddit.data.source.remote.RedditElementTypeAdapterFactory.Companion.elementTypeAdapterFactory
import ch.zuehlke.sbb.reddit.data.source.remote.RedditNewsDataRemoteDataSource
import ch.zuehlke.sbb.reddit.data.source.remote.model.posts.RedditPostElement
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.github.salomonbrys.kodein.*


import com.google.common.base.Preconditions.checkNotNull

/**
 * Enables injection of production implementations for
 * [ch.zuehlke.sbb.reddit.data.source.RedditDataSource] at compile time.
 */
object Injection {

    val kodein = Kodein{

        bind<Retrofit>() with singleton {
            val gson = GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    .excludeFieldsWithoutExposeAnnotation()
                    .registerTypeAdapterFactory(elementTypeAdapterFactory)
                    .create()
            Retrofit.Builder()
                    .baseUrl(REDDIT_END_POINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
        }

        bind<RedditAPI>() with singleton { instance<Retrofit>().create<RedditAPI>(RedditAPI::class.java!!)  }

        bind<Gson>() with singleton {
            GsonBuilder()
                .registerTypeAdapterFactory(elementTypeAdapterFactory)
                .create()
        }
    }

    private val REDDIT_END_POINT = "https://www.reddit.com/r/dota2/"

    val type = object : TypeToken<List<RedditPostElement>>() {

    }.type

    fun provideRedditNewsRepository(context: Context): RedditRepository {
        checkNotNull(context)
        return RedditRepository.getInstance(RedditNewsDataRemoteDataSource.getInstance(context, kodein.instance(),kodein.instance(), type),
                RedditNewsLocalDataSource.getInstance(context), context)
    }

}
