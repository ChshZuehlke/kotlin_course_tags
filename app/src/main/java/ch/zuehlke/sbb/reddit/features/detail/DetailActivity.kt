package ch.zuehlke.sbb.reddit.features.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log

import ch.zuehlke.sbb.reddit.Injection
import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.extensions.addFragment
import ch.zuehlke.sbb.reddit.extensions.logI
import kotlinx.android.synthetic.main.activity_detail.*

/**
 * Created by chsc on 11.11.17.
 */

class DetailActivity : AppCompatActivity() {

    private var mOverviewPresenter: DetailPresenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Set up the toolbar.
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
        }

        val redditUrl = intent.getStringExtra(EXTRA_REDDIT_NEWS_URL)
        logI("RedditUrl: $redditUrl")
        var detailFragment: DetailFragment? = supportFragmentManager.findFragmentById(R.id.contentFrame) as DetailFragment?
        if (detailFragment == null) {
            // Create the fragment
            detailFragment = DetailFragment.newInstance()
            addFragment(detailFragment, R.id.contentFrame)
        }

        // Create the presenter
        mOverviewPresenter = DetailPresenter(detailFragment, Injection.provideRedditNewsRepository(this), redditUrl, Injection.getRedditAPI(Injection.retroFit))
    }

    companion object {

        val EXTRA_REDDIT_NEWS_URL = "redditNewsUrl"
    }
}
