package ch.zuehlke.sbb.reddit

import android.app.Application
import android.arch.persistence.room.Room
import ch.zuehlke.sbb.reddit.models.MIGRATION_1_2
import com.github.salomonbrys.kodein.*
import de.dabotz.shoppinglist.database.AppDatabase

/**
 * Created by chsc on 12.11.17.
 */

class RedditApp : Application(), KodeinAware{


    override val kodein by Kodein.lazy {
        import(createBaseModule(this@RedditApp))

        bind<AppDatabase>() with eagerSingleton {
            Room.databaseBuilder(this@RedditApp, AppDatabase::class.java, "reddit-db")
                    .addMigrations(MIGRATION_1_2)
                    .build() }
    }

}
