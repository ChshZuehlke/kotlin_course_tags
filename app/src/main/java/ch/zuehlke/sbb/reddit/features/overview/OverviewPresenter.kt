package ch.zuehlke.sbb.reddit.features.overview

import android.util.Log
import ch.zuehlke.sbb.reddit.data.source.RedditRepository
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import com.google.common.base.Preconditions.checkNotNull
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber

/**
 * Created by chsc on 11.11.17.
 */


class OverviewPresenter(private val view: OverviewContract.View, private val redditRepository: RedditRepository) : OverviewContract.Presenter {

    abstract class PageSubscriber : ResourceSubscriber<Pair<Boolean, List<RedditNewsData>>>() {
        override fun onStart() {
            nextPage()
        }

        fun nextPage() {
            Log.d(TAG, "Request next page")
            request(1)
        }
    }

    fun createReddidSubscriber() = object : PageSubscriber() {
        override fun onNext(response: Pair<Boolean, List<RedditNewsData>>) {
            val (isFirst, page) = response
            Log.d(TAG, "Got ${if(isFirst) "first" else "next"} page with ${page.size} entries")
            processTasks(page, !isFirst)
        }

        override fun onComplete() {
            Log.d(TAG, "Reddit subscription completed")
        }

        override fun onError(t: Throwable?) {
            Log.e(TAG, "Reddit subscription failed with $t")
        }
    }

    val disoposable = CompositeDisposable()
    var reddidSubscriber: PageSubscriber? = null

    init {
        checkNotNull(view, "OverviewView cannot be null")
        checkNotNull(redditRepository, "RedditRepository cannot be null")
        view.setPresenter(this)
    }

    override fun start() {
        loadRedditNews(true)
    }

    override fun stop() {
        disoposable.dispose()
    }

    override fun showRedditNews(redditNewsData: RedditNewsData) {
        checkNotNull(redditNewsData, "redditNewsData cannot be null!")
        view.showRedditNewsDetails(redditNewsData.id!!)
    }

    override fun loadRedditNews(forceUpdate: Boolean) {
        loadRedditNews(forceUpdate, true)
    }

    override fun loadMoreRedditNews() {
        loadRedditNews(false)
    }

    private fun loadRedditNews(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if(forceUpdate || reddidSubscriber == null) {
            Log.d(TAG, "Create new subscription")
            reddidSubscriber = createReddidSubscriber()
            disoposable.add(
                    Flowables.zip(
                            Flowable.just(true, false),
                            redditRepository.news)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread(), false, 1)
                    .subscribeWith(reddidSubscriber!!)
            )
        }
        reddidSubscriber!!.nextPage()
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

    companion object {
        const val TAG = "OverviewPresenter"
    }
}
