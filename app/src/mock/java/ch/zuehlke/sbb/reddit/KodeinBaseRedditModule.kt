package ch.zuehlke.sbb.reddit

import android.content.Context
import ch.zuehlke.sbb.reddit.data.FakeRedditNewsRemoteDataSource
import ch.zuehlke.sbb.reddit.data.source.RedditRepository
import ch.zuehlke.sbb.reddit.data.source.local.RedditNewsLocalDataSource
import ch.zuehlke.sbb.reddit.data.source.remote.RedditElementTypeAdapterFactory
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.eagerSingleton
import com.github.salomonbrys.kodein.singleton
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Created by celineheldner on 15.11.17.
 */

fun createBaseModule(context: Context) = Kodein.Module{


    bind<Gson>() with singleton {
        GsonBuilder()
                .registerTypeAdapterFactory(RedditElementTypeAdapterFactory.elementTypeAdapterFactory)
                .create()
    }

    //TODO: kodein_exercise1: Bind die FakeRedditNewsRemoteDataSource und die RedditNewsLocalDataSource mit einem Singleton

    //TODO: kodein_exercise1: Binde die RedditRepository mit einem Singleton. Benutze die transitive abhängigkeit von Kodein um sie zu instanzieren.

}