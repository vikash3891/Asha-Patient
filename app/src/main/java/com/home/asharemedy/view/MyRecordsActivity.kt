package com.home.asharemedy.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.home.asharemedy.R
import com.home.asharemedy.adapter.MyReportsListAdapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_my_records.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyRecordsActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var adapter: MyReportsListAdapter? = null
    var myReportList = ArrayList<ResponseModelClasses.GetMyRecordResponseModel.TableData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_records)

        checkOnClick()

    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        topbar.screenName.text = getString(R.string.my_records)

        floatingActionButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))

        setupToolDrawer()
        getMedicalReportsProfile()
    }

    private fun checkOnClick() {

        topbar.imageBack.setOnClickListener {
            finish()
        }

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddMedicalRecordActivity::class.java))
        }

        bottomBar.layoutSettings.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        bottomBar.layoutHome.setOnClickListener {
            startActivity(
                Intent(
                    this@MyRecordsActivity,
                    DashboardActivity::class.java
                )
            )
            finish()
        }

    }

    private fun getMedicalReportsProfile() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetMyRecordResponseModel> =
                apiService.getMyRecordsList(AppPrefences.getUserID(this))
            call.enqueue(object :
                Callback<ResponseModelClasses.GetMyRecordResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetMyRecordResponseModel>,
                    response: Response<ResponseModelClasses.GetMyRecordResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response: ", response.body().toString())
                        if (response.body() != null) {
                            myReportList = response.body()!!.data
                            loadList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetMyRecordResponseModel>,
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

    fun loadList() {

        adapter = MyReportsListAdapter(this, myReportList)

        listRecyc.apply {

            layoutManager = LinearLayoutManager(this@MyRecordsActivity)
            adapter = MyReportsListAdapter(this@MyRecordsActivity, myReportList)
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

        headerLayout!!.title.text = getString(R.string.my_records)

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