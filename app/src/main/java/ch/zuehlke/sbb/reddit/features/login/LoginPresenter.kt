package ch.zuehlke.sbb.reddit.features.login

import com.google.common.base.Preconditions.checkNotNull
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
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
            if (mLoginView.isActive) {

                var hasError = false
                // Validate the username asynchrounous - validate the username in a function and simulate a long running operation using delay
                if (validateUsername(userEmail)) {
                    mLoginView.showInvalidUsername()
                    hasError = true
                }
                if (password != "123456") {
                    mLoginView.showInvalidPassword()
                    hasError = true
                    // TODO: Block the use of the login using 'mLoginView.showInvalidPasswordTimeout' for 10 seconds
                }

                if (!hasError) {
                    mLoginView.showRedditNews()
                }
                if (mLoginView.isActive) {
                    mLoginView.setLoadingIndicator(false)
                }
            }
        }
    }

    suspend private fun validateUsername(userEmail: String): Boolean {
        delay(1000, TimeUnit.MILLISECONDS)
        return userEmail != "test.tester@test.com"
    }


}
