package ch.zuehlke.sbb.reddit.data.source.remote

import ch.zuehlke.sbb.reddit.createBaseModule
import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by celineheldner on 11.12.17.
 */
class RedditNewsDataRemoteDataSourceTest {

    val kodein = Kodein{
        import(createBaseModule())
    }

    val redditRemoteData: RedditDataSource = kodein.instance("remoteDS")

    @Test
    fun basic(){
        Assert.assertNotNull(redditRemoteData)
    }


    @Test
    fun getNews() {
    }

    @Test
    fun refreshNews() {
    }

    @Test
    fun posts() {
        //given a TestObserver if substriptions are not running on the calling Thread
        val observer = TestObserver<List<RedditPostsData>>()

        //when we call posts from the RemoteData und subscribe with our testoberver
        redditRemoteData.posts("7itax0").subscribe(observer)

        //then
        observer.assertComplete()
        observer.assertNoErrors()
        Assert.assertTrue(observer.values().size>0)


    }

}