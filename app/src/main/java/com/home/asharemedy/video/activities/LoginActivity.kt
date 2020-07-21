package com.home.asharemedy.video.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.home.asharemedy.DEFAULT_USER_PASSWORD
import com.home.asharemedy.R
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.home.asharemedy.video.services.LoginService
import com.home.asharemedy.video.util.signInUser
import com.home.asharemedy.video.util.signUp
import com.home.asharemedy.video.utils.*
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import kotlinx.android.synthetic.main.activity_video_login.*

const val ERROR_LOGIN_ALREADY_TAKEN_HTTP_STATUS = 422

class LoginActivity : BaseActivity() {

    private lateinit var userLoginEditText: EditText
    private lateinit var userFullNameEditText: EditText

    private lateinit var user: QBUser

    companion object {
        fun start(context: Context) = context.startActivity(Intent(context, LoginActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_login)
        initUI()
    }

    private fun initUI() {
        supportActionBar?.title = "Login"
        userLoginEditText = findViewById(R.id.userLoginEditText)
        userLoginEditText.addTextChangedListener(LoginEditTextWatcher(userLoginEditText))

        userFullNameEditText = findViewById(R.id.userFullNameEditText)
        userFullNameEditText.addTextChangedListener(LoginEditTextWatcher(userFullNameEditText))

        btnLogin.setOnClickListener(){
            if (isEnteredUserNameValid() && isEnteredRoomNameValid()) {
                hideKeyboard()
                val user = createUserWithEnteredData()
                signUpNewUser(user)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_login, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_login_user_done -> {
                if (isEnteredUserNameValid() && isEnteredRoomNameValid()) {
                    hideKeyboard()
                    val user = createUserWithEnteredData()
                    signUpNewUser(user)
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isEnteredUserNameValid(): Boolean {
        return isLoginValid(this, userLoginEditText)
    }

    private fun isEnteredRoomNameValid(): Boolean {
        return isFoolNameValid(this, userFullNameEditText)
    }

    private fun hideKeyboard() {
        hideKeyboard(userLoginEditText)
        hideKeyboard(userFullNameEditText)
    }

    private fun signUpNewUser(newUser: QBUser) {
        showProgressDialog(R.string.dlg_creating_new_user)
        signUp(newUser, object : QBEntityCallback<QBUser> {
            override fun onSuccess(result: QBUser, params: Bundle) {
                SharedPrefsHelper.saveQbUser(newUser)
                loginToChat(result)
            }

            override fun onError(e: QBResponseException) {
                if (e.httpStatusCode == ERROR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                    signInCreatedUser(newUser)
                } else {
                    hideProgressDialog()
                    longToast(R.string.sign_up_error)
                }
            }
        })
    }

    private fun loginToChat(qbUser: QBUser) {
        qbUser.password =  DEFAULT_USER_PASSWORD
        user = qbUser
        startLoginService(qbUser)
    }

    private fun createUserWithEnteredData(): QBUser {
        val qbUser = QBUser()
        val userLogin = userLoginEditText.text.toString()
        val userFullName = userFullNameEditText.text.toString()
        qbUser.login = userLogin
        qbUser.fullName = userFullName
        qbUser.password = DEFAULT_USER_PASSWORD
        return qbUser
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == EXTRA_LOGIN_RESULT_CODE) {
            hideProgressDialog()

            var isLoginSuccess = false
            data?.let {
                isLoginSuccess = it.getBooleanExtra(EXTRA_LOGIN_RESULT, false)
            }

            var errorMessage = getString(R.string.unknown_error)
            data?.let {
                errorMessage = it.getStringExtra(EXTRA_LOGIN_ERROR_MESSAGE)
            }

            if (isLoginSuccess) {
                SharedPrefsHelper.saveQbUser(user)
                signInCreatedUser(user)
            } else {
                longToast(getString(R.string.login_chat_login_error) + errorMessage)
                userLoginEditText.setText(user.login)
                userFullNameEditText.setText(user.fullName)
            }
        }
    }

    private fun signInCreatedUser(user: QBUser) {
        signInUser(user, object : QBEntityCallback<QBUser> {
            override fun onSuccess(result: QBUser, params: Bundle) {
                SharedPrefsHelper.saveQbUser(user)
                updateUserOnServer(user)
            }

            override fun onError(responseException: QBResponseException) {
                hideProgressDialog()
                longToast(R.string.sign_in_error)
            }
        })
    }

    private fun updateUserOnServer(user: QBUser) {
        user.password = null
        QBUsers.updateUser(user).performAsync(object : QBEntityCallback<QBUser> {
            override fun onSuccess(updUser: QBUser?, params: Bundle?) {
                hideProgressDialog()
                OpponentsActivity.start(this@LoginActivity)
                finish()
            }

            override fun onError(responseException: QBResponseException?) {
                hideProgressDialog()
                longToast(R.string.update_user_error)
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }

    private fun startLoginService(qbUser: QBUser) {
        val tempIntent = Intent(this, LoginService::class.java)
        val pendingIntent = createPendingResult(EXTRA_LOGIN_RESULT_CODE, tempIntent, 0)
        LoginService.start(this, qbUser, pendingIntent)
    }

    private inner class LoginEditTextWatcher internal constructor(private val editText: EditText) : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            editText.error = null
        }

        override fun afterTextChanged(s: Editable) {

        }
    }
}