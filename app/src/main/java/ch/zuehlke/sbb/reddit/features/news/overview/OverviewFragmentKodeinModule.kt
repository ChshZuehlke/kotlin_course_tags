package ch.zuehlke.sbb.reddit.features.news.overview

import ch.zuehlke.sbb.reddit.features.news.GenericBindingViewHolder
import ch.zuehlke.sbb.reddit.features.news.ViewType
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.android.androidSupportFragmentScope
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.scopedSingleton

/**
 * Created by celineheldner on 17.11.17.
 */

object  OverviewFragmentKodeinModule{

    fun createNewsOverviewModule(view: OverviewContract.View, listener: GenericBindingViewHolder.GenericBindingClickListener) = Kodein.Module{

        bind<OverviewContract.Presenter>() with scopedSingleton(androidSupportFragmentScope){
            OverviewPresenter(view, instance())
        }

        //Exercise 01
        // Add a compare function so that the viewtype items can be compared by the diffutil
        // A compare function looks like (T,T) -> Boolean
        bind<OverviewAdapter>() with scopedSingleton(androidSupportFragmentScope){
            OverviewAdapter(listener,{viewType: ViewType, otherViewType: ViewType ->
                when{
                    viewType is RedditNewsData && otherViewType is RedditNewsData -> {
                        viewType.id == otherViewType.id
                    }
                    viewType is RedditNewsData && otherViewType !is RedditNewsData -> {false}
                    viewType !is RedditNewsData && otherViewType is RedditNewsData -> {false}
                    viewType !is RedditNewsData && otherViewType !is RedditNewsData -> {true}
                    else -> {false}
                }

            })
        }
    }
}

