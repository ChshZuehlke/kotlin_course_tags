package ch.zuehlke.sbb.reddit.features.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.features.overview.OverviewActivity
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.SupportFragmentInjector
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_login.view.*
import java.util.concurrent.TimeUnit

/**
 * Created by chsc on 08.11.17.
 */

class LoginFragment : Fragment(), LoginContract.View, SupportFragmentInjector {

    override val injector: KodeinInjector = KodeinInjector()

    override fun provideOverridingModule() = createLoginModule(this@LoginFragment)

    //injected
    private val mPresenter: LoginContract.Presenter by injector.with(this@LoginFragment).instance()

    private val mDisposable: CompositeDisposable = CompositeDisposable()

    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    override fun onPause() {
        super.onPause()
        mDisposable.dispose()
    }

    private fun textChanges(view: EditText) = Observable.create(ObservableOnSubscribe<String> { emitter ->
        val callback: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                emitter.onNext(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
        view.addTextChangedListener(callback)
        Log.d(TAG, "Text changed listener ${callback.hashCode()} to view ${view.id} added")
        emitter.setCancellable {
            view.removeTextChangedListener(callback)
            Log.d(TAG, "Text changed listener ${callback.hashCode()} from view ${view.id} removed")
        }
    })

    private fun <T : View> clicks(view: T) = Observable.create(ObservableOnSubscribe<T> { emitter ->
        val callback: View.OnClickListener = View.OnClickListener { emitter.onNext(view) }
        view.setOnClickListener(callback)
        Log.d(TAG, "Click listener ${callback.hashCode()} set on view ${view.id}")
        emitter.setCancellable {
            view.setOnClickListener(null)
            Log.d(TAG, "Click listener ${callback.hashCode()} removed from view ${view.id}")
        }
    })

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        initializeInjector()

        val root = inflater!!.inflate(R.layout.fragment_login, container, false)
        val password = root.password
        val username: TextInputEditText = root.username
        val loginButton = root.loginButton

        val loginSubscription =
                clicks(loginButton)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            mPresenter.login(username.text.toString(), password.text.toString())
                        }
        val usernameValidationSubscription =
                textChanges(username)
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .map(mPresenter::validateUserName)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::showUsernameResult)
        val passwordValidationSubscription =
                textChanges(password)
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .map { mPresenter.validatePassword(it) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::showPasswordResult)
        mDisposable.addAll(loginSubscription, usernameValidationSubscription, passwordValidationSubscription)

        return root
    }

    override fun setPresenter(presenter: LoginContract.Presenter) {
        //do nothing- presenter is already injected
    }

    override val isActive: Boolean
        get() = isAdded

    override fun setLoadingIndicator(isActive: Boolean) {
        view?.progressBar?.visibility = if (isActive) View.VISIBLE else View.GONE
    }

    private fun showTextViewValidationResult(view: EditText, result: ValidationResult) {
        when (result) {
            is Ok -> view.error = null
            is Failed -> {
                view.error = result.message
            }
        }
    }

    override fun showUsernameResult(result: ValidationResult) {
        showTextViewValidationResult(view?.username!!, result)
    }

    override fun showPasswordResult(result: ValidationResult) {
        showTextViewValidationResult(view?.password!!, result)
    }

    override fun showRedditNews() {
        val intent = Intent(context, OverviewActivity::class.java)
        startActivity(intent)
        activity.finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        destroyInjector()
    }

    companion object {
        private const val TAG = "LoginFragment"

        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}
