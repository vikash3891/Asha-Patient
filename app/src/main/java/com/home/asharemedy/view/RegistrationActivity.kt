package com.home.asharemedy.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.home.asharemedy.R
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.RequestModel
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.widget.RadioGroup


class RegistrationActivity : BaseActivity() {

    private var utilityID = ""
    var name = ""
    var DOB = ""
    var mobile = ""
    var email = ""
    var password = ""
    var cDate = ""
    var city = ""
    var state = ""
    var country = ""
    var refCode = ""
    var gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        try {
            txtCABtitle.text = getString(R.string.registration_title_new)
            imgCABback.setOnClickListener {
                finish()
            }

            getCountryList(false, txtUtilityName)

            clickPerform()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun initView() {
        radioGroupGender.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male -> {
                    gender = "Male"
                }
                R.id.female -> {
                    gender = "Female"
                }
                R.id.other -> {
                    gender = "Other"
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (Utils.isRegisterSuccess)
            finish()
    }

    private fun clickPerform() {
        try {
            r_lyt_utility.setOnClickListener {
                /*if (UtilitiesData.getCount() > 0) {
                    openDialog(getString(R.string.select_utility), txtUtilityName)
                } else {
                    getCountryList(true, txtUtilityName)

                }*/
            }

            btnSubmitRegister.setOnClickListener {
                validationField()
            }
            editDOB.setOnClickListener {
                openCalendar(editDOB)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun validationField() {
        try {
            var allValid = true
            if (editName.text!!.isEmpty()) {
                showSuccessPopup("Please enter Name")
                !allValid
                return
            } else if (editMobileNumber.text!!.isEmpty() || editMobileNumber.length() < 10) {
                showSuccessPopup("Please enter Mobile Number")
                !allValid
                return
            } else if (editEmail.text!!.isEmpty()) {
                showSuccessPopup(getString(R.string.error_message_enter_email))
                !allValid
                return
            } else if (!editEmail.text!!.isEmpty() && !Utils.isValidEmail(editEmail.text.toString().trim())) {
                showSuccessPopup("Please enter valid Email")
                !allValid
                return
            } else if (editCity.text!!.isEmpty()) {
                showSuccessPopup("Please enter City")
                !allValid
                return
            } else if (editState.text!!.isEmpty()) {
                showSuccessPopup("Please enter State")
                !allValid
                return
            } else if (txtUtilityName.text!!.isEmpty()) {
                showSuccessPopup("Please select Country")
                !allValid
                return
            } else if (editPassword.text!!.isEmpty() || !isPasswordValid(editPassword.text)) {
                showSuccessPopup(getString(R.string.password_validation_message))
                !allValid
                return
            } else if (editCPassword.text!!.isEmpty() || !isPasswordValid(editCPassword.text)) {
                showSuccessPopup("Please enter Confirm Password")
                !allValid
                return
            } else if (editCPassword!!.text!!.isNotEmpty() && editPassword!!.text!!.isNotEmpty() &&
                editPassword.text.toString() != editCPassword.text.toString()
            ) {
                showSuccessPopup("Password doesn't match")
                !allValid
                return
            } else if (editReferralCode.text!!.isNotEmpty() && editReferralCode.length() < 8) {
                showSuccessPopup("Please enter valid Referral Code")
                !allValid
                return
            } else if (allValid) {
                registerUser()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun registerUser() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            name = editName.text!!.toString()
            DOB = editDOB.text!!.toString()
            mobile = editMobileNumber.text!!.toString()
            email = editEmail.text!!.toString()
            password = editPassword.text!!.toString()
            city = editCity.text!!.toString()
            state = editState.text!!.toString()
            country = txtUtilityName.text!!.toString()
            refCode = editReferralCode.text!!.toString()

            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.RegistrationResponse> =
                apiService.registerUser(
                    RequestModel.getRegistrationRequestModel(
                        name, DOB, gender, email,
                        mobile, password, refCode, city, state, country
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.RegistrationResponse> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.RegistrationResponse>,
                    response: Response<ResponseModelClasses.RegistrationResponse>
                ) {
                    try {
                        dismissDialog()

                        if (response.body() != null) {
                            if (response.body()!!.status == "0") {
                                showSuccessPopup(response.body()!!.message)
                            } else {

                                var alertDialog = AlertDialog.Builder(this@RegistrationActivity)
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

    private fun getCountryList(dialogOpen: Boolean = false, textView: TextView) =
        if (Utils.isConnected(this)) {
//        showDialog()
            /*try {
                val apiService =
                    ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
                val call = apiService.getCountryList(*//*ApiUrls.AuthKey*//*)
                call.enqueue(object : Callback<ResponseModelClasses.UtilityListResponseModel> {
                    override fun onResponse(
                        call: Call<ResponseModelClasses.UtilityListResponseModel>,
                        response: Response<ResponseModelClasses.UtilityListResponseModel>
                    ) {
                        dismissDialog()
                        try {
                            if (response.body() != null)
                                UtilitiesData.clearArrayList()
                            UtilitiesData.addArrayList(response.body()!!.Results.Table)
                            if (dialogOpen) {
                                openDialog(getString(R.string.select_utility), textView)
                            }
                            AppLog.printLog("UtilityList Response- ", response.body().toString())

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseModelClasses.UtilityListResponseModel>,
                        t: Throwable
                    ) {
                        try {
                            dismissDialog()
                            AppLog.printLog("Failure()- ", t.message.toString())

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
//            dismissDialog()
            }*/
        } else {
            showToast(getString(R.string.internet))
        }

    fun openCalendar(selectedView: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                try {
                    val date = Date(year - 1900, monthOfYear, dayOfMonth)
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    cDate = formatter.format(date)

                    editDOB.text =
                        "" + dayOfMonth + " " + Utils.setMonth(monthOfYear + 1) + ", " + year

                } catch (e1: ParseException) {
                    e1.printStackTrace()
                }
            },
            year,
            month,
            day
        )
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        dpd.show()
    }
}