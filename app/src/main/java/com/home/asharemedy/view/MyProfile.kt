package com.home.asharemedy.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.home.asharemedy.R
import com.home.asharemedy.databinding.AcitivityProfileBinding
import kotlinx.android.synthetic.main.acitivity_profile.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.item_profile_details.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*

class MyProfile : AppCompatActivity() {

    private lateinit var viewDataBinding: AcitivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_profile)

        try {
            topBarLayout.screenName.text = "My Profile"
            topBarLayout.imageBack.setOnClickListener {
                finish()
            }
            viewProfile.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}