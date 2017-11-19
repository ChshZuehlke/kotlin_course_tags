package ch.zuehlke.sbb.reddit.features.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.data.source.RedditRepository
import ch.zuehlke.sbb.reddit.util.ActivityUtils
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.ActivityInjector
import com.github.salomonbrys.kodein.instance


/**
 * Created by chsc on 11.11.17.
 */

class DetailActivity : AppCompatActivity(), ActivityInjector {

    override val injector: KodeinInjector = KodeinInjector()

    val redditRepository: RedditRepository by instance()

    private var mOverviewPresenter: DetailPresenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        initializeInjector()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Set up the toolbar.
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(false)

        val redditUrl = intent.getStringExtra(EXTRA_REDDIT_NEWS_URL)
        Log.i(TAG, "RedditUrl: " + redditUrl)
        var detailFragment: DetailFragment? = supportFragmentManager.findFragmentById(R.id.contentFrame) as DetailFragment?
        if (detailFragment == null) {
            // Create the fragment
            detailFragment = DetailFragment.newInstance()
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, detailFragment, R.id.contentFrame)
        }

        // Create the presenter
        mOverviewPresenter = DetailPresenter(detailFragment, redditRepository, redditUrl)
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyInjector()
    }

    companion object {
        const val EXTRA_REDDIT_NEWS_URL = "redditNewsUrl"
        private const val TAG = "DetailActivity"
    }
}
