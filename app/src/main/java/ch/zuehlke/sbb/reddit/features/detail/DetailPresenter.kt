package ch.zuehlke.sbb.reddit.features.detail

import android.util.Log
import ch.zuehlke.sbb.reddit.data.source.RedditRepository
import com.google.common.base.Preconditions.checkNotNull
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by chsc on 13.11.17.
 */

class DetailPresenter(detailView: DetailContract.View, repository: RedditRepository, redditUrl: String) : DetailContract.Presenter {

    private val mRedditUrl: String
    private val mDetailView: DetailContract.View = checkNotNull(detailView, "The DetailView cannot be null")
    private val mRepository: RedditRepository = checkNotNull(repository, "The repository cannot be null")
    private val mDisposables = CompositeDisposable()

    init {
        checkNotNull(redditUrl, "The reddit url cannot be null")
        mRedditUrl = checkNotNull(redditUrl, "The reddit url cannot be null")
        detailView.setPresenter(this)
    }

    override fun start() {
        loadRedditPosts()
    }

    override fun stop() {
        mDisposables.dispose()
    }


    override fun loadRedditPosts() {
        if (mDetailView.isActive) {
            mDetailView.setLoadingIndicator(true)
        }

        val subscription = mRepository
                .posts(mRedditUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { _ -> mDetailView.isActive }
                .subscribeBy(
                        onError = { error ->
                            Log.e(TAG, "Error when loading reddit posts $mRedditUrl: $error")
                            with(mDetailView) {
                                setLoadingIndicator(false)
                                showRedditNewsLoadingError()
                            }
                        },
                        onNext = { posts ->
                            Log.d(TAG, "Updating the content of the posts")
                            with(mDetailView) {
                                setLoadingIndicator(false)
                                showRedditPosts(posts)
                            }
                        })

        mDisposables.add(subscription)
    }

    companion object {
        const val TAG = "DetailPresenter"
    }
}
