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

class FakeRedditNewsRemoteDataSource// Prevent direct instantiation.
 constructor() : RedditDataSource {
    override fun getMoreNews(callback: RedditDataSource.LoadNewsCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPosts(callback: RedditDataSource.LoadPostsCallback, title: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun savePosts(data: RedditPostsData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePostsWithPermaLink(permaLink: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    companion object {

        private var INSTANCE: FakeRedditNewsRemoteDataSource? = null

        private val REDDIT_NEWS_SERVICE_DATA = LinkedHashMap<String, RedditNewsData>()

        fun getInstance(): FakeRedditNewsRemoteDataSource{
            if (INSTANCE == null) {
                INSTANCE = FakeRedditNewsRemoteDataSource()
            }
            return INSTANCE!!
        }
    }
}
