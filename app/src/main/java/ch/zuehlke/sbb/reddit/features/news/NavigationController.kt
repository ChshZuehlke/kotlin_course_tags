package ch.zuehlke.sbb.reddit.features.news

import android.support.v7.app.AppCompatActivity
import ch.zuehlke.sbb.reddit.extensions.addFragment
import ch.zuehlke.sbb.reddit.features.news.detail.DetailFragment
import ch.zuehlke.sbb.reddit.features.news.overview.OverviewFragment

/**
 * Created by celineheldner on 17.11.17.
 */
class NavigationController constructor(activity: AppCompatActivity, fragmentContainerId: Int?){
    val mActivity = activity
    val mContainerId = fragmentContainerId

    fun showOverview(){
        var overviewFragment: OverviewFragment? = mActivity.supportFragmentManager.findFragmentById(mContainerId!!) as OverviewFragment?
        if (overviewFragment == null) {
            // Create the fragment
            overviewFragment = OverviewFragment.newInstance()
            mActivity.addFragment(overviewFragment!!, mContainerId)
        }
    }

    fun showDetails(redditUrl: String?){
        // Create the fragment
        val detailFragment: DetailFragment = DetailFragment.newInstance(redditUrl!!)
        mActivity.addFragment( detailFragment, mContainerId!!)
    }
}