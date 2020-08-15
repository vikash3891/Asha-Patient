package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.asharemedy.R
import com.home.asharemedy.adapter.PaymentListAdapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.model.PaymentItemModel
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_clinic_visit.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityPaymentHistory : BaseActivity() {

    var adapter: PaymentListAdapter? = null
    var foodsList = ArrayList<ResponseModelClasses.GetPaymentHistoryResponseModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_list)

        initView()
        checkClick()
    }

    fun initView() {
        try {
            topbar.screenName.text = getString(R.string.payment_history)
            topbar.imageBack.setOnClickListener {
                finish()
            }
            getPaymentHistoryList()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkClick() {
        try {
            topbar.imageBack.setOnClickListener {
                finish()
            }
            bottomBar.layoutSettings.setOnClickListener {
                logoutAlertDialog()
            }
            bottomBar.layoutHome.setOnClickListener {
                startActivity(
                    Intent(
                        this@ActivityPaymentHistory,
                        DashboardActivity::class.java
                    )
                )
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPaymentHistoryList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ArrayList<ResponseModelClasses.GetPaymentHistoryResponseModel>> =
                apiService.getPaymentHistoryList()
            call.enqueue(object :
                Callback<ArrayList<ResponseModelClasses.GetPaymentHistoryResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.GetPaymentHistoryResponseModel>>,
                    response: Response<ArrayList<ResponseModelClasses.GetPaymentHistoryResponseModel>>
                ) {
                    try {
                        dismissDialog()
                        Log.d("PayHisRes: ", response.body().toString())

                        if (response.body() != null) {
                            foodsList.clear()
                            foodsList = response.body()!!

                            loadList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.GetPaymentHistoryResponseModel>>,
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

    private fun loadList() {
        adapter = PaymentListAdapter(this, foodsList)

        listRecyc.apply {

            layoutManager = LinearLayoutManager(this@ActivityPaymentHistory)

            adapter =
                PaymentListAdapter(this@ActivityPaymentHistory, foodsList)
        }
    }

}