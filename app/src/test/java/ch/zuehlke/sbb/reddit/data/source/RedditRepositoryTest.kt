package ch.zuehlke.sbb.reddit.data.source

import ch.zuehlke.sbb.reddit.kodein.createBaseModule
import ch.zuehlke.sbb.reddit.mock.RedditDataSourceMock
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.util.AndroidUtils
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test

/**
 * Created by celineheldner on 20.11.17.
 */
class RedditRepositoryTest {

    val kodein = Kodein {
        bind<AndroidUtils>() with singleton {
            mock<AndroidUtils> {
                on { isNetworkAvailable() } doReturn true
            }
        }
        import(createBaseModule())
    }
    val repositoryToTest: RedditRepository = kodein.instance()

    var newsCallback: RedditDataSource.LoadNewsCallback? = null
    var postsCallback: RedditDataSource.LoadPostsCallback? = null

    @Before
    fun setUp() {
        newsCallback = mock<RedditDataSource.LoadNewsCallback>()
        postsCallback = mock<RedditDataSource.LoadPostsCallback>()
    }

    @Test
    fun getMoreNews() {
        assert(true)
    }

    @Test
    fun getNews() {
        //given
        repositoryToTest.refreshNews()

        //when
        repositoryToTest.getNews(newsCallback!!)

        //then
        val expectedNews = ArrayList<RedditNewsData>()
        expectedNews.addAll(RedditDataSourceMock.initialNews)
        verify(newsCallback!!).onNewsLoaded(expectedNews)
    }

    @Test
    fun getNewsWithoutConnection() {
        //given
        val androidUtilNoConnection = mock<AndroidUtils> {
            on { isNetworkAvailable() } doReturn false
        }
        var repositoryNoconnection = RedditRepository(kodein.instance("remoteDSMock"), kodein.instance("localDSMock"), androidUtilNoConnection)

        //when
        repositoryNoconnection.refreshNews()
        repositoryNoconnection.getNews(newsCallback!!)

        //then
        verify(newsCallback!!).onNewsLoaded(ArrayList<RedditNewsData>())
    }

    @Test
    fun refreshNews() {
        assert(true)
    }

    @Test
    fun getPosts() {
        //when
        repositoryToTest.getPosts(postsCallback!!, "/r/DotA2/comments/7ckn4x/nov_13_competitive_matches_southeast_asia/")

        //then
        verify(postsCallback!!, times(2)).onPostsLoaded(any())
    }
}