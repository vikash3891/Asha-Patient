package com.home.asharemedy.base

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.home.asharemedy.R
import com.home.asharemedy.utils.AppLog
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.LocaleManagerMew
import com.home.asharemedy.utils.Utils
import com.home.asharemedy.view.AppLoginActivity
import com.home.asharemedy.view.WebViewActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


abstract class BaseActivity : AppCompatActivity(), BaseFragment.Callback {

    private var mProgressDialog: ProgressDialog? = null
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleManagerMew.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window // in Activity's onCreate() for instance
            w.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }*/

        AppLog.printLog("StatusBarHeight", getStatusBarHeight().toString())

    }

    fun logoutAlertDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.app_name))
        alertDialog.setMessage("Are you sure you want to logout? ")
        alertDialog.setNeutralButton("Cancel") { _, _ ->
        }

        alertDialog.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()
            //AppPrefences.clearAll(this)

            AppPrefences.setLogin(this, false)
            val intent = Intent(this, AppLoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            //startActivity(Intent(this, AppLoginActivity::class.java))
            finish()
        }

        alertDialog.show()

    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String) {
    }


    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus

        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText &&
            !view.javaClass.name.startsWith("android.webkit.")
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.left - scrcoords[0]
            val y = ev.rawY + view.top - scrcoords[1]
            if (x < view.left || x > view.right || y < view.top || y > view.bottom)

                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    this.window.decorView.applicationWindowToken,
                    0
                )
        }
        return super.dispatchTouchEvent(ev)
    }

    fun showToast(message: String) {
        if (message != null)
            hideKeyboard()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showSuccessPopup(message: String) {
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.app_name))
        alertDialog.setMessage(message)

        alertDialog.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }

        alertDialog.show()
    }

    fun isPasswordValid(password: Editable?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%]).{8,16})"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)

        return matcher.matches()
    }

    fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    fun showDialog() {
        if (mProgressDialog == null || !mProgressDialog!!.isShowing())
            mProgressDialog = Utils.showProgressDialog1(this)
    }

    fun dismissDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
                mProgressDialog!!.cancel()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getHeader(): String {
        return ""//AppPrefences.getLoginUserInfo(this).token_type + " " + AppPrefences.getLoginUserInfo(this).access_token
    }

    fun getTime(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        println("Current Time is: $date.time")
        return inputFormat.format(date.time)
    }

    fun getDate(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        println("Current Date is: $date.date")
        return inputFormat.format(date.time)
    }

    fun startWebActivity(title: String, url: String) {
        try {
            intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("contentTitle", title)
            intent.putExtra("contentUrl", url)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}