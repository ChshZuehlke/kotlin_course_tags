package ch.zuehlke.sbb.reddit.util

/**
 * Created by celineheldner on 11.12.17.
 */
class TestLogger: LoggerInterface{
    override fun d(TAG: String, message: String) {
        System.out.println(TAG+": "+message)
    }

    override fun e(TAG: String, message: String, throwable: Throwable? ) {
        System.out.println(TAG+": "+message)
    }


    override fun i(TAG: String, message: String) {
        System.out.println(TAG+": "+message)
    }

    override fun w(TAG: String, message: String) {
        System.out.println(TAG+": "+message)
    }

}