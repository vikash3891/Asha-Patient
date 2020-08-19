package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import com.home.asharemedy.R
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.databinding.LayoutAppointmentDetailBinding
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.layout_appointment_detail.*
import kotlinx.android.synthetic.main.topbar_layout.view.*

class ListItemDetailActivity : BaseActivity() {

    private lateinit var viewDataBinding: LayoutAppointmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_appointment_detail)

        try {
            topbar.screenName.text = "My Clinical Visit"

            checkClick()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun checkClick() {

        topbar.imageBack.setOnClickListener {
            finish()
        }
        bottomBar.layoutSettings.setOnClickListener {
            logoutAlertDialog()
        }
        bottomBar.layoutHome.setOnClickListener {
            startActivity(
                Intent(
                    this@ListItemDetailActivity,
                    DashboardActivity::class.java
                )
            )
            finish()
        }
    }

}