package ch.zuehlke.sbb.reddit

import android.content.Context
import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.data.source.RedditRepository
import ch.zuehlke.sbb.reddit.data.source.local.RedditNewsLocalDataSource
import ch.zuehlke.sbb.reddit.data.source.remote.RedditAPI
import ch.zuehlke.sbb.reddit.data.source.remote.RedditElementTypeAdapterFactory
import ch.zuehlke.sbb.reddit.data.source.remote.RedditNewsDataRemoteDataSource
import ch.zuehlke.sbb.reddit.data.source.remote.model.posts.RedditPostElement
import com.github.salomonbrys.kodein.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier

/**
 * Created by celineheldner on 15.11.17.
 */

fun createBaseModule(context: Context) = Kodein.Module{

    val REDDIT_END_POINT = "https://www.reddit.com/r/dota2/"

    val type = object : TypeToken<List<RedditPostElement>>() {

    }.type

    bind<Retrofit>() with singleton {
        val gson = GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapterFactory(RedditElementTypeAdapterFactory.elementTypeAdapterFactory)
                .create()
        Retrofit.Builder()
                .baseUrl(REDDIT_END_POINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    bind<RedditAPI>() with singleton { instance<Retrofit>().create<RedditAPI>(RedditAPI::class.java!!)  }

    bind<Gson>() with singleton {
        GsonBuilder()
                .registerTypeAdapterFactory(RedditElementTypeAdapterFactory.elementTypeAdapterFactory)
                .create()
    }


    /*
    TODO: kodein_exercise1:  Binde die beiden "RedditDataSource": "RedditNewsDataRemoteDataSource" und "RedditNewsLocalDataSource" als Singletons.
    TODO: kodein_exercise1:  Hint: Tagging und Transitive Abhängigkeiten werden benötigt
     */


    /*
    TODO: kodein_exercise1:  Binde das "RedditRepository"
    TODO: kodein_exercise1:  Hint: Tagging und Transitive Abhängigkeiten werden benötigt
     */


}