package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.home.asharemedy.R
import com.home.asharemedy.adapter.DashboardGridAapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.databinding.ActivityDashboardBinding
import com.home.asharemedy.model.DashboardGridModel
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import com.home.asharemedy.base.BaseActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.item_profile_details.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : BaseActivity() {

    var adapter: DashboardGridAapter? = null
    var foodsList = ArrayList<DashboardGridModel>()
    private lateinit var viewDataBinding: ActivityDashboardBinding

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

    fun initView() {
        topbar.screenName.text = getString(R.string.dashboard)
        topbar.imageBack.visibility = View.GONE

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
        foodsList.add(DashboardGridModel(getString(R.string.talk_to_thb), R.drawable.ic_dialer_sip))
        foodsList.add(DashboardGridModel(getString(R.string.health_info), R.drawable.ic_timeline))
        foodsList.add(DashboardGridModel(getString(R.string.more_thb), R.drawable.ic_more_thb))
        adapter = DashboardGridAapter(this, foodsList)

        gvDashboard.adapter = adapter
    }

    fun checkClicks() {
        try {
            profileDetails.viewProfile.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, MyProfile::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getProductList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.ProductListResponseModel> =
                apiService.getAllProduct()
            call.enqueue(object : Callback<ResponseModelClasses.ProductListResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.ProductListResponseModel>,
                    response: Response<ResponseModelClasses.ProductListResponseModel>
                ) {
                    try {
                        dismissDialog()

                        if (response.body() != null) {
                            if (response.body()!!.status == "0") {
                                //showSuccessPopup(response.body()!!.message)
                            } else {
                                Utils.foodsList = response.body()!!.data

                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.ProductListResponseModel>,
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

}