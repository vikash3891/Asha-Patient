package com.home.asharemedy.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
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

class RegistrationActivity : BaseActivity() {

    var name = ""
    var DOB = ""
    var mobile = ""
    var email = ""
    var address = ""
    var street = ""
    var password = ""
    var cDate = ""
    var city = ""
    var state = ""
    var country = ""
    var refCode = ""
    var gender = ""
    var pincode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        try {
            initView()
            clickPerform()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {

        txtCABtitle.text = getString(R.string.registration_title_new)
        //getCountryList(false, txtUtilityName)
        gender = "Male"
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
                /*if (AilmentArrayData.getCount() > 0) {
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

            imgCABback.setOnClickListener {
                finish()
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
            } else if (editAddress.text!!.isEmpty()) {
                showSuccessPopup("Please enter Address")
                !allValid
                return
            } else if (editStreet.text!!.isEmpty()) {
                showSuccessPopup("Please enter Street")
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
            } else if (editPincode.text!!.isEmpty()) {
                showSuccessPopup("Please enter Pincode")
                !allValid
                return
            } else if (editPassword.text!!.isEmpty()) {// || !isPasswordValid(editPassword.text)
                showSuccessPopup(getString(R.string.password_validation_message))
                !allValid
                return
            } else if (editCPassword.text!!.isEmpty()) {// || !isPasswordValid(editCPassword.text)
                showSuccessPopup("Please enter Confirm Password")
                !allValid
                return
            } else if (editCPassword!!.text!!.isNotEmpty() && editPassword!!.text!!.isNotEmpty() &&
                editPassword.text.toString() != editCPassword.text.toString()
            ) {
                showSuccessPopup("Password doesn't match")
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
            name = editName.text!!.toString().trim()
            DOB = cDate//editDOB.text!!.toString()
            mobile = editMobileNumber.text!!.toString().trim()
            email = editEmail.text!!.toString().trim()
            address = editAddress.text!!.toString().trim()
            street = editStreet.text!!.toString().trim()
            password = editPassword.text!!.toString().trim()
            city = editCity.text!!.toString().trim()
            state = editState.text!!.toString().trim()
            country = txtUtilityName.text!!.toString().trim()
            refCode = editReferralCode.text!!.toString().trim()
            pincode = editPincode.text!!.toString().trim()

            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.RegistrationResponse> =
                apiService.registerUser(
                    Utils.getJSONRequestBody(
                        RequestModel.getRegistrationRequestModel(
                            name,
                            DOB,
                            gender,
                            email,
                            mobile,
                            password,
                            refCode,
                            city,
                            state,
                            country,
                            address,
                            street,
                            pincode
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

    private fun openCalendar(selectedView: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                try {
                    val date = Date(year - 1900, monthOfYear, dayOfMonth)
                    val formatter = SimpleDateFormat("dd-MM-yyyy")
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