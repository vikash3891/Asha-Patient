package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.home.asharemedy.R
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.RequestModel
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_add_medication.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMedicationActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("SelectedItem", medicationStatus[position])
    }

    private var appointment_id = "0"
    private var patient_id = ""
    private var medication_type = ""
    private var drug_name = ""
    private var dosage_instructions = ""
    private var days = ""
    private var dose_per_day = ""
    private var other_instruction = ""
    private var status = ""

    private var medicationStatus = arrayOf("Active", "Inactive")
    private var prescribedByArray = arrayOf("Prescribed off Platform", "Prescribed on Platform")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medication)

        initView()
        checkClick()
    }

    fun initView() {
        try {
            topbar.screenName.text = getString(R.string.add_medication)

            statusValue.onItemSelectedListener = this
            prescribedBy.onItemSelectedListener = this

            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, medicationStatus)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            statusValue.adapter = aa

            val prescribedByAdapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, prescribedByArray)
            prescribedByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            prescribedBy.adapter = prescribedByAdapter

            prescribedForName.filters = arrayOf<InputFilter>(Utils.InputFilterMinMax(1, 30))
            dosePerDayName.filters = arrayOf<InputFilter>(Utils.InputFilterMinMax(1, 10))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkClick() {
        try {
            topbar.imageBack.setOnClickListener {
                finish()
            }
            saveMedicationDetail.setOnClickListener {

                validationField()
            }

            bottomBar.layoutSettings.setOnClickListener {
                logoutAlertDialog()
            }
            bottomBar.layoutHome.setOnClickListener {
                startActivity(
                    Intent(
                        this@AddMedicationActivity,
                        DashboardActivity::class.java
                    )
                )
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun validationField() {
        try {
            var allValid = true
            when {
                medicineName.text!!.isEmpty() -> {
                    showSuccessPopup("Please enter Medicine Name")
                    !allValid
                    return
                }
                prescribedForName.text!!.isEmpty() -> {
                    showSuccessPopup("Please enter days of Prescription")
                    !allValid
                    return
                }
                dosePerDayName.text!!.isEmpty() -> {
                    showSuccessPopup("Please enter Doses details")
                    !allValid
                    return
                }
                allValid -> {
                    patient_id = AppPrefences.getUserID(this)
                    medication_type = prescribedBy.selectedItem.toString()
                    drug_name = medicineName.text.toString()
                    dosage_instructions = instructionsName.text.isNullOrEmpty().toString()
                    days = prescribedForName.text.toString()
                    dose_per_day = dosePerDayName.text.toString()
                    other_instruction = instructionsName.text.isNullOrEmpty().toString()
                    status = statusValue.selectedItem.toString()

                    updateMedication()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateMedication() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            /* Log.d(
                 "ReqData:",
                 "$selectedVitalName $vital_date $vital_reading $vital_unit"
             )*/
            val call: Call<ResponseModelClasses.SetVitalResponseModel> =
                apiService.setMedication(
                    AppPrefences.getUserID(this),
                    Utils.getJSONRequestBodyAny(
                        RequestModel.getAddMedicationRequestModel(
                            appointment_id,
                            patient_id,
                            medication_type,
                            drug_name,
                            dosage_instructions,
                            days,
                            dose_per_day,
                            other_instruction,
                            status
                        )
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.SetVitalResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.SetVitalResponseModel>,
                    response: Response<ResponseModelClasses.SetVitalResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response:", response.body().toString())
                        if (response.code() == 400) {
                            Log.v("Error code 400", response.errorBody().toString())
                        }
                        if (response.body() != null) {
                            showSuccessPopup("Vital Saved Successfully.")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.SetVitalResponseModel>,
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