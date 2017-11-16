package ch.zuehlke.sbb.reddit.features.detail

import android.content.ContentValues.TAG
import android.util.Log
import ch.zuehlke.sbb.reddit.data.source.RedditRepository
import com.google.common.base.Preconditions.checkNotNull
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by chsc on 13.11.17.
 */

class DetailPresenter(detailView: DetailContract.View, repository: RedditRepository, redditUrl: String) : DetailContract.Presenter {

    private val mRedditUrl: String
    private val mDetailView: DetailContract.View
    private val mRepository: RedditRepository

    init {
        mRepository = checkNotNull(repository, "The repository cannot be null")
        mDetailView = checkNotNull(detailView, "The DetailView cannot be null")
        checkNotNull(redditUrl, "The reddit url cannot be null")
        mRedditUrl = checkNotNull(redditUrl, "The reddit url cannot be null")
        detailView.setPresenter(this)
    }

    override fun start() {
        loadRedditPosts()
    }


    override fun loadRedditPosts() {
        if (mDetailView.isActive) {
            mDetailView.setLoadingIndicator(true)
        }

        mRepository
                .posts(mRedditUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), false, 1)
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
                            with(mDetailView) {
                                setLoadingIndicator(false)
                                showRedditPosts(posts)
                            }
                        })
    }

}
