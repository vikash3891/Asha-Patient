package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.asharemedy.R
import com.home.asharemedy.adapter.MyCarePlansListAdapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_my_records.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCarePlansActivity : BaseActivity() {

    var adapter: MyCarePlansListAdapter? = null
    var myReportList = ArrayList<ResponseModelClasses.GetMyCarePlanResponseModel>()
    var cDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_records)
        initView()
        checkOnClick()

    }

    private fun initView() {
        topbar.screenName.text = getString(R.string.my_care_plan)
        floatingActionButton.visibility = View.GONE
        floatingActionButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        getMyCarePlansList()
    }

    private fun checkOnClick() {

        topbar.imageBack.setOnClickListener {
            finish()
        }

        floatingActionButton.setOnClickListener {
            //startActivity(Intent(this, AddAppointmentListActivity::class.java))
        }

        bottomBar.layoutSettings.setOnClickListener {
            logoutAlertDialog()
        }
        bottomBar.layoutHome.setOnClickListener {
            startActivity(
                Intent(
                    this@MyCarePlansActivity,
                    DashboardActivity::class.java
                )
            )
            finish()
        }

    }

    private fun getMyCarePlansList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ArrayList<ResponseModelClasses.GetMyCarePlanResponseModel>> =
                apiService.getMyCarePlanList(AppPrefences.getUserID(this))
            call.enqueue(object :
                Callback<ArrayList<ResponseModelClasses.GetMyCarePlanResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.GetMyCarePlanResponseModel>>,
                    response: Response<ArrayList<ResponseModelClasses.GetMyCarePlanResponseModel>>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response: ", response.body().toString())
                        if (response.body() != null) {
                            myReportList = response.body()!!
                            loadList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.GetMyCarePlanResponseModel>>,
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

        adapter = MyCarePlansListAdapter(this, myReportList)

        listRecyc.apply {

            layoutManager = LinearLayoutManager(this@MyCarePlansActivity)
            adapter = MyCarePlansListAdapter(this@MyCarePlansActivity, myReportList)
        }
    }
}