package ch.zuehlke.sbb.reddit.features.overview


import android.content.ContentValues.TAG
import android.util.Log
import ch.zuehlke.sbb.reddit.data.source.RedditRepository
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import com.google.common.base.Preconditions.checkNotNull
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber

/**
 * Created by chsc on 11.11.17.
 */

class OverviewPresenter(private val view: OverviewContract.View, private val redditRepository: RedditRepository) : OverviewContract.Presenter {

    private var mFirstLoad = true

    abstract class PageSubscriber : ResourceSubscriber<List<RedditNewsData>>() {
        override fun onStart() {
            nextPage()
        }

        fun nextPage() {
            Log.d(TAG, "Request next page")
            request(1)
        }
    }

    val reddidSubscriber = object : PageSubscriber() {
        override fun onNext(page: List<RedditNewsData>?) {
            Log.d(TAG, "Got page with ${page?.size} entries")
            page?.let { processTasks(it, true) }
        }

        override fun onComplete() {
            Log.d(TAG, "Reddit subscription completed")
        }

        override fun onError(t: Throwable?) {
            Log.e(TAG, "Reddit subscription failed with $t")
        }
    }

    val redditDisposable: Disposable


    init {
        checkNotNull(view, "OverviewView cannot be null")
        checkNotNull(redditRepository, "RedditRepository cannot be null")
        view.setPresenter(this)
        redditDisposable = redditRepository.news
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), false, 1)
                .subscribeWith(reddidSubscriber)
    }

    override fun start() {
        loadRedditNews(false)
    }

    override fun loadRedditNews(forceUpdate: Boolean) {
        // Simplification for sample: a network reload will be forced on first load.
        loadRedditNews(forceUpdate || mFirstLoad, true)
        mFirstLoad = false

    }


    override fun showRedditNews(redditNewsData: RedditNewsData) {
        checkNotNull(redditNewsData, "redditNewsData cannot be null!")
        view.showRedditNewsDetails(redditNewsData.id!!)
    }


    override fun loadMoreRedditNews() {
        reddidSubscriber.nextPage()
    }

    private fun loadRedditNews(forceUpdate: Boolean, showLoadingUI: Boolean) {
        reddidSubscriber.nextPage()
    }

    private fun processTasks(news: List<RedditNewsData>, added: Boolean) {
        if (news.isEmpty()) {
            // Show a message indicating there are no news for that filter type.
            processEmptyTasks()
        } else {
            // Show the list of news
            if (added) {
                view.addRedditNews(news)
            } else {
                view.showRedditNews(news)
            }

        }
    }

    private fun processEmptyTasks() {
        view.showNoNews()
    }
}
