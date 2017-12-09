package ch.zuehlke.sbb.reddit.features.overview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.data.FakeRedditNewsRemoteDataSource
import ch.zuehlke.sbb.reddit.data.source.RedditRepository
import ch.zuehlke.sbb.reddit.data.source.local.RedditNewsLocalDataSource
import ch.zuehlke.sbb.reddit.util.ActivityUtils
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.ActivityInjector

/**
 * Created by chsc on 11.11.17.
 */

class OverviewActivity : AppCompatActivity(), ActivityInjector {

    override val injector: KodeinInjector = KodeinInjector()

    //TODO: kodein_exercise1: Benutze Kodein um das RedditRepository abzurufen.

    private var mOverviewPresenter: OverviewPresenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Kodein Injector wird initialisiert
        initializeInjector()

        setContentView(R.layout.activity_overview)

        // Set up the toolbar.
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(false)


        var overviewFragment: OverviewFragment? = supportFragmentManager.findFragmentById(R.id.contentFrame) as OverviewFragment?
        if (overviewFragment == null) {
            // Create the fragment
            overviewFragment = OverviewFragment.newInstance()
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, overviewFragment!!, R.id.contentFrame)
        }

        //TODO: kodein_exercise1: entferne diese Variable 'redditRepository' hier und benutze die oben von Kodein instanzierte!

        val redditRepository = RedditRepository.getInstance(FakeRedditNewsRemoteDataSource.getInstance(),
                RedditNewsLocalDataSource.getInstance(this), this)

        // Create the presenter
        mOverviewPresenter = OverviewPresenter(overviewFragment, redditRepository)
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyInjector()
    }
}
