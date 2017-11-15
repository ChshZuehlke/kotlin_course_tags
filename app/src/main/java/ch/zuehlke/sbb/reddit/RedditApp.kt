package ch.zuehlke.sbb.reddit

import android.app.Application
import android.content.Context
import com.github.salomonbrys.kodein.*

/**
 * Created by chsc on 12.11.17.
 */

class RedditApp : Application(), KodeinAware{

    override val kodein by Kodein.lazy {
        import(createBaseModule(this@RedditApp))
    }


    override fun onCreate() {
        super.onCreate()
    }
}
