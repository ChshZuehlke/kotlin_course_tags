package ch.zuehlke.sbb.reddit.features.login

import com.google.common.base.Preconditions.checkNotNull
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit

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

        launch(UI) {
            delay(1000, TimeUnit.MILLISECONDS)
            if (mLoginView.isActive) {

                var hasError = false
                if (userEmail != "test.tester@test.com") {
                    mLoginView.showInvalidUsername()
                    hasError = true
                }

                if (password != "123456") {
                    mLoginView.showInvalidPassword()
                    hasError = true

                    mLoginView.showInvalidPasswordTimeout(10)
                    delay(10, TimeUnit.SECONDS)
                }

                if (!hasError) {
                    mLoginView.showRedditNews()
                }
                mLoginView.setLoadingIndicator(false)
            }
        }
    }


}
