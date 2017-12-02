package ch.zuehlke.sbb.reddit.features.overview


import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.data.source.RedditRepository
import ch.zuehlke.sbb.reddit.models.RedditNewsData

import com.google.common.base.Preconditions.checkNotNull

/**
 * Created by chsc on 11.11.17.
 */

class OverviewPresenter(view: OverviewContract.View, redditRepository: RedditRepository) : OverviewContract.Presenter {

    private val mOverviewView: OverviewContract.View
    private var mFirstLoad = true
    private val mRedditRepository: RedditRepository

    init {
        mOverviewView = checkNotNull(view, "OverviewView cannot be null")
        mRedditRepository = checkNotNull(redditRepository, "RedditRepository cannot be null")
        view.setPresenter(this)
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
        mOverviewView.showRedditNewsDetails(redditNewsData.id!!)
    }


    override fun loadMoreRedditNews() {
        mRedditRepository.getMoreNews(object : RedditDataSource.LoadNewsCallback {
            override fun onNewsLoaded(data: List<RedditNewsData>) {
                // The view may not be able to handle UI updates anymore
                if (!mOverviewView.isActive) {
                    return
                }
                processTasks(data, true)
            }

            override fun onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mOverviewView.isActive) {
                    return
                }
                mOverviewView.showRedditNewsLoadingError()
            }
        })

    }

    private fun loadRedditNews(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            mOverviewView.setLoadingIndicator(true)
        }
        if (forceUpdate) {
            mRedditRepository.refreshNews()
        }

        mRedditRepository.getNews(object : RedditDataSource.LoadNewsCallback {
            override fun onNewsLoaded(newsDataList: List<RedditNewsData>) {
                // The view may not be able to handle UI updates anymore
                if (!mOverviewView.isActive) {
                    return
                }
                if (showLoadingUI) {
                    mOverviewView.setLoadingIndicator(false)
                }

                processTasks(newsDataList, false)
            }

            override fun onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mOverviewView.isActive) {
                    return
                }
                mOverviewView.showRedditNewsLoadingError()
            }
        })
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
