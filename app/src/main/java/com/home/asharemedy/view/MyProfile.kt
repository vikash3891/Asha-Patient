package com.home.asharemedy.view

import android.app.AlertDialog
import android.content.Intent
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
import com.home.asharemedy.databinding.AcitivityProfileBinding
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_profile_editable.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfile : BaseActivity() {

    private lateinit var viewDataBinding: AcitivityProfileBinding
    var isEditable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_editable)

        try {
            initView()
            checkClicks()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
        getPatientProfile()

    }

    private fun checkClicks() {
        edit.setOnClickListener {
            if (!isEditable) {
                isEditable = true
                edit.text = "Save"
                nationalityValue.isEnabled = true
                religionValue.isEnabled = true
                membershipValue.isEnabled = true
                addressValue.isEnabled = true
                communicationValue.isEnabled = true
                insuranceValue.isEnabled = true
                emiratesIDValue.isEnabled = true
            } else {
                edit.text = "Edit"
                isEditable = false
                nationalityValue.isEnabled = false
                religionValue.isEnabled = false
                membershipValue.isEnabled = false
                addressValue.isEnabled = false
                communicationValue.isEnabled = false
                insuranceValue.isEnabled = false
                emiratesIDValue.isEnabled = false
            }
        }

        radioGroupSmoking.setOnCheckedChangeListener { _: RadioGroup, i: Int ->

            if (i == R.id.smokingYes) {
                smokingFrequency.visibility = View.VISIBLE
            } else if (i == R.id.smokingNo) {
                smokingFrequency.visibility = View.GONE
            }
        }
        radioGroupDrinking.setOnCheckedChangeListener { _: RadioGroup, i: Int ->

            if (i == R.id.drinkingYes) {
                drinkingFrequency.visibility = View.VISIBLE
            } else if (i == R.id.drinkingNo) {
                drinkingFrequency.visibility = View.GONE
            }
        }
        radioGroupExercise.setOnCheckedChangeListener { _: RadioGroup, i: Int ->

            if (i == R.id.exerciseYes) {
                exerciseFrequency.visibility = View.VISIBLE
            } else if (i == R.id.exerciseNo) {
                exerciseFrequency.visibility = View.GONE
            }
        }

        bottomBar.layoutSettings.setOnClickListener {
            logoutAlertDialog()
        }
        bottomBar.layoutHome.setOnClickListener {
            startActivity(
                Intent(
                    this@MyProfile,
                    DashboardActivity::class.java
                )
            )
            finish()
        }

        topBarLayout.imageBack.setOnClickListener {
            finish()
        }
    }

    /*private fun validationField() {
        try {
            var allValid = true
            if (name.text!!.isEmpty()) {
                showSuccessPopup("Please enter Name")
                !allValid
                return
            } else if (phoneNumber.text!!.isEmpty() || phoneNumber.length() < 10) {
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
            } else if (allValid) {
                item.name = name.text.toString()
                item.mobile = phoneNumber.text.toString()
                item.email = editEmail.text.toString()
                item.address = streetSocietyOffice.text.toString()
                item.landmark = landmarkValue.text.toString()
                item.state = stateValue.text.toString()
                item.city = cityValue.text.toString()
                item.pin = pinValue.text.toString()
                updateUserProfile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/

    /*private fun updateUserProfile() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.RegistrationResponse> =
                apiService.updateProfile(
                    Utils.getUserDetails().get(0).id.orEmpty(),
                    RequestModel.getUpdateProfileRequestModel(
                        Utils.getUserDetails().get(0).password.orEmpty(), item
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
                            if (response.body()!!.status == "fail") {
                                showSuccessPopup(response.body()!!.message)
                            } else {

                                var alertDialog = AlertDialog.Builder(this@MyProfile)
                                alertDialog.setTitle(getString(R.string.app_name))
                                alertDialog.setMessage("Profile Updated Successfully")

                                alertDialog.setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                    Utils.setUserDetails(item)
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
    }*/

    private fun getPatientProfile() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetPatientProfileResponseModel> =
                apiService.getPatientProfile("15")//AppPrefences.getUserID(this))
            call.enqueue(object : Callback<ResponseModelClasses.GetPatientProfileResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetPatientProfileResponseModel>,
                    response: Response<ResponseModelClasses.GetPatientProfileResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response: ", response.body().toString())
                        if (response.body() != null) {

                            /*foodsList = response.body()!!.data

                            Utils.setOrderHistoryList(foodsList)

                            loadList()*/
                            getPatientHabit()
                            updateView(response.body()!!)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetPatientProfileResponseModel>,
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

    private fun getPatientHabit() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ArrayList<ResponseModelClasses.GetHabitResponseModel>> =
                apiService.getPatientHabits("17")//AppPrefences.getUserID(this))
            call.enqueue(object : Callback<ArrayList<ResponseModelClasses.GetHabitResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.GetHabitResponseModel>>,
                    response: Response<ArrayList<ResponseModelClasses.GetHabitResponseModel>>
                ) {
                    try {
                        dismissDialog()
                        Log.d("HabitResponse: ", response.body().toString())
                        if (response.body() != null) {

                            updateHabitView(response.body()!!)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.GetHabitResponseModel>>,
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

    fun updateView(data: ResponseModelClasses.GetPatientProfileResponseModel) {
        userName.text = data.patient_name
        patientID.text = data.patient_id
        dobValue.text = data.birth_date
        brandPartnerName.text = data.insurance_company_name
        phoneNumberValue.text = data.patient_mobile
        emailValue.text = data.patient_email
        emergencyContactValue.text = data.emergency_contact_number

        nationalityValue.setText(data.patient_country)
        religionValue.setText(data.patient_country)
        membershipValue.setText(data.membership_number)
        addressValue.setText(data.patient_address1)
        communicationValue.setText(data.patient_address2)
        insuranceValue.setText(data.insurance_company_name)
        emiratesIDValue.setText(data.parent_id)
    }

    fun updateHabitView(data: ArrayList<ResponseModelClasses.GetHabitResponseModel>) {

        for (i in 0 until data.size) {
            if (data[i].patient_habit_id.equals("4")) {
                if (data[i].status.equals("active")) {
                    smokingYes.isChecked = true
                    smokingNo.isChecked = false
                    smokingFrequency.visibility = View.VISIBLE
                    smokingFrequency.setText(data[i].habit_frequency)
                    smokingFrequency.hint = data[i].habit_frequency_unit
                } else {
                    smokingYes.isChecked = false
                    smokingNo.isChecked = true
                    smokingFrequency.visibility = View.GONE
                }
            } else if (data[i].patient_habit_id.equals("5")) {
                if (data[i].status.equals("active")) {
                    exerciseYes.isChecked = true
                    exerciseNo.isChecked = false
                    exerciseFrequency.visibility = View.VISIBLE
                    exerciseFrequency.setText(data[i].habit_frequency)
                    exerciseFrequency.hint = data[i].habit_frequency_unit
                } else {
                    exerciseYes.isChecked = false
                    exerciseNo.isChecked = true
                    exerciseFrequency.visibility = View.GONE
                }
            } else if (data[i].patient_habit_id.equals("4")) {

            }
        }
    }
}