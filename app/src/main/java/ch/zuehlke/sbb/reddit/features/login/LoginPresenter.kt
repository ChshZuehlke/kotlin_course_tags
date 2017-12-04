package ch.zuehlke.sbb.reddit.features.login

import android.os.AsyncTask
import android.os.Handler
import com.google.common.base.Preconditions.checkNotNull

/**
 * Created by chsc on 08.11.17.
 */

class LoginPresenter(view: LoginContract.View) : LoginContract.Presenter {

    private val mLoginView: LoginContract.View


    init {
        this.mLoginView = checkNotNull(view, "LoginView cannot be null")
    }

    override fun start() {
        // Do nothing here, as we don't load any redditPost
    }

    override fun stop() {
        // Do nothing here, as we don't load any redditPost
    }

    override fun login(userEmail: String, password: String) {
        mLoginView.setLoadingIndicator(true)
        // Simulate a 'long' network call to verify the credentials
        Handler().postDelayed({
            object : AsyncTask<Void, Void, Void>() {
                public override fun doInBackground(vararg voids: Void): Void? {
                    if (mLoginView.isActive) {

                        var hasError = false
                        if (userEmail != "test.tester@test.com") {
                            mLoginView.showInvalidUsername()
                            hasError = true
                        }

                        if (password != "123456") {
                            mLoginView.showInvalidPassword()
                            hasError = true
                        }

                        if (!hasError) {
                            mLoginView.showRedditNews()
                        }
                        mLoginView.setLoadingIndicator(false)
                    }
                    return null
                }
            }.doInBackground()
        }, 1000)

    }


}
