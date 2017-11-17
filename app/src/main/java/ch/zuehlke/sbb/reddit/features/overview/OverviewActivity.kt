package ch.zuehlke.sbb.reddit.features.overview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import ch.zuehlke.sbb.reddit.Injection
import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.extensions.addFragment

/**
 * Created by chsc on 11.11.17.
 */

class OverviewActivity : AppCompatActivity() {

    private var mOverviewPresenter: OverviewPresenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        // Set up the toolbar.
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(false)


        var overviewFragment: OverviewFragment? = supportFragmentManager.findFragmentById(R.id.contentFrame) as OverviewFragment?
        if (overviewFragment == null) {
            // Create the fragment
            overviewFragment = OverviewFragment.newInstance()
            addFragment(overviewFragment!!, R.id.contentFrame)
        }

        // Create the presenter
        mOverviewPresenter = OverviewPresenter(overviewFragment, Injection.provideRedditNewsRepository(this))
    }
}
