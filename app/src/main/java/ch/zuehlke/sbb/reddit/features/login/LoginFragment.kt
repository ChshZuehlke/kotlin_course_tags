package ch.zuehlke.sbb.reddit.features.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.AppCompatButton
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.features.BaseFragment
import ch.zuehlke.sbb.reddit.features.login.LoginActivityKodeinModule.createLoginModule
import ch.zuehlke.sbb.reddit.features.news.NewsActivity
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.google.common.base.Strings
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created by chsc on 08.11.17.
 */

class LoginFragment : BaseFragment(), LoginContract.View {


    override fun provideOverridingModule() = createLoginModule(this@LoginFragment)
    //injected
    private val mPresenter: LoginContract.Presenter by injector.with(this@LoginFragment).instance()

    private val loginListener = View.OnClickListener { mPresenter!!.login(username.text.toString(), password.text.toString()) }

    private val usernameListener = object: TextWatcher{
        override fun afterTextChanged(editable: Editable) {
            if (editable.length > 0 && verifyIsEmail(editable.toString())) {
                username!!.error = null
            } else {
                username!!.error = getString(R.string.login_screen_invalid_email)
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Do nothing
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Do nothing
        }
    }

    // Excersise-DataBinding-01
    // Remove the passwordListener and move the functionality to the password viewmodel


    private val passwordListener = object : TextWatcher{

        override fun afterTextChanged(editable: Editable) {
            if (verifyPasswordLength(editable.toString())) {
                password!!.error = null
            } else {
                password!!.error = getString(R.string.login_screen_invalid_password_length)
            }
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
           // DO nothing
        }


        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Do nothing
        }
    }

    override fun onResume() {
        super.onResume()

        mPresenter.start()
        loginButton.setOnClickListener(loginListener)
        username.addTextChangedListener(usernameListener)
        password.addTextChangedListener(passwordListener)
    }

    override fun onPause() {
        loginButton.setOnClickListener(null)
        username.removeTextChangedListener(usernameListener)
        password.removeTextChangedListener(passwordListener)

        super.onPause()
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = LayoutInflater.from(context).inflate(R.layout.fragment_login,container,false)


    override val isActive: Boolean
        get() = isAdded

    override fun setLoadingIndicator(isActive: Boolean) {
        progressBar.visibility = if (isActive) View.VISIBLE else View.GONE
    }

    override fun showInvalidUsername() {
        username.error = getString(R.string.login_screen_invalid_username)
    }

    override fun showInvalidPassword() {
        password.error = getString(R.string.login_screen_invalid_password)
    }


    override fun showRedditNews() {
        val intent = Intent(context, NewsActivity::class.java)
        startActivity(intent)
        activity.finish()
    }


    private fun verifyPasswordLength(password: String): Boolean {
        return !Strings.isNullOrEmpty(password) && password.length >= 6
    }

    private fun verifyIsEmail(email: String): Boolean {
        val matcher = android.util.Patterns.EMAIL_ADDRESS.matcher(email)
        return matcher.matches()
    }



    companion object {

        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}
