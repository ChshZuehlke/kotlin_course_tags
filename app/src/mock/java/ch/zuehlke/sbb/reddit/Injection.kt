package ch.zuehlke.sbb.reddit

import android.content.Context
import android.support.annotation.NonNull
import ch.zuehlke.sbb.reddit.data.FakeRedditNewsRemoteDataSource
import ch.zuehlke.sbb.reddit.data.source.RedditRepository
import ch.zuehlke.sbb.reddit.data.source.local.RedditNewsLocalDataSource
import ch.zuehlke.sbb.reddit.data.source.remote.RedditElementTypeAdapterFactory.Companion.elementTypeAdapterFactory
import ch.zuehlke.sbb.reddit.data.source.remote.model.posts.RedditPostElement
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.google.common.base.Preconditions.checkNotNull
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

/**
 * Created by chsc on 08.11.17.
 */

object Injection {

    val kodein = Kodein{

        bind<Gson>() with singleton {
            GsonBuilder()
                    .registerTypeAdapterFactory(elementTypeAdapterFactory)
                    .create()
        }
    }

    val gson = GsonBuilder()
            .registerTypeAdapterFactory(elementTypeAdapterFactory)
            .create()


    fun provideRedditNewsRepository(@NonNull context: Context): RedditRepository {
        checkNotNull(context)
        return RedditRepository.getInstance(FakeRedditNewsRemoteDataSource.getInstance(),
                RedditNewsLocalDataSource.getInstance(context), context)
    }

    val type = object : TypeToken<List<RedditPostElement>>() {

    }.type
}
