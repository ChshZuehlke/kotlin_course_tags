package ch.zuehlke.sbb.reddit.util

import android.util.Log

/**
 * Created by celineheldner on 11.12.17.
 */
class ConfigurableLogger: LoggerInterface{
    override fun d(TAG: String, message: String){
        Log.d(TAG,message)
    }

    override fun e(TAG: String, message: String, throwable: Throwable?){
        Log.d(TAG,message,throwable)
    }

    override fun i(TAG: String, message: String){
        Log.i(TAG,message)
    }

    override fun w(TAG: String, message: String){
        Log.w(TAG,message)
    }

}