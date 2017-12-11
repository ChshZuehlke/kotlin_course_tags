package ch.zuehlke.sbb.reddit.features.news

import android.support.v7.app.AppCompatActivity
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.android.androidActivityScope
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.scopedSingleton

/**
 * Created by celineheldner on 17.11.17.
 */
fun createNewsActivityModule(appCompatActivity: AppCompatActivity, fragmentContainerId: Int) = Kodein.Module{

    bind<NavigationController>() with scopedSingleton(androidActivityScope){
        NavigationController(appCompatActivity,fragmentContainerId)
    }

}