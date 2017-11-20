package ch.zuehlke.sbb.reddit.data.source

import ch.zuehlke.sbb.reddit.kodein.createBaseModule
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import ch.zuehlke.sbb.reddit.util.AndroidUtils
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by celineheldner on 20.11.17.
 */
class RedditRepositoryTest {

    val kodein =  Kodein {
        bind<AndroidUtils>() with singleton { mock<AndroidUtils>{
            on{ isNetworkAvailable()} doReturn true
        } }
        import(createBaseModule())
    }
    val repositoryToTest: RedditRepository = kodein.instance()

    var newsCallback: RedditDataSource.LoadNewsCallback? = null
    var postsCallback: RedditDataSource.LoadPostsCallback? = null
    var newsLoadedNbrCalls = 0
    var postsLoadedNbrCalls = 0
    var currentNews: List<RedditNewsData> = ArrayList<RedditNewsData>()
    var currentPosts: List<RedditPostsData> = ArrayList<RedditPostsData>()

    @Before
    fun setUp() {
        newsLoadedNbrCalls = 0
        postsLoadedNbrCalls = 0
        newsCallback = object: RedditDataSource.LoadNewsCallback{
            override fun onNewsLoaded(news: List<RedditNewsData>) {
                currentNews = news
                when(newsLoadedNbrCalls){
                    //First call comes from LocalDatasource, which is empty at first
                    0 -> assert(news.isEmpty())
                    //Second call should come from RemoteDataSource, because the Mock has a timeout of 1000 Milliseconds
                    1 -> assertEquals(2, news.size)
                    //Third call comes from LocalDatasource, which was previously saved
                    2 -> assertEquals(2, news.size)
                }
                newsLoadedNbrCalls++
            }

            override fun onDataNotAvailable() {
            }
        }

        postsCallback = object: RedditDataSource.LoadPostsCallback{
            override fun onPostsLoaded(posts: List<RedditPostsData>) {
                currentPosts = posts
                when (postsLoadedNbrCalls) {
                    0 -> assert(posts.isEmpty())
                    1 -> assertEquals(2, posts.size)
                }
            }

            override fun onDataNotAvailable() {
                assert(false)
            }
        }
    }

    @Test
    fun getMoreNews() {
        assert(true)
    }

    @Test
    fun getNews() {
        repositoryToTest.refreshNews()
        repositoryToTest.getNews(newsCallback!!)
        repositoryToTest.getNews(newsCallback!!)
    }

    @Test
    fun refreshNews() {
        assert(true)
    }

    @Test
    fun getPosts() {
        repositoryToTest.getPosts(postsCallback!!,"/r/DotA2/comments/7ckn4x/nov_13_competitive_matches_southeast_asia/")
        repositoryToTest.getPosts(postsCallback!!,"/r/DotA2/comments/7ckn4x/nov_13_competitive_matches_southeast_asia/")
    }
}