package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.home.asharemedy.R
import com.home.asharemedy.adapter.DashboardGridAapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.databinding.ActivityDashboardBinding
import com.home.asharemedy.model.DashboardGridModel
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.item_profile_details.view.*
import kotlinx.android.synthetic.main.nav_header_home.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var adapter: DashboardGridAapter? = null
    var foodsList = ArrayList<DashboardGridModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        try {
            initView()
            checkClicks()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
//        topbar.screenName.text = getString(R.string.dashboard)
//        topbar.imageBack.visibility = View.GONE

        /* setSupportActionBar(toolbar);
         val toggle = ActionBarDrawerToggle(
             this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
         )
         drawer.addDrawerListener(toggle)
         toggle.syncState()*/
        setNavigationalDrawer()
        setupToolDrawer()
        getPatientProfile()

        foodsList.add(
            DashboardGridModel(
                getString(R.string.my_health_record),
                R.drawable.ic_health_record
            )
        )
        foodsList.add(
            DashboardGridModel(
                getString(R.string.appointments),
                R.drawable.ic_appointment
            )
        )
        foodsList.add(DashboardGridModel(getString(R.string._247_doctor), R.drawable.ic_call))
        foodsList.add(
            DashboardGridModel(
                getString(R.string.payment_history),
                R.drawable.ic_records
            )
        )

        adapter = DashboardGridAapter(this, foodsList)

        gvDashboard.adapter = adapter
    }

    private fun setNavigationalDrawer() {


        val navigationView = findViewById<NavigationView>(R.id.navView)
        //val headerView = navigationView.getHeaderView(0)

//        val layout = headerView.findViewById<RelativeLayout>(R.id.nav_header_layout)

    }

    private fun checkClicks() {
        try {
            profileDetails.viewProfile.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, MyProfile::class.java))
            }
            bottomBar.layoutSettings.setOnClickListener {
                // logoutAlertDialog()
                drawerLayout.openDrawer(GravityCompat.START)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPatientProfile() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetPatientProfileResponseModel> =
                apiService.getPatientProfile(AppPrefences.getUserID(this))
            call.enqueue(object : Callback<ResponseModelClasses.GetPatientProfileResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetPatientProfileResponseModel>,
                    response: Response<ResponseModelClasses.GetPatientProfileResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response: ", response.body().toString())
                        if (response.body() != null) {
                            updateView(response.body()!!)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetPatientProfileResponseModel>,
                    t: Throwable
                ) {
                    Log.d("Throws:", t.message.toString())
                    dismissDialog()
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
            dismissDialog()
        }

    } else {
        dismissDialog()
        showToast(getString(R.string.internet))
    }

    fun updateView(data: ResponseModelClasses.GetPatientProfileResponseModel) {
        profileName.text = data.patient_name
        profileDetails.userName.text = data.patient_name
        profileDetails.patientID.text = data.patient_id
        profileDetails.dobValue.text = data.patient_dob
        profileDetails.brandPartnerName.text = data.insurance_company_name
        profileDetails.phoneNumberValue.text = data.patient_mobile
        profileDetails.emailValue.text = data.patient_email
        profileDetails.emergencyContactValue.text = data.emergency_contact_number

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
            header!!.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

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