package ch.zuehlke.sbb.reddit.extensions

import android.util.Log
import ch.zuehlke.sbb.reddit.BuildConfig

/**
 * Created by chsc on 17.11.17.
 */

inline fun Any?.logE(message: String){
    this?.let {
       Log.e(this::class.java.simpleName,message)
    }
}

inline fun Any?.logI(message: String){
    this?.let {
        Log.i(this::class.java.simpleName,message)
    }
}

inline fun Any?.logD(message: String){
    if(BuildConfig.DEBUG){
        this?.let {
            Log.d(this::class.java.simpleName,message)
        }
    }
}