package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import com.home.asharemedy.R
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.databinding.LayoutAppointmentDetailBinding
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.layout_appointment_detail.*
import kotlinx.android.synthetic.main.topbar_layout.view.*

class ListItemDetailActivity : BaseActivity() {

    private lateinit var viewDataBinding: LayoutAppointmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_appointment_detail)

        try {
            topbar.screenName.text = "Appointment Details"

            initView()
            checkClick()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initView() {

/*Utils.selectedAppointmentDetails[0].*/
        appointmentStatusValue.text = Utils.selectedAppointmentDetails[0].appointment_info.status
        facilityNameValue.text =
            Utils.selectedAppointmentDetails[0].appointment_provider_info.provider_name
        addressValue.text =
            Utils.selectedAppointmentDetails[0].appointment_provider_info.provider_type
        dateValue.text = Utils.selectedAppointmentDetails[0].appointment_slot_info.slot_date
        timeValue.text = Utils.selectedAppointmentDetails[0].appointment_slot_info.start_time
        clinicianValue.text =
            Utils.selectedAppointmentDetails[0].appointment_provider_info.provider_name
        specialityValue.text = Utils.selectedAppointmentDetails[0].appointment_info.status
        complaintsValue.text = Utils.selectedAppointmentDetails[0].appointment_info.purpose
        coordinatorValue.text =
            Utils.selectedAppointmentDetails[0].appointment_provider_info.provider_type
        remarksValue.text = Utils.selectedAppointmentDetails[0].appointment_info.purpose
    }

    private fun checkClick() {

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