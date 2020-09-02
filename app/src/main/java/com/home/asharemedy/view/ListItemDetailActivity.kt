package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.home.asharemedy.R
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.databinding.LayoutAppointmentDetailBinding
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.layout_appointment_detail.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*

class ListItemDetailActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

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

        try {
            setupToolDrawer()
            appointmentStatusValue.text =
                Utils.selectedAppointmentDetails[0].appointment_info.status
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkClick() {

        topbar.imageBack.setOnClickListener {
            finish()
        }
        bottomBar.layoutSettings.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        try {
            Log.e("item.itemId", item.itemId.toString())
            when (item.itemId) {

                R.id.logout -> {
                    logoutAlertDialog()
                }

                R.id.navProfile -> {
                    startActivity(Intent(this, MyProfile::class.java))
                }

                R.id.navChangePassword -> {
                    startWebActivity(getString(R.string.privacy_policy), Constants.FAQ)
                }

                R.id.navPayment -> {
                    startActivity(Intent(this, ActivityPaymentHistory::class.java))
                }

                R.id.navFaq -> {
                    startWebActivity(getString(R.string.privacy_policy), Constants.FAQ)
                }

                R.id.navTerms -> {
                    startWebActivity(
                        getString(R.string.terms_and_conditions),
                        Constants.TERMS_AND_CONDITION
                    )
                }

                R.id.navPrivacy -> {
                    startWebActivity(getString(R.string.privacy_policy), Constants.PRIVACY_POLICY)
                }

            }
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false

        }
    }

    private fun setupToolDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            headerLayout!!.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        headerLayout!!.title.text = getString(R.string.appointments)

        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer_icon)
        toggle.setToolbarNavigationClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

}