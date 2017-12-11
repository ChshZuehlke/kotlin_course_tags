package ch.zuehlke.sbb.reddit.features.news.detail


import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.android.androidSupportFragmentScope
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.scopedSingleton

/**
 * Created by celineheldner on 17.11.17.
 */
fun createNewsDetailsModule(view: DetailContract.View) = Kodein.Module{

    //TODO: kodein_exercise2a: binde den DetailAdapter mit einem AndroidSupportFragment Scoped Singleton

    val redditUrlBundleKey = DetailFragment.EXTRA_REDDIT_NEWS_URL
    //TODO: kodein_exercise2a: binde den DetailContract.Presenter mit einem AndroidSupportFragment Scoped Singleton.
    //TODO: kodein_exercise2a: Hint: Die RedditUrl wird im Intent Bundle mit dem Key: "redditUrlBundleKey"  mitgegeben

}