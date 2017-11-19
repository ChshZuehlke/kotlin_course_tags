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
import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.features.GenericBindingViewHolder
import ch.zuehlke.sbb.reddit.features.overview.ScrollChildSwipeRefreshLayout
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import com.google.common.base.Preconditions.checkNotNull


/**
 * Created by chsc on 13.11.17.
 */

class DetailFragment : Fragment(), DetailContract.View {

    private var mPresenter: DetailContract.Presenter? = null
    private val clickListener =  object: GenericBindingViewHolder.GenericBindingClickListener{
        override fun onItemSelected(obj: Any) {
            // Do nothing
        }
    }
    private var mAdapter: PostAdapter = PostAdapter(clickListener)
    private var mPostView: RecyclerView? = null
    private var mNoPostView: View? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.fragment_detail, container, false)

        mNoPostView = root.findViewById<View>(R.id.noRedditPostVIew)
        mPostView = root.findViewById<RecyclerView>(R.id.redditPostView)
        mPostView!!.layoutManager = LinearLayoutManager(context)
        mPostView!!.adapter = mAdapter

        // Set up progress indicator
        val swipeRefreshLayout = root.findViewById<View>(R.id.refreshLayout) as ScrollChildSwipeRefreshLayout
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(activity, R.color.colorPrimary),
                ContextCompat.getColor(activity, R.color.colorAccent),
                ContextCompat.getColor(activity, R.color.colorPrimaryDark)
        )


        swipeRefreshLayout.setScrollUpChild(mPostView!!)
        swipeRefreshLayout.setOnRefreshListener { mPresenter!!.loadRedditPosts() }


        mPostView!!.setHasFixedSize(true)
        return root
    }


    override fun onResume() {
        super.onResume()
        mPresenter!!.start()
    }

    override fun onPause() {
        super.onPause()
        mPresenter!!.stop()
    }

    override fun setPresenter(presenter: DetailContract.Presenter) {
        mPresenter = checkNotNull(presenter)
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
        val srl = view!!.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post { srl.isRefreshing = isActive }
    }

    companion object {

        private val TAG = "DetailFragment"

        fun newInstance(): DetailFragment {
            return DetailFragment()
        }
    }
}
