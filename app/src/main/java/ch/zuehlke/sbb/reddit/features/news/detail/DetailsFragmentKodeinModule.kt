package ch.zuehlke.sbb.reddit.features.news.detail


import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.android.androidSupportFragmentScope
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.scopedSingleton

/**
 * Created by celineheldner on 17.11.17.
 */

object DetailsFragmentKodeinModule{

    fun createNewsDetailsModule(view: DetailContract.View, redditUrl: String) = Kodein.Module{

    bind<DetailContract.Presenter>() with scopedSingleton(androidSupportFragmentScope){
        DetailPresenter(view,instance(), it.arguments.getString(redditUrl))
    }


    }
}

