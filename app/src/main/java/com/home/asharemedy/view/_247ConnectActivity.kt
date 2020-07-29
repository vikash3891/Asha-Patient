package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import com.home.asharemedy.R
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.chat.ui.activity.ChatLoginActivity
import com.home.asharemedy.video.activities.VideoLoginActivity
import kotlinx.android.synthetic.main.activity_247_connect.*

class _247ConnectActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_247_connect)

        chat.setOnClickListener {
            startActivity(Intent(this@_247ConnectActivity, ChatLoginActivity::class.java))
        }
        audioVideo.setOnClickListener {
            startActivity(Intent(this@_247ConnectActivity, VideoLoginActivity::class.java))
        }

    }

}