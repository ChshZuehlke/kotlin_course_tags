package ch.zuehlke.sbb.reddit.data

import android.support.annotation.NonNull
import com.google.common.collect.Lists

import java.util.LinkedHashMap

import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData

/**
 * Created by chsc on 08.11.17.
 */

//TODO: kodein_exercise1: Verändere diese Klasse, so dass sie direkt instanziert werden kann. Entferne die Singleton Implementation

class FakeRedditNewsRemoteDataSource
/* Prevent direct instantiation.*/ private constructor() : RedditDataSource {

    private val REDDIT_NEWS_SERVICE_DATA = LinkedHashMap<String, RedditNewsData>()


    companion object {

        private var INSTANCE: FakeRedditNewsRemoteDataSource? = null


        fun getInstance(): FakeRedditNewsRemoteDataSource{
            if (INSTANCE == null) {
                INSTANCE = FakeRedditNewsRemoteDataSource()
            }
            return INSTANCE!!
        }
    }

    override fun getMoreNews(callback: RedditDataSource.LoadNewsCallback) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPosts(callback: RedditDataSource.LoadPostsCallback, title: String) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun savePosts(data: RedditPostsData) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePostsWithPermaLink(permaLink: String) {
        //To change body of created functions use File | Settings | File Templates.
    }


    override fun getNews(@NonNull callback: RedditDataSource.LoadNewsCallback) {
        callback.onNewsLoaded(Lists.newArrayList(REDDIT_NEWS_SERVICE_DATA.values))
    }

    override fun refreshNews() {
        // Not required because the {@link ch.zuehlke.sbb.reddit.data.source.RedditRepository} handles the logic of refreshing the
        // news from all the available data sources.
    }

    override fun deleteAllNews() {
        // Not supported by Reddit :)
    }


    override fun saveRedditNews(@NonNull data: RedditNewsData) {
        // In this demo app we do not support posting of news, therefore not implemented.
    }

}
