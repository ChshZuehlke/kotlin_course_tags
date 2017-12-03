package ch.zuehlke.sbb.reddit.features.news.detail

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.features.BaseFragment
import ch.zuehlke.sbb.reddit.features.news.detail.DetailsFragmentKodeinModule.createNewsDetailsModule
import ch.zuehlke.sbb.reddit.features.news.overview.ScrollChildSwipeRefreshLayout
import ch.zuehlke.sbb.reddit.models.RedditPostsData

import com.google.common.base.Preconditions.checkNotNull
import kotlinx.android.synthetic.main.fragment_detail.*
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with


/**
 * Created by chsc on 13.11.17.
 */

class DetailFragment: BaseFragment(), DetailContract.View {

    override fun provideOverridingModule() = createNewsDetailsModule(this@DetailFragment,EXTRA_REDDIT_NEWS_URL)
    //injected
    private val mPresenter: DetailContract.Presenter by injector.with(this@DetailFragment).instance()
    private val mAdapter: DetailAdapter by injector.with(this@DetailFragment).instance()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_detail,container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        redditPostView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            setHasFixedSize(true)
        }

        refreshLayout.apply {
            setColorSchemeColors(
                    ContextCompat.getColor(activity, R.color.colorPrimary),
                    ContextCompat.getColor(activity, R.color.colorAccent),
                    ContextCompat.getColor(activity, R.color.colorPrimaryDark)
            )
            setScrollUpChild(redditPostView)
            setOnRefreshListener { mPresenter!!.loadRedditPosts() }
        }
    }


    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    override val isActive: Boolean
        get() = isAdded

    override fun showRedditPosts(posts: List<RedditPostsData>) {
        Log.i(TAG, "Got " + posts.size + " posts")
        mAdapter.clearAndAddPosts(posts)
    }

    override fun showRedditNewsLoadingError() {
        Snackbar.make(view!!, R.string.overview_screen_error_loading_reddit_posts, Snackbar.LENGTH_LONG)
    }

    override fun setLoadingIndicator(isActive: Boolean) {
        if (view == null) {
            return
        }
        // Make sure setRefreshing() is called after the layout is done with everything else.
        refreshLayout.post { refreshLayout.isRefreshing = isActive }
    }

    companion object {

        private val TAG = "DetailFragment"
        val EXTRA_REDDIT_NEWS_URL = "redditNewsUrl"

        fun newInstance(redditUrl: String): DetailFragment {
            val detailFragment = DetailFragment()
            val args = Bundle()
            args.putString(EXTRA_REDDIT_NEWS_URL, redditUrl)
            detailFragment.setArguments(args)
            return detailFragment
        }
    }
}
