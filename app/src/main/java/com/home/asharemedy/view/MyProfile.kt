package com.home.asharemedy.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import com.home.asharemedy.R
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.RequestModel
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_profile_editable.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfile : BaseActivity(), AdapterView.OnItemSelectedListener {

    var isEditable = false
    lateinit var dialog: Dialog
    var isSmokingAdded = false
    var isDrinkingAdded = false
    var isExerciseAdded = false
    var habitName = ""
    var habitFrequencyValue = ""
    var habitFrequencyUnit = ""
    var habitStatus = ""
    var data: ResponseModelClasses.GetPatientProfileResponseModel? = null

    var languages = arrayOf("Smoking", "Drinking", "Exercise")

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

    override fun onResume() {
        super.onResume()
        if (isSmokingAdded) {

        }
    }

    private fun initView() {
        getPatientProfile()

    }

    private fun checkClicks() {
        communicationEdit.setOnClickListener {
            if (!isEditable) {
                isEditable = true
                communicationEdit.text = "Save"
                nationalityValue.isEnabled = true
                religionValue.isEnabled = true
                membershipValue.isEnabled = true
                addressValue.isEnabled = true
                communicationValue.isEnabled = true
                insuranceValue.isEnabled = true
                emiratesIDValue.isEnabled = true
            } else {
                communicationEdit.text = "Edit"
                isEditable = false
                nationalityValue.isEnabled = false
                religionValue.isEnabled = false
                membershipValue.isEnabled = false
                addressValue.isEnabled = false
                communicationValue.isEnabled = false
                insuranceValue.isEnabled = false
                emiratesIDValue.isEnabled = false

                validationField()
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
        habitEdit.setOnClickListener {
            showHabitDialog()
        }
    }

    private fun validationField() {
        try {
            var allValid = true
            if (userName.text!!.isEmpty()) {
                showSuccessPopup("Please enter Name")
                !allValid
                return
            } else if (phoneNumberValue.text!!.isEmpty() || phoneNumberValue.length() < 10) {
                showSuccessPopup("Please enter Mobile Number")
                !allValid
                return
            } else if (emailValue.text!!.isEmpty()) {
                showSuccessPopup(getString(R.string.error_message_enter_email))
                !allValid
                return
            } else if (!emailValue.text!!.isEmpty() && !Utils.isValidEmail(emailValue.text.toString().trim())) {
                showSuccessPopup("Please enter valid Email")
                !allValid
                return
            } else if (allValid) {
                data!!.patient_name = userName.text.toString()
                data!!.patient_mobile = phoneNumberValue.text.toString()
                data!!.patient_email = emailValue.text.toString()
                data!!.patient_address1 = addressValue.text.toString()
                data!!.patient_address2 = streetValue.text.toString()
                data!!.patient_city = cityValue.text.toString()
                // data!!.pin = pinValue.text.toString()
                updateProfileApi()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateProfileApi() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            data!!.patient_name = userName.text.toString()
            data!!.patient_dob = dobValue.text.toString()
            data!!.patient_mobile = phoneNumberValue.text.toString()
            data!!.patient_address1 = addressValue.text.toString()
            data!!.patient_address2 = communicationValue.text.toString()
            data!!.emergency_contact_number = emergencyContactValue.text.toString()
            data!!.patient_city = cityValue.text.toString()


            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.LoginResponseModel> =
                apiService.updateProfile(
                    "54",
                    Utils.getJSONRequestBody(
                        RequestModel.getUpdateProfileRequestModel(
                            this@MyProfile, data!!
                        )
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
                        if (response.code() == 400) {
                            Log.v("Error code 400", response.errorBody().toString())
                        }
                        if (response.body() != null) {
                            if (response.body()!!.message == "fail") {
                                showSuccessPopup(response.body()!!.message)
                            } else {
                                when {
                                    isSmokingAdded -> layoutSmoking.visibility = View.VISIBLE
                                    isDrinkingAdded -> layoutDrinking.visibility = View.VISIBLE
                                    else -> layoutExercise.visibility = View.VISIBLE
                                }
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
                        //dismissDialog()
                        Log.d("Response: ", response.body().toString())
                        if (response.body() != null) {

                            /*foodsList = response.body()!!.data

                            Utils.setOrderHistoryList(foodsList)

                            loadList()*/
                            getPatientHabit()
                            data = response.body()!!
                            updateView(data!!)
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
        //showDialog()
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
        dobValue.setText(data.patient_dob)
        brandPartnerName.text = data.insurance_company_name
        phoneNumberValue.setText(data.patient_mobile)
        emailValue.setText(data.patient_email)
        emergencyContactValue.setText(data.emergency_contact_number)

        nationalityValue.setText(data.patient_country)
        religionValue.setText(data.patient_country)
        membershipValue.setText(data.membership_number)
        addressValue.setText(data.patient_address1)
        communicationValue.setText(data.patient_address2)
        insuranceValue.setText(data.insurance_company_name)
        emiratesIDValue.setText(data.parent_id)
        cityValue.setText(data.patient_city)
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

    private fun showHabitDialog() {

        try {
            dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_add_habit)
            val spinnerHabit = dialog.findViewById(R.id.spinnerHabit) as Spinner
            val radioGroupHabit = dialog.findViewById(R.id.radioGroupHabit) as RadioGroup
            val habitFrequency = dialog.findViewById(R.id.habitFrequency) as EditText
            val habitYes = dialog.findViewById(R.id.habitYes) as RadioButton
            val habitNo = dialog.findViewById(R.id.habitNo) as RadioButton

            spinnerHabit.onItemSelectedListener = this

            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerHabit.adapter = aa

            radioGroupHabit.setOnCheckedChangeListener { _: RadioGroup, i: Int ->

                if (i == R.id.habitYes) {
                    habitFrequency.visibility = View.VISIBLE
                } else if (i == R.id.habitNo) {
                    habitFrequency.visibility = View.GONE
                }
            }

            val cancel = dialog.findViewById(R.id.cancel) as TextView
            val submit = dialog.findViewById(R.id.submit) as TextView

            cancel.setOnClickListener {
                dialog.dismiss()
            }

            submit.setOnClickListener {

                habitFrequencyValue = habitFrequency.text.toString()
                habitFrequencyUnit = "Per Day"

                habitStatus = if (habitYes.isChecked)
                    "active"
                else
                    "inactive"

                when {
                    spinnerHabit.selectedItem == "Smoking" -> {
                        isSmokingAdded = true
                        habitName = "Smoking"
                        layoutSmoking.visibility = View.VISIBLE
                        if (habitYes.isChecked) {
                            smokingYes.isChecked = true
                            smokingFrequency.setText(habitFrequencyValue)
                        }
                    }
                    spinnerHabit.selectedItem == "Drinking" -> {
                        isDrinkingAdded = true
                        habitName = "Drinking"
                        layoutDrinking.visibility = View.VISIBLE
                        if (habitYes.isChecked) {
                            drinkingYes.isChecked = true
                            drinkingFrequency.setText(habitFrequencyValue)
                        }
                    }
                    else -> {
                        isExerciseAdded = true
                        habitName = "Exercise"
                        layoutExercise.visibility = View.VISIBLE
                        if (habitYes.isChecked) {
                            exerciseYes.isChecked = true
                            exerciseFrequency.setText(habitFrequencyValue)
                        }
                    }
                }
                addHabitApi()
                dialog.dismiss()
            }
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    private fun addHabitApi() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.LoginResponseModel> =
                apiService.getHabit(
                    "17",
                    Utils.getJSONRequestBody(
                        RequestModel.getHabitRequestModel(
                            habitName, habitFrequencyValue, habitFrequencyUnit, habitStatus
                        )
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
                        if (response.code() == 400) {
                            Log.v("Error code 400", response.errorBody().toString())
                        }
                        if (response.body() != null) {
                            if (response.body()!!.message == "fail") {
                                showSuccessPopup(response.body()!!.message)
                            } else {
                                when {
                                    isSmokingAdded -> layoutSmoking.visibility = View.VISIBLE
                                    isDrinkingAdded -> layoutDrinking.visibility = View.VISIBLE
                                    else -> layoutExercise.visibility = View.VISIBLE
                                }
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