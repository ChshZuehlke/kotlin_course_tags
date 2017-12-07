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

    /**
     * The Datasources don't need to be implemented as singletons anymore. Kodein can handle that
     * Binding the two Datasources as Singletons
     * Needs Tagging to differentiate the instances
     */
    bind<RedditDataSource>("remoteDS") with singleton { RedditNewsDataRemoteDataSource(context, instance(),instance(), type) }
    bind<RedditDataSource>("localDS") with singleton { RedditNewsLocalDataSource(context) }

    /**
     * the RedditRepository does not need to be implemented as a singleton -> Kodein can handle that
     * Retrievs the two DataSource-Instances bound previously using a tag
     */
    bind<RedditRepository>() with singleton {
        RedditRepository(
                instance("remoteDS"),
                instance("localDS"),
                context)
    }


}