package ch.zuehlke.sbb.reddit.util

import android.util.Log

/**
 * Created by celineheldner on 11.12.17.
 */
interface LoggerInterface {
    fun d(TAG: String, message: String)

    fun e(TAG: String, message: String,throwable: Throwable? = null)

    fun i(TAG: String, message: String)

    fun w(TAG: String, message: String)
}