package ch.zuehlke.sbb.reddit.data

import android.support.annotation.NonNull
import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import io.reactivex.Flowable
import java.util.*

/**
 * Created by chsc on 08.11.17.
 */

class FakeRedditNewsRemoteDataSource// Prevent direct instantiation.
 constructor() : RedditDataSource {
    override val news: Flowable<List<RedditNewsData>>
        get() = Flowable.fromCallable {
            REDDIT_NEWS_SERVICE_DATA.values.toList()
        }

    private val REDDIT_NEWS_SERVICE_DATA = LinkedHashMap<String, RedditNewsData>()


    override fun getPosts(callback: RedditDataSource.LoadPostsCallback, title: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun savePosts(data: List<RedditPostsData>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePostsWithPermaLink(permaLink: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllNews() {
        // Not supported by Reddit :)
    }


    override fun saveRedditNews(@NonNull data: List<RedditNewsData>) {
        // In this demo app we do not support posting of news, therefore not implemented.
    }

}
