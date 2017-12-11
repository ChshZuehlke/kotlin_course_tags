package ch.zuehlke.sbb.reddit

import android.app.Application
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.lazy

/**
 * Created by chsc on 12.11.17.
 */

class RedditApp : Application(), KodeinAware{

    override val kodein by Kodein.lazy {
        import(createBaseModule(this@RedditApp))
    }


    override fun onCreate() {
        super.onCreate()
        //TODO: kodein_exercise2b

    }
}
