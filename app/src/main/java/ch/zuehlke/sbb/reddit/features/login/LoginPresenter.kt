package ch.zuehlke.sbb.reddit.features.login

import android.os.AsyncTask
import android.os.Handler
import com.google.common.base.Preconditions.checkNotNull
import com.google.common.base.Strings

/**
 * Created by chsc on 08.11.17.
 */

class LoginPresenter(view: LoginContract.View) : LoginContract.Presenter {

    private fun verifyPasswordLength(password: String): Boolean {
        return !Strings.isNullOrEmpty(password) && password.length >= 6
    }

    private fun verifyIsEmail(email: String): Boolean {
        val matcher = android.util.Patterns.EMAIL_ADDRESS.matcher(email)
        return matcher.matches()
    }

    override fun validateUserName(userName: String): ValidationResult {
        return if(verifyIsEmail(userName)) {
            Ok
        } else {
            Failed("Username must be an email address")
        }
    }

    override fun validatePassword(password: String): ValidationResult {
        return if(verifyPasswordLength(password)) {
            Ok
        } else {
            Failed("Password must be 6 or more long")
        }
    }

    private val mLoginView: LoginContract.View = checkNotNull(view, "LoginView cannot be null")


    init {
        view.setPresenter(this)
    }

    override fun start() {
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
                            mLoginView.showUsernameResult(Failed("Username must be a test email"))
                            hasError = true
                        }

                        if (password != "123456") {
                            mLoginView.showPasswordResult(Failed("Password must be very secure"))
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
