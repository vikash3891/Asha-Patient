package com.home.asharemedy.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.home.asharemedy.R
import com.home.asharemedy.databinding.LayoutAppointmentDetailBinding
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.item_profile_details.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*

class ListItemDetailActivity : AppCompatActivity() {

    private lateinit var viewDataBinding: LayoutAppointmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_appointment_detail)

        try {
            topbar.screenName.text = "My Clinical Visit"

            profileDetails.viewProfile.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}