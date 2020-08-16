package com.home.asharemedy.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.home.asharemedy.R
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.RequestModel
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
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
    var gross_total = "2500"
    var discount_percentage = "0"
    var convenience_fee = "50"
    var payer_id = ""
    var payer_type = "patient"
    var receiver_id = ""
    var receiver_type = ""


    var patient_id = ""
    var doctor_slot_id = ""
    var payment_id = ""
    var purpose = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        Handler().postDelayed(Runnable {

            startActivity(Intent(this, DashboardActivity::class.java))
            finish()

        }, 5000)
    }

    private fun setPaymentDetailsStepOne() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            var payment_id = "1"
            var setPaymentRequestOne = ResponseModelClasses.GetPaymentHistoryResponseModel(
                amount,
                cgst_percentage,
                convenience_fee,
                discount_percentage,
                gross_total,
                igst_percentage,
                payer_id,
                payer_type,
                payment_id,
                receiver_id,
                receiver_type,
                sgst_percentage,
                status,
                transanction_id

            )
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.RegistrationResponse> =
                apiService.setPaymentStepOne(
                    Utils.getJSONRequestBodyAny(
                        RequestModel.getPaymentStep1RequestModel(
                            setPaymentRequestOne
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

                                var alertDialog = AlertDialog.Builder(this@SuccessActivity)
                                alertDialog.setTitle(getString(R.string.app_name))
                                alertDialog.setMessage("User Registered Successfully")

                                alertDialog.setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                    finish()
                                }

                                alertDialog.show()
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

    private fun setPaymentDetailsStepTwo() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            /*patient_id, doctor_slot_id, payment_id,purpose*/
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.RegistrationResponse> =
                apiService.setPaymentStepOne(
                    Utils.getJSONRequestBodyAny(
                        RequestModel.getPaymentStepTwoRequestModel(
                            patient_id, doctor_slot_id, payment_id,purpose
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

                                var alertDialog = AlertDialog.Builder(this@SuccessActivity)
                                alertDialog.setTitle(getString(R.string.app_name))
                                alertDialog.setMessage("User Registered Successfully")

                                alertDialog.setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                    finish()
                                }

                                alertDialog.show()
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