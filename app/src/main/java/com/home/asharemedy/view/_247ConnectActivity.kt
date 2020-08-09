package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import com.home.asharemedy.R
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.chat.ui.activity.ChatLoginActivity
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.video.activities.VideoLoginActivity
import kotlinx.android.synthetic.main.activity_247_connect.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*

class _247ConnectActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_247_connect)

        initView()
        checkClick()
    }

    fun initView() {
        try {
            topbar.screenName.text = "24*7 Doctor"
            topbar.imageBack.setOnClickListener {
                finish()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkClick() {
        try {
            topbar.imageBack.setOnClickListener {
                finish()
            }

            bottomBar.layoutSettings.setOnClickListener {
                logoutAlertDialog()
            }
            bottomBar.layoutHome.setOnClickListener {
                startActivity(
                    Intent(
                        this@_247ConnectActivity,
                        DashboardActivity::class.java
                    )
                )
                finish()
            }

            chat.setOnClickListener {
                val intent = Intent(this@_247ConnectActivity, ChatLoginActivity::class.java);
                intent.putExtra("Username", AppPrefences.getUserName(this))
                startActivity(intent)
            }
            audioVideo.setOnClickListener {

                val intent = Intent(this@_247ConnectActivity, VideoLoginActivity::class.java);
                intent.putExtra("Username", AppPrefences.getUserName(this))
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}