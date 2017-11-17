package ch.zuehlke.sbb.reddit.features.login

import ch.zuehlke.sbb.reddit.BasePresenter
import ch.zuehlke.sbb.reddit.BaseView

/**
 * Created by chsc on 08.11.17.
 */

interface LoginContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(isActive: Boolean)

        fun showUsernameResult(result: ValidationResult)
        fun showPasswordResult(result: ValidationResult)

        fun showRedditNews()
    }

    interface Presenter : BasePresenter {

        fun login(userEmail: String, password: String)

        fun validateUserName(userName: String): ValidationResult

        fun validatePassword(password: String): ValidationResult
    }
}

