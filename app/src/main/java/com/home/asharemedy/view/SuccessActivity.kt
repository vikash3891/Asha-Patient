package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.home.asharemedy.R
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.RequestModel
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_success.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SuccessActivity : BaseActivity() {

    var transanction_id = "wnjkd28337udhwed"
    var amount = ""
    var status = ""
    var cgst_percentage = "5"
    var sgst_percentage = "10"
    var igst_percentage = "15"
    var discount_percentage = "0"
    var convenience_fee = "50"
    var payer_type = "patient"
    var payment_date = ""
    var payment_id = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
        checkOnClick()
        setPaymentDetailsStepOne()
    }

    private fun checkOnClick() {

        close.setOnClickListener {
            startActivity(
                Intent(
                    this@SuccessActivity,
                    DashboardActivity::class.java
                )
            )
            finish()
        }

    }

    private fun setPaymentDetailsStepOne() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            payment_date = Utils.getDate()
            var setPaymentRequestOne =
                ResponseModelClasses.GetPaymentHistoryResponseModel.TableData(
                    Utils.selectedDoctorFacility!!.fees,
                    cgst_percentage,
                    convenience_fee,
                    discount_percentage,
                    Utils.selectedDoctorFacility!!.fees,
                    igst_percentage,
                    AppPrefences.getUserID(this),
                    payer_type,
                    payment_date,
                    payment_id,
                    Utils.selectedDoctorFacility!!.doctor_id,
                    if (Utils.isDoctor) "doctor" else "facility",
                    sgst_percentage,
                    "successful",
                    transanction_id
                )
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.SetPaymentStepOneResponseModel> =
                apiService.setPaymentStepOne(
                    Utils.getJSONRequestBodyAny(
                        RequestModel.getPaymentStep1RequestModel(
                            setPaymentRequestOne
                        )
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.SetPaymentStepOneResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.SetPaymentStepOneResponseModel>,
                    response: Response<ResponseModelClasses.SetPaymentStepOneResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response: ", response.body().toString())
                        if (response.body() != null) {
                            if (response.body()!!.message == "0") {
                                showSuccessPopup(response.body()!!.message)
                            } else {
                                payment_id = response.body()!!.data.payment_id
                                Log.d(
                                    "payment_id:",
                                    response.body()!!.data.payment_id + ":" + payment_id
                                )
                                setPaymentDetailsStepTwo()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.SetPaymentStepOneResponseModel>,
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
        showToast(getString(R.string.internet))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(
            Intent(
                this@SuccessActivity,
                DashboardActivity::class.java
            )
        )
        finish()
    }

    private fun setPaymentDetailsStepTwo() = if (Utils.isConnected(this)) {
        showDialog()
        try {

            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.RegistrationResponse> =
                apiService.setPaymentStepTwo(
                    Utils.getJSONRequestBodyAny(
                        RequestModel.getPaymentStepTwoRequestModel(
                            AppPrefences.getUserID(this),
                            Utils.selectedGridList[0].slot_id,
                            payment_id,
                            "scheduled"
                        )
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.RegistrationResponse> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.RegistrationResponse>,
                    response: Response<ResponseModelClasses.RegistrationResponse>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response: ", response.body().toString())
                        if (response.body() != null) {
                            if (response.body()!!.message == "0") {
                                showSuccessPopup(response.body()!!.message)
                            } else {
                                successLayout.visibility = View.VISIBLE
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.RegistrationResponse>,
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
        showToast(getString(R.string.internet))
    }

}