package ch.zuehlke.sbb.reddit.features.login

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by celineheldner on 20.11.17.
 */
class LoginPresenterTest {

    val presenter: LoginPresenter
    val loginContract: LoginContract.View

    init {
        loginContract = mock<LoginContract.View>()
        presenter = LoginPresenter(loginContract)
    }

    @Test
    fun loginWithWrongUsername() {
        presenter.login("wrong user","123456")
        Thread.sleep(1100)
        verify(loginContract.showInvalidUsername())
    }

}