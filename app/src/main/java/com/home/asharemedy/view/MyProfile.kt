package com.home.asharemedy.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.home.asharemedy.R
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.RequestModel
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.chat.utils.getFilePath
import com.home.asharemedy.test.Adapter
import com.home.asharemedy.test.SwipeHelper
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import com.home.asharemedy.utils.Utils.profileData
import kotlinx.android.synthetic.main.activity_profile_editable.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MyProfile : BaseActivity(), AdapterView.OnItemSelectedListener,
    NavigationView.OnNavigationItemSelectedListener {

    var isEditable = false
    lateinit var dialog: Dialog

    var habitNameValue = ""
    var habitFrequencyValue = 0
    var habitFrequencyUnit = ""
    var habitStatus = "active"
    var patient_habit_id = ""
    var patient_id = ""
    var habitData = ArrayList<ResponseModelClasses.GetHabitResponseModel>()

    var cDate = ""
    var isPrimaryClicked = false

    private val REQUEST_CODE = 100
    private val CAMERA_REQUEST_CODE = 200

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
        try {
            setupPermissions()

            setupToolDrawer()
            getPatientProfile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkClicks() {

        try {
            captureImage.setOnClickListener {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            }
            chooseImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            communicationEdit.setOnClickListener {
                if (!isEditable) {
                    enableEditableUI()

                } else {
                    disableEditableUI()
                }
            }

            bottomBar.layoutSettings.setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
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
                showHabitDialog(-1)
            }

            dobValue.setOnClickListener {
                openCalendar(dobValue)
            }

            layoutPrimaryHealthIssues.setOnClickListener {
                if (!isPrimaryClicked) {
                    val intent = Intent(this, CheckListActivity::class.java)
                    startActivityForResult(intent, 123)
                    isPrimaryClicked = true
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun enableEditableUI() {
        try {
            isEditable = true
            communicationEdit.text = getString(R.string.save)

            /*dobValue.isEnabled = true
            phoneNumberValue.isEnabled = true
            phoneNumberValue.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_border)

            emailValue.isEnabled = true
            emailValue.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_border)*/

            emergencyContactValue.isEnabled = true
            emergencyContactValue.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_border)

            nationalityValue.isEnabled = true
            nationalityValue.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_border)

            religionValue.isEnabled = true
            religionValue.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_border)

            membershipValue.isEnabled = true
            membershipValue.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_border)

            addressValue.isEnabled = true
            addressValue.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_border)

            stateValue.isEnabled = true
            stateValue.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_border)

            cityValue.isEnabled = true
            cityValue.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_border)

            communicationValue.isEnabled = true
            communicationValue.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_border)

            insuranceValue.isEnabled = true
            insuranceValue.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_border)

            emiratesIDValue.isEnabled = true
            emiratesIDValue.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_border)

            layoutPrimaryHealthIssues.isEnabled = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun disableEditableUI() {
        try {
            isEditable = false
            communicationEdit.text = getString(R.string.menu_message_edit)

            dobValue.isEnabled = false
            phoneNumberValue.isEnabled = false
            phoneNumberValue.background = null

            emailValue.isEnabled = false
            emailValue.background = null

            emergencyContactValue.isEnabled = false
            emergencyContactValue.background = null

            nationalityValue.isEnabled = false
            nationalityValue.background = null

            religionValue.isEnabled = false
            religionValue.background = null

            membershipValue.isEnabled = false
            membershipValue.background = null

            addressValue.isEnabled = false
            addressValue.background = null

            stateValue.isEnabled = false
            stateValue.background = null

            cityValue.isEnabled = false
            cityValue.background = null

            communicationValue.isEnabled = false
            communicationValue.background = null

            insuranceValue.isEnabled = false
            insuranceValue.background = null

            emiratesIDValue.isEnabled = false
            emiratesIDValue.background = null

            layoutPrimaryHealthIssues.isEnabled = false

            validationField()
        } catch (e: Exception) {
            e.printStackTrace()
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
                updateProfileApi()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateProfileApi() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            profileData!!.patient_name = userName.text.toString()
            profileData!!.patient_dob = dobValue.text.toString()
            profileData!!.patient_mobile = phoneNumberValue.text.toString()
            profileData!!.patient_address1 = addressValue.text.toString()
            profileData!!.patient_address2 = communicationValue.text.toString()
            profileData!!.emergency_contact_number = emergencyContactValue.text.toString()
            profileData!!.patient_city = cityValue.text.toString()
            profileData!!.patient_state = stateValue.text.toString()
            profileData!!.patient_email = emailValue.text.toString()
            profileData!!.emergency_contact_number = emergencyContactValue.text.toString()
            profileData!!.primary_health_issue = primaryHealthIssuesValue.text.toString()
            //profileData!!.photo = Utils.userfileUploadBase64

            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetPatientProfileResponseModel> =
                apiService.updateProfile(
                    AppPrefences.getUserID(this),
                    Utils.getJSONRequestBodyAny(
                        RequestModel.getUpdateProfileRequestModel(
                            this@MyProfile, profileData!!
                        )
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.GetPatientProfileResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetPatientProfileResponseModel>,
                    response: Response<ResponseModelClasses.GetPatientProfileResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("UpdateProfileResponse:", response.body().toString())
                        /*if (response.code() == 400) {
                            Log.v(
                                "Profile not updated. Please try again later.",
                                response.errorBody().toString()
                            )
                        }*/
                        if (response.code() == 200) {
                            showSuccessPopup("Profile Updated Successfully")
                        }else {
                            showSuccessPopup("Profile not updated. Please try again later.")
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

    private fun getPatientProfile() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetPatientProfileResponseModel> =
                apiService.getPatientProfile(AppPrefences.getUserID(this))
            call.enqueue(object : Callback<ResponseModelClasses.GetPatientProfileResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetPatientProfileResponseModel>,
                    response: Response<ResponseModelClasses.GetPatientProfileResponseModel>
                ) {
                    try {
                        //dismissDialog()
                        Log.d("ProfileResponse: ", response.body().toString())
                        if (response.body() != null) {
                            profileData = response.body()!!
                            updateView()
                            getPatientHabit()
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

        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ArrayList<ResponseModelClasses.GetHabitResponseModel>> =
                apiService.getPatientHabits(AppPrefences.getUserID(this))
            call.enqueue(object : Callback<ArrayList<ResponseModelClasses.GetHabitResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.GetHabitResponseModel>>,
                    response: Response<ArrayList<ResponseModelClasses.GetHabitResponseModel>>
                ) {
                    try {
                        dismissDialog()
                        Log.d("HabitResponse: ", response.body().toString())
                        if (response.body() != null) {
                            habitData = response.body()!!
                            updateHabitView()
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

    fun updateView() {
        try {
            userName.text = profileData!!.patient_name
            patientID.text = profileData!!.patient_id
            dobValue.setText(profileData!!.patient_dob)
            brandPartnerName.text = profileData!!.insurance_company_name
            phoneNumberValue.setText(profileData!!.patient_mobile)
            emailValue.setText(profileData!!.patient_email)
            emergencyContactValue.setText(profileData!!.emergency_contact_number)

            nationalityValue.setText(profileData!!.patient_country)
            religionValue.setText(profileData!!.patient_country)
            membershipValue.setText(profileData!!.membership_number)
            addressValue.setText(profileData!!.patient_address1)
            communicationValue.setText(profileData!!.patient_address2)
            insuranceValue.setText(profileData!!.insurance_company_name)
            emiratesIDValue.setText(profileData!!.parent_id)
            cityValue.setText(profileData!!.patient_city)
            stateValue.setText(profileData!!.patient_state)
            Log.d("PrimaryIssues: ", profileData!!.primary_health_issue)
            primaryHealthIssuesValue.setText(profileData!!.primary_health_issue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateHabitView() {

        try {
            setUpRecyclerView()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setUpRecyclerView() {
        habitRecyclerView.adapter = Adapter(
            habitData
        )
        /* habitRecyclerView.addItemDecoration(
             DividerItemDecoration(
                 this,
                 DividerItemDecoration.VERTICAL
             )
         )*/
        habitRecyclerView.layoutManager = LinearLayoutManager(this)

        var itemTouchHelper = ItemTouchHelper(object : SwipeHelper(habitRecyclerView) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                var buttons = listOf<UnderlayButton>()
                var deleteButton = deleteButton(position)
                var editButton = editButton(position)
                for (i in 0 until habitData.size) {
                    buttons = listOf(deleteButton, editButton)
                }

                return buttons
            }
        })

        itemTouchHelper.attachToRecyclerView(habitRecyclerView)
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Delete",
            14.0f,
            R.color.colorComparePreviousYear,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    Log.d("Delete Button: ", "Delete Button Clicked")
                    val alertDialog = AlertDialog.Builder(this@MyProfile)
                    alertDialog.setTitle(getString(R.string.app_name))
                    alertDialog.setMessage("Are you sure you want to delete this Habit? ")
                    alertDialog.setNeutralButton("Cancel") { _, _ ->
                    }

                    alertDialog.setPositiveButton("Yes") { dialog, which ->
                        dialog.dismiss()
                        habitNameValue = habitData[position].habit_name
                        habitFrequencyValue = habitData[position].habit_frequency.toInt()
                        habitFrequencyUnit = habitData[position].habit_frequency_unit
                        patient_habit_id = habitData[position].patient_habit_id
                        deleteHabitApi()
                    }

                    alertDialog.show()
                }
            })
    }

    private fun editButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Edit",
            14.0f,
            R.color.colorAccent,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    Log.d("Edit Button: ", "Edit Button Clicked")
                    showHabitDialog(position)
                }
            })
    }

    private fun showHabitDialog(position: Int) {

        try {
            dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_add_habit)

            val radioGroupHabit = dialog.findViewById(R.id.radioGroupHabit) as RadioGroup
            val habitName = dialog.findViewById(R.id.habitName) as EditText
            val habitFrequency = dialog.findViewById(R.id.habitFrequency) as EditText
            val habitDaily = dialog.findViewById(R.id.habitDaily) as RadioButton
            val habitWeekly = dialog.findViewById(R.id.habitWeekly) as RadioButton
            val habitMonthly = dialog.findViewById(R.id.habitMonthly) as RadioButton

            val cancel = dialog.findViewById(R.id.cancel) as TextView
            val submit = dialog.findViewById(R.id.submit) as TextView

            if (position > -1) {
                habitName.setText(habitData[position].habit_name)
                habitFrequency.setText(habitData[position].habit_frequency)

                when {
                    habitData[position].habit_frequency_unit.equals("Per Day") -> habitDaily.isChecked =
                        true
                    habitData[position].habit_frequency_unit.equals("Per Week") -> habitWeekly.isChecked =
                        true
                    habitData[position].habit_frequency_unit.equals("Per Month") -> habitMonthly.isChecked =
                        true
                }
            }

            cancel.setOnClickListener {
                dialog.dismiss()
            }

            submit.setOnClickListener {

                try {
                    when {
                        habitName.text.toString().isEmpty() -> showSuccessPopup("Please enter Habit Name.")
                        habitFrequency.text.toString().isEmpty() -> showSuccessPopup("Please enter Habit Frequency.")
                        else -> {
                            habitNameValue = habitName.text.toString()
                            habitFrequencyValue = habitFrequency.text.toString().toInt()
                            Log.e("Frequency: ", habitFrequencyValue.toString())

                            when {
                                habitDaily.isChecked -> habitFrequencyUnit = "Per Day"
                                habitWeekly.isChecked -> habitFrequencyUnit = "Per Week"
                                habitMonthly.isChecked -> habitFrequencyUnit = "Per Month"
                            }

                            if (position > -1) {
                                patient_habit_id = habitData[position].patient_habit_id
                                patient_id = AppPrefences.getUserID(this)
                                editHabitApi()
                            } else {
                                addHabitApi()
                            }
                            dialog.dismiss()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
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

    private fun deleteHabitApi() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.LoginResponseModel> =
                apiService.deleteHabit(
                    AppPrefences.getUserID(this), patient_habit_id,
                    Utils.getJSONRequestBodyAny(
                        RequestModel.getHabitRequestModel(
                            habitNameValue, habitFrequencyValue, habitFrequencyUnit, habitStatus
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
                                //showSuccessPopup("Habit added Successfully.")
                                getPatientHabit()
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

    private fun addHabitApi() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.LoginResponseModel> =
                apiService.getHabit(
                    AppPrefences.getUserID(this),
                    Utils.getJSONRequestBodyAny(
                        RequestModel.getHabitRequestModel(
                            habitNameValue, habitFrequencyValue, habitFrequencyUnit, habitStatus
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
                                //showSuccessPopup("Habit added Successfully.")
                                getPatientHabit()
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

    private fun editHabitApi() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetHabitResponseModel> =
                apiService.editHabit(
                    AppPrefences.getUserID(this),patient_habit_id,
                    Utils.getJSONRequestBodyAny(
                        RequestModel.editHabitRequestModel(
                            habitNameValue,
                            habitFrequencyValue,
                            habitFrequencyUnit,
                            patient_habit_id,
                            patient_id,
                            habitStatus
                        )
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.GetHabitResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetHabitResponseModel>,
                    response: Response<ResponseModelClasses.GetHabitResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response:", response.body().toString())
                        if (response.code() == 400) {
                            Log.v("Error code 400", response.errorBody().toString())
                        }
                        if (response.body() != null) {
                            /*if (response.body()!!.message == "fail") {
                                showSuccessPopup(response.body()!!.message)
                            } else {*/
                            //showSuccessPopup("Habit added Successfully.")
                            getPatientHabit()
                            /*}*/
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

    private fun openCalendar(selectedView: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                try {
                    val date = Date(year - 1900, monthOfYear, dayOfMonth)
                    val formatter = SimpleDateFormat("dd-MM-yyyy")
                    cDate = formatter.format(date)

                    dobValue.text =
                        "" + dayOfMonth + " " + Utils.setMonth(monthOfYear + 1) + ", " + year

                } catch (e1: ParseException) {
                    e1.printStackTrace()
                }
            },
            year,
            month,
            day
        )
        dpd.datePicker.maxDate = System.currentTimeMillis() - 1000;
        dpd.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
                userProfileImage.setImageBitmap(resultData!!.extras?.get("data") as Bitmap)
            }
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {

                userProfileImage.setImageURI(resultData?.data) // handle chosen image

                Log.d("FilePath", getFilePath(applicationContext, resultData?.data!!))
                Log.d(
                    "FileBase64",
                    Utils.encoder(getFilePath(applicationContext, resultData.data!!)!!)
                )
                Utils.userfileUploadBase64 =
                    Utils.encoder(getFilePath(applicationContext, resultData.data!!)!!)
            }
            if (resultCode == 0 && requestCode == 123) {
                isPrimaryClicked = false
                primaryHealthIssuesValue.setText(Utils.selectedHealthIssues)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupPermissions() {
        try {
            val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission", "Permission to record denied")
                makeRequest()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            101
        )
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        try {
            Log.e("item.itemId", item.itemId.toString())
            when (item.itemId) {

                R.id.logout -> {
                    logoutAlertDialog()
                }

                R.id.navProfile -> {
                    startActivity(Intent(this, MyProfile::class.java))
                }

                R.id.navChangePassword -> {
                    startWebActivity(getString(R.string.privacy_policy), Constants.FAQ)
                }

                R.id.navPayment -> {
                    startActivity(Intent(this, ActivityPaymentHistory::class.java))
                }

                R.id.navFaq -> {
                    startWebActivity(getString(R.string.privacy_policy), Constants.FAQ)
                }

                R.id.navTerms -> {
                    startWebActivity(
                        getString(R.string.terms_and_conditions),
                        Constants.TERMS_AND_CONDITION
                    )
                }

                R.id.navPrivacy -> {
                    startWebActivity(getString(R.string.privacy_policy), Constants.PRIVACY_POLICY)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false

        }
    }

    private fun setupToolDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            header!!.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        header!!.title.text = "My Profile"

        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer_icon)
        toggle.setToolbarNavigationClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

}