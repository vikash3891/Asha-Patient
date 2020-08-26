package com.home.asharemedy.video.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.home.asharemedy.DEFAULT_USER_PASSWORD
import com.home.asharemedy.R
import com.home.asharemedy.video.services.LoginService
import com.home.asharemedy.video.util.signInUser
import com.home.asharemedy.video.util.signUp
import com.home.asharemedy.video.utils.*
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import kotlinx.android.synthetic.main.activity_video_login.*

const val ERROR_LOGIN_ALREADY_TAKEN_HTTP_STATUS = 422

class VideoLoginActivity : BaseActivity() {

    private lateinit var user: QBUser
    private var strUser: String = ""

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, VideoLoginActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_video_login)
            if (intent.hasExtra("Username"))
                strUser = intent.getStringExtra("Username")
            initUI()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initUI() {
        try {
            supportActionBar?.title = "Login"

            val user = createUserWithEnteredData()
            signUpNewUser(user)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_login, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_login_user_done -> {

                val user = createUserWithEnteredData()
                signUpNewUser(user)

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signUpNewUser(newUser: QBUser) {
        try {
            showProgressDialog(R.string.dlg_loading_dialogs_users)
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loginToChat(qbUser: QBUser) {
        try {
            qbUser.password = DEFAULT_USER_PASSWORD
            user = qbUser
            startLoginService(qbUser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createUserWithEnteredData(): QBUser {
        val qbUser = QBUser()
        try {
            val userLogin = strUser.replace(" ", "")
            val userFullName = strUser
            qbUser.login = userLogin
            qbUser.fullName = userFullName
            qbUser.password = DEFAULT_USER_PASSWORD
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return qbUser
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun signInCreatedUser(user: QBUser) {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateUserOnServer(user: QBUser) {
        try {
            user.password = null
            QBUsers.updateUser(user).performAsync(object : QBEntityCallback<QBUser> {
                override fun onSuccess(updUser: QBUser?, params: Bundle?) {
                    hideProgressDialog()
                    OpponentsActivity.start(this@VideoLoginActivity)
                    finish()
                }

                override fun onError(responseException: QBResponseException?) {
                    hideProgressDialog()
                    longToast(R.string.update_user_error)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun startLoginService(qbUser: QBUser) {
        try {
            val tempIntent = Intent(this, LoginService::class.java)
            val pendingIntent = createPendingResult(EXTRA_LOGIN_RESULT_CODE, tempIntent, 0)
            LoginService.start(this, qbUser, pendingIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}