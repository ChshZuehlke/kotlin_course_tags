package ch.zuehlke.sbb.reddit.features.detail

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.data.source.remote.model.posts.RedditPost
import ch.zuehlke.sbb.reddit.features.overview.InfiniteScrollListener
import ch.zuehlke.sbb.reddit.features.overview.ScrollChildSwipeRefreshLayout
import ch.zuehlke.sbb.reddit.models.RedditPostsData

import com.google.common.base.Preconditions.checkNotNull
import kotlinx.android.synthetic.main.fragment_detail.*


/**
 * Created by chsc on 13.11.17.
 */

class DetailFragment : Fragment(), DetailContract.View {

    private var mPresenter: DetailContract.Presenter? = null
    private var mAdapter: DetailAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = DetailAdapter(context)
    }

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
        mPresenter!!.start()
    }

    override fun setPresenter(presenter: DetailContract.Presenter) {
        mPresenter = checkNotNull(presenter)
    }

    override val isActive: Boolean
        get() = isAdded

    override fun showRedditPosts(posts: List<RedditPostsData>) {
        Log.i(TAG, "Got " + posts.size + " posts")
        mAdapter!!.clearAndAddPosts(posts)
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

        fun newInstance(): DetailFragment {
            return DetailFragment()
        }
    }
}
