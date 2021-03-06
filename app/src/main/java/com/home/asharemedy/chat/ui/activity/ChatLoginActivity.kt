package com.home.asharemedy.chat.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.home.asharemedy.DEFAULT_USER_PASSWORD
import com.home.asharemedy.R
import com.home.asharemedy.chat.utils.SharedPrefsHelper
import com.home.asharemedy.chat.utils.chat.ChatHelper
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import kotlinx.android.synthetic.main.activity_chat_login.*

private const val UNAUTHORIZED = 401
private const val DRAFT_LOGIN = "draft_login"
private const val DRAFT_USERNAME = "draft_username"

class ChatLoginActivity : BaseActivity() {

    private var strUser: String = ""

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, ChatLoginActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_chat_login)
            if (intent.hasExtra("Username"))
                strUser = intent.getStringExtra("Username")
            tvBtnLogin.setOnClickListener {

                showProgressDialog(R.string.dlg_login)
                prepareUser()

            }
            setValues()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setValues() {

        try {
            showProgressDialog(R.string.dlg_login)
            prepareUser()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_login, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_login_app_info -> {
                AppInfoActivity.start(this)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun clearDrafts() {
        SharedPrefsHelper.save(DRAFT_LOGIN, "")
        SharedPrefsHelper.save(DRAFT_USERNAME, "")
    }

    private fun prepareUser() {
        try {
            val qbUser = QBUser()
            qbUser.login = strUser.replace(" ", "")
            qbUser.fullName = strUser
            qbUser.password = DEFAULT_USER_PASSWORD
            signIn(qbUser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun signIn(user: QBUser) {
        try {
            showProgressDialog(R.string.dlg_login)
            ChatHelper.login(user, object : QBEntityCallback<QBUser> {
                override fun onSuccess(userFromRest: QBUser, bundle: Bundle?) {
                    if (userFromRest.fullName == user.fullName) {
                        loginToChat(user)
                    } else {
                        //Need to set password NULL, because server will update user only with NULL password
                        user.password = null
                        updateUser(user)
                    }
                }

                override fun onError(e: QBResponseException) {
                    if (e.httpStatusCode == UNAUTHORIZED) {
                        signUp(user)
                    } else {
                        hideProgressDialog()
                        showErrorSnackbar(
                            R.string.login_chat_login_error,
                            e,
                            View.OnClickListener { signIn(user) })
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateUser(user: QBUser) {
        try {
            ChatHelper.updateUser(user, object : QBEntityCallback<QBUser> {
                override fun onSuccess(qbUser: QBUser, bundle: Bundle?) {
                    loginToChat(user)
                }

                override fun onError(e: QBResponseException) {
                    hideProgressDialog()
                    showErrorSnackbar(R.string.login_chat_login_error, e, null)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loginToChat(user: QBUser) {
        try {//Need to set password, because the server will not register to chat without password
            user.password = DEFAULT_USER_PASSWORD
            ChatHelper.loginToChat(user, object : QBEntityCallback<Void> {
                override fun onSuccess(void: Void?, bundle: Bundle?) {
                    SharedPrefsHelper.saveQbUser(user)
                    if (!true) {
                        clearDrafts()
                    }
                    DialogsActivity.start(this@ChatLoginActivity)
                    finish()
                    hideProgressDialog()
                }

                override fun onError(e: QBResponseException) {
                    hideProgressDialog()
                    showErrorSnackbar(R.string.login_chat_login_error, e, null)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun signUp(user: QBUser) {
        try {
            SharedPrefsHelper.removeQbUser()
            QBUsers.signUp(user).performAsync(object : QBEntityCallback<QBUser> {
                override fun onSuccess(p0: QBUser?, p1: Bundle?) {
                    hideProgressDialog()
                    signIn(user)
                }

                override fun onError(exception: QBResponseException?) {
                    hideProgressDialog()
                    showErrorSnackbar(R.string.login_sign_up_error, exception, null)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}