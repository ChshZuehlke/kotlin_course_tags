package ch.zuehlke.sbb.reddit.features.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.extensions.addFragment
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.AppCompatActivityInjector
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance

/**
 * Created by chsc on 08.11.17.
 */

class LoginActivity : AppCompatActivity(), AppCompatActivityInjector{

    override val injector: KodeinInjector = KodeinInjector()

    override fun provideOverridingModule() = Kodein.Module {
        bind<LoginActivity>() to instance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeInjector()

        setContentView(R.layout.activity_login)


        var loginFragment: LoginFragment? = supportFragmentManager.findFragmentById(R.id.contentFrame) as LoginFragment?
        if (loginFragment == null) {
            // Create the fragment
            loginFragment = LoginFragment.newInstance()
            addFragment(loginFragment!!, R.id.contentFrame)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyInjector()
    }
}
