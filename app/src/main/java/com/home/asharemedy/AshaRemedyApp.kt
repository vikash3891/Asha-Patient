package com.home.asharemedy

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import com.crashlytics.android.Crashlytics
import com.home.asharemedy.utils.LocaleManagerMew
import com.quickblox.auth.session.QBSettings
import com.home.asharemedy.chat.managers.BackgroundListener
import com.home.asharemedy.chat.utils.ActivityLifecycle
import com.home.asharemedy.video.db.DbHelper
import io.fabric.sdk.android.Fabric

const val USER_DEFAULT_PASSWORD = "quickblox"
const val DEFAULT_USER_PASSWORD = "quickblox"
const val CHAT_PORT = 5223
const val SOCKET_TIMEOUT = 300
const val KEEP_ALIVE: Boolean = true
const val USE_TLS: Boolean = true
const val AUTO_JOIN: Boolean = false
const val AUTO_MARK_DELIVERED: Boolean = true
const val RECONNECTION_ALLOWED: Boolean = true
const val ALLOW_LISTEN_NETWORK: Boolean = true

//App credentials
private const val APPLICATION_ID = "84731"
private const val AUTH_KEY = "2j9QFQH7FPNsj3j"
private const val AUTH_SECRET = "5ZWuKF7wAF-SvpH"
private const val ACCOUNT_KEY = "ep4mHaWmU6wUAbZhsokC"

//Chat credentials range
private const val MAX_PORT_VALUE = 65535
private const val MIN_PORT_VALUE = 1000
private const val MIN_SOCKET_TIMEOUT = 300
private const val MAX_SOCKET_TIMEOUT = 60000

class AshaRemedyApp : Application() {
    private lateinit var dbHelper: DbHelper
    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleManagerMew.setLocale(base))
        //MultiDex.install(base)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleManagerMew.setLocale(this)
        //Log.d(MewConstants.mewLogs, "onConfigurationChanged: " + newConfig.locale.getLanguage())
    }

    companion object {
        private lateinit var instance: AshaRemedyApp

        fun getInstance(): AshaRemedyApp = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerActivityLifecycleCallbacks(ActivityLifecycle)
        initFabric()
        dbHelper = DbHelper(this)
        checkAppCredentials()
        checkChatSettings()
        initCredentials()
        ProcessLifecycleOwner.get().lifecycle.addObserver(BackgroundListener())
    }

    private fun checkAppCredentials() {
        if (APPLICATION_ID.isEmpty() || AUTH_KEY.isEmpty() || AUTH_SECRET.isEmpty() || ACCOUNT_KEY.isEmpty()) {
            throw AssertionError(getString(R.string.error_qb_credentials_empty))
        }
    }

    private fun checkChatSettings() {
        if (USER_DEFAULT_PASSWORD.isEmpty() || CHAT_PORT !in MIN_PORT_VALUE..MAX_PORT_VALUE
            || SOCKET_TIMEOUT !in MIN_SOCKET_TIMEOUT..MAX_SOCKET_TIMEOUT) {
            throw AssertionError(getString(R.string.error_chat_credentails_empty))
        }
    }

    private fun initCredentials() {
        QBSettings.getInstance().init(applicationContext, APPLICATION_ID, AUTH_KEY, AUTH_SECRET)
        QBSettings.getInstance().accountKey = ACCOUNT_KEY

        // Uncomment and put your Api and Chat servers endpoints if you want to point the sample
        // against your own server.
        //
        // QBSettings.getInstance().setEndpoints("https://your_api_endpoint.com", "your_chat_endpoint", ServiceZone.PRODUCTION);
        // QBSettings.getInstance().zone = ServiceZone.PRODUCTION
    }

    private fun initFabric() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }
    }

    @Synchronized
    fun getDbHelper(): DbHelper {
        return dbHelper
    }
}
