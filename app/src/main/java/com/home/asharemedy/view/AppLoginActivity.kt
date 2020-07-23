package com.home.asharemedy.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.RequestModel
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.LocaleManagerMew
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AppLoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        try {
            clickPerform()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun validationFields() {
        try {
            var isValid = true

            if (editUserName.text!!.isEmpty() && editUserPass.text!!.isEmpty()) {
                showSuccessPopup("Please enter Email/Password")
                !isValid
                return
            } else if (editUserName.text!!.isEmpty()) {
                showSuccessPopup("Please enter valid Email")
                !isValid
                return
            } else if (editUserPass.text!!.isEmpty()) {
                showSuccessPopup("Password cannot be empty")
                !isValid
                return
            } else if (editUserPass.text!!.isEmpty()) {
                showSuccessPopup(getString(R.string.password_validation_message))
                !isValid
                return
            } else if (isValid) {
                loginApi(false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clickPerform() {
        try {
            txtRegister.setOnClickListener {
                startActivity(Intent(this@AppLoginActivity, RegistrationActivity::class.java))
            }

            txtForgotPassword.setOnClickListener {
                //startActivity(Intent(this@LoginActivity, FilterActivity::class.java))
            }

            btnLogin.setOnClickListener {
                //validationFields()

                startActivity(Intent(this@AppLoginActivity, DashboardActivity::class.java))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loginApi(isChecked: Boolean) = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.LoginResponseModel> =
                apiService.getAdminLogin(
                    RequestModel.getLoginRequestModel(
                        editUserName.text.toString(),
                        editUserPass.text.toString()
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.LoginResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.LoginResponseModel>,
                    response: Response<ResponseModelClasses.LoginResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response:", response.body().toString())
                        if (response.body() != null) {
                            if (response.body()!!.status == "fail") {
                                showSuccessPopup(response.body()!!.message)
                            } else {

                                AppPrefences.setLogin(this@AppLoginActivity, true)
                                AppPrefences.setRememberMe(this@AppLoginActivity, true)
                                AppPrefences.setUserName(
                                    this@AppLoginActivity,
                                    response.body()!!.data[0].name!!
                                )
                                AppPrefences.setUserID(
                                    this@AppLoginActivity,
                                    response.body()!!.data[0].id!!
                                )

                                Utils.setUserDetails(response.body()!!.data[0])

                                AppPrefences.setPassword(
                                    this@AppLoginActivity,
                                    editUserPass.text.toString()
                                )
                                startActivity(
                                    Intent(
                                        this@AppLoginActivity,
                                        DashboardActivity::class.java
                                    )
                                )

                                finish()
                                //showSuccessPopup(response.body()!!.message)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.LoginResponseModel>,
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