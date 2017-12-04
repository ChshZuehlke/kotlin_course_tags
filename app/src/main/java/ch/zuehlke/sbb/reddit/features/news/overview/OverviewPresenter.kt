package ch.zuehlke.sbb.reddit.features.news.overview


import ch.zuehlke.sbb.reddit.data.source.RedditRepository
import ch.zuehlke.sbb.reddit.extensions.logD
import ch.zuehlke.sbb.reddit.extensions.logE
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import com.google.common.base.Preconditions.checkNotNull
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toSingle
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber

/**
 * Created by chsc on 11.11.17.
 */

class OverviewPresenter(
        val view: OverviewContract.View,
        val redditRepository: RedditRepository,
        val mainScheduler: Scheduler = AndroidSchedulers.mainThread(),
        val ioScheduler: Scheduler = Schedulers.io()
) : OverviewContract.Presenter {

    abstract class PageSubscriber : ResourceSubscriber<List<RedditNewsData>>() {
        override fun onStart() {
            nextPage()
        }

        fun nextPage() {
            logD("Request next page")
            request(1) // <<- Generate a single request
        }
    }

    fun createRedditSubscriber() = object : PageSubscriber() {
        override fun onNext(news: List<RedditNewsData>) {
            // The view may not be able to handle UI updates anymore
            if (mOverviewView.isActive) {
                processTasks(news, true)
            }
        }

        override fun onError(t: Throwable?) {
            // The view may not be able to handle UI updates anymore
            logE("Error on subscription: $t")
            if (mOverviewView.isActive) {
                mOverviewView.showRedditNewsLoadingError()
            }
        }

        override fun onComplete() {
            logD("Reddit subscription was completed")
        }

    }


    private val mOverviewView: OverviewContract.View
    private var mFirstLoad = true
    private val mRedditRepository: RedditRepository
    private var currentSubscription: PageSubscriber? = null
    private val disposable = CompositeDisposable()


    init {
        mOverviewView = checkNotNull(view, "OverviewView cannot be null")
        mRedditRepository = checkNotNull(redditRepository, "RedditRepository cannot be null")
    }

    override fun start() {
        loadRedditNews(false)
    }

    override fun stop() {
        disposable.clear()
    }

    override fun loadRedditNews(forceUpdate: Boolean) {
        // Simplification for sample: a network reload will be forced on first load.
        loadRedditNews(forceUpdate || mFirstLoad, true)
        mFirstLoad = false
    }


    override fun showRedditNews(redditNewsData: RedditNewsData) {
        checkNotNull(redditNewsData, "redditNewsData cannot be null!")
        mOverviewView.showRedditNewsDetails(redditNewsData.id!!)
    }


    override fun loadMoreRedditNews() {
        loadRedditNews(false, true)
    }

    private fun loadRedditNews(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            mOverviewView.setLoadingIndicator(true)
        }
        val subscription = currentSubscription

        val news = mRedditRepository.news
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler, false, 1)
        if (forceUpdate || subscription == null) {
            logD("Create new subscription")
            subscription?.let {
                it.dispose()
                disposable.remove(it)
            }
            currentSubscription = createRedditSubscriber()
            news.subscribeWith(currentSubscription)
        } else {
            subscription.nextPage()
        }
        if (showLoadingUI) {
            disposable.add(
                    news.toSingle().subscribeBy {
                        mOverviewView.setLoadingIndicator(false)
                    }
            )
        }
    }

    private fun processTasks(news: List<RedditNewsData>, added: Boolean) {
        if (news.isEmpty()) {
            // Show a message indicating there are no news for that filter type.
            processEmptyTasks()
        } else {
            // Show the list of news
            if (added) {
                mOverviewView.addRedditNews(news)
            } else {
                mOverviewView.showRedditNews(news)
            }
        }
    }


    private fun processEmptyTasks() {
        mOverviewView.showNoNews()
    }
}
