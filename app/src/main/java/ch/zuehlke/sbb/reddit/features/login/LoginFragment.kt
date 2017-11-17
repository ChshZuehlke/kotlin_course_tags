package ch.zuehlke.sbb.reddit.features.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.features.overview.OverviewActivity
import com.google.common.base.Preconditions.checkNotNull
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

/**
 * Created by chsc on 08.11.17.
 */

class LoginFragment : Fragment(), LoginContract.View {

    private var mPresenter: LoginContract.Presenter? = null
    private var mProgessBar: ProgressBar? = null
    private var mLoginButton: AppCompatButton? = null
    private var mUsername: TextInputEditText? = null
    private var mPassword: TextInputEditText? = null
    private val mDisposable: CompositeDisposable = CompositeDisposable()

    override fun onResume() {
        super.onResume()
        mPresenter!!.start()
    }

    override fun onPause() {
        super.onPause()
        mDisposable.dispose()
    }

    private fun textChanges(view: EditText) = Observable.create(ObservableOnSubscribe<String> { e ->
        val callback: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                e.onNext(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
        view.addTextChangedListener(callback)
        Log.d(TAG, "Text changed listener ${callback.hashCode()} to view ${view.id} added")
        e.setCancellable {
            view.removeTextChangedListener(callback)
            Log.d(TAG, "Text changed listener ${callback.hashCode()} from view ${view.id} removed")
        }
    })

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.fragment_login, container, false)
        mProgessBar = root.findViewById<ProgressBar>(R.id.progressBar)
        mLoginButton = root.findViewById<AppCompatButton>(R.id.loginButton)
        mUsername = root.findViewById<TextInputEditText>(R.id.username)
        mPassword = root.findViewById<TextInputEditText>(R.id.password)

        mLoginButton!!.setOnClickListener { mPresenter!!.login(mUsername!!.text.toString(), mPassword!!.text.toString()) }

        mPresenter?.let { presenter ->
            val userValidationResults = textChanges(mUsername!!).debounce(500, TimeUnit.MILLISECONDS).map(presenter::validateUserName)
            val passwordValidationResults = textChanges(mPassword!!).debounce(500, TimeUnit.MILLISECONDS).map { presenter.validatePassword(it) }

            mDisposable.addAll(
                    userValidationResults.observeOn(AndroidSchedulers.mainThread()).subscribe(this::showUsernameResult),
                    passwordValidationResults.observeOn(AndroidSchedulers.mainThread()).subscribe(this::showPasswordResult)
            )

        }
        return root

    }

    override fun setPresenter(presenter: LoginContract.Presenter) {
        this.mPresenter = checkNotNull(presenter)
    }

    override val isActive: Boolean
        get() = isAdded

    override fun setLoadingIndicator(isActive: Boolean) {
        mProgessBar!!.visibility = if (isActive) View.VISIBLE else View.GONE
    }

    fun showTextViewValidationResult(view: EditText, result: ValidationResult) {
        when (result) {
            is Ok -> view.error = null
            is Failed -> {
                view.error = result.message
            }
        }
    }

    override fun showUsernameResult(result: ValidationResult) {
        showTextViewValidationResult(mUsername!!, result)
    }

    override fun showPasswordResult(result: ValidationResult) {
        showTextViewValidationResult(mPassword!!, result)
    }

    override fun showRedditNews() {
        val intent = Intent(context, OverviewActivity::class.java)
        startActivity(intent)
        activity.finish()
    }


    companion object {
        private const val TAG = "LoginFragment"

        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}
