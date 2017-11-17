package ch.zuehlke.sbb.reddit.extensions

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity



import com.google.common.base.Preconditions.checkNotNull

/**
 * Created by chsc on 08.11.17.
 */

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
    checkNotNull(fragment)
    checkNotNull(frameId)
    val transaction = supportFragmentManager.beginTransaction()
    transaction.apply {
        add(frameId,fragment)
        commit()
    }
}

