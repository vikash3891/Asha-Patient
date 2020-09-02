package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.home.asharemedy.R
import com.home.asharemedy.adapter.DashboardGridAapter
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.databinding.ActivityHealthRecordDashboardBinding
import com.home.asharemedy.model.DashboardGridModel
import com.home.asharemedy.utils.Constants
import kotlinx.android.synthetic.main.activity_health_record_dashboard.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*

class HealthRecordActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var adapter: DashboardGridAapter? = null
    var foodsList = ArrayList<DashboardGridModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_record_dashboard)
        initView()
        checkClick()
    }

    private fun initView() {

        try {
            setupToolDrawer()
            topbar.screenName.text = getString(R.string.my_health_record)
            topbar.imageBack.setOnClickListener {
                finish()
            }
            // load foods
            foodsList.add(
                DashboardGridModel(
                    getString(R.string.my_vitals),
                    R.drawable.ic_vitals
                )
            )
            foodsList.add(
                DashboardGridModel(
                    getString(R.string.my_medications),
                    R.drawable.ic_medication
                )
            )
            foodsList.add(
                DashboardGridModel(
                    getString(R.string.my_records),
                    R.drawable.ic_records
                )
            )
            foodsList.add(
                DashboardGridModel(
                    getString(R.string.my_care_plan),
                    R.drawable.ic_care_plan
                )
            )

            adapter = DashboardGridAapter(this, foodsList)

            gvDashboard.adapter = adapter
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
                    this@HealthRecordActivity,
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

        headerLayout!!.title.text = getString(R.string.my_health_record)

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