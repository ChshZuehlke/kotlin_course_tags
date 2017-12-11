package ch.zuehlke.sbb.reddit.data.source.remote

import ch.zuehlke.sbb.reddit.createBaseModule
import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.TimeUnit

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
        //given
        val subscriber = TestSubscriber<List<RedditNewsData>>()

        //when
        redditRemoteData.news.subscribe(subscriber)
        subscriber.awaitDone(3,TimeUnit.SECONDS) //Wartet Terminal Event und bricht die Sequenz ab nach dem Timeout

        //then
        subscriber.assertNever(ArrayList<RedditNewsData>())
        Assert.assertTrue(subscriber.values().size>0)
        Assert.assertTrue(subscriber.values().get(0).size>0)
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
        Assert.assertTrue(observer.values().get(0).size>0)
    }

}