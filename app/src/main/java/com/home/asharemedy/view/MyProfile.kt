package com.home.asharemedy.view

import android.app.AlertDialog
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
        getPatientHabit()
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

                        if (response.body() != null) {

                            /*foodsList = response.body()!!.data

                            Utils.setOrderHistoryList(foodsList)

                            loadList()*/
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
            val call: Call<ResponseModelClasses.GetHabitResponseModel> =
                apiService.getPatientHabits("15")//AppPrefences.getUserID(this))
            call.enqueue(object : Callback<ResponseModelClasses.GetHabitResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetHabitResponseModel>,
                    response: Response<ResponseModelClasses.GetHabitResponseModel>
                ) {
                    try {
                        dismissDialog()

                        if (response.body() != null) {

                            /*foodsList = response.body()!!.data

                            Utils.setOrderHistoryList(foodsList)

                            loadList()*/
                            updateHabitView(response.body()!!)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetHabitResponseModel>,
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

    fun updateHabitView(data: ResponseModelClasses.GetHabitResponseModel) {
        /*userName.text = data.patient_name
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
        emiratesIDValue.setText(data.parent_id)*/
    }
}