package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.home.asharemedy.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

        Handler().postDelayed(Runnable {

            startActivity(Intent(this, AppLoginActivity::class.java))
            finish()
            /*if (AppPrefences.getLogin(this)!! && AppPrefences.getRememberMe(this)!!) {
                startActivity(Intent(this, AshaRemedyApp::class.java))
                finish()
            } else {
//                AppPrefences.clearAll(this)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }*/
        }, 2500)
    }


}