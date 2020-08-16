package com.home.asharemedy.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import com.home.asharemedy.R
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.RequestModel
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_forgot_password_first.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordFirstActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_first)

        try {
            txtCABtitle.text = getString(R.string.forgot_password)
            imgCABback.setOnClickListener {
                finish()
            }

            btnSubmitStep1.setOnClickListener {
                if (editEmailF1.text!!.isNotEmpty() && Utils.isValidEmail(editEmailF1.text.toString())) {
                    //setForgotPassword()
                    var alertDialog =
                        AlertDialog.Builder(this@ForgotPasswordFirstActivity)
                    alertDialog.setTitle(getString(R.string.app_name))
                    alertDialog.setMessage("Password Reset Link provided.")

                    alertDialog.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }

                    alertDialog.setPositiveButton(getString(R.string.reset_password)) { dialog, _ ->
                        startWebActivity(
                            getString(R.string.reset_password),
                            Constants.PRIVACY_POLICY
                        )
                    }

                    alertDialog.show()
                } else
                    showSuccessPopup("Please enter valid Email")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setForgotPassword() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.RegistrationResponse> =
                apiService.forgotPassword(
                    Utils.getJSONRequestBodyAny(
                        RequestModel.getForgotPasswordRequestModel(
                            editEmailF1.text.toString()
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

                                var alertDialog =
                                    AlertDialog.Builder(this@ForgotPasswordFirstActivity)
                                alertDialog.setTitle(getString(R.string.app_name))
                                alertDialog.setMessage("Password Reset Link provided.")

                                alertDialog.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                                    dialog.dismiss()
                                    finish()
                                }

                                alertDialog.setPositiveButton(getString(R.string.reset_password)) { dialog, _ ->
                                    startWebActivity(
                                        getString(R.string.reset_password),
                                        Constants.PRIVACY_POLICY
                                    )
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
