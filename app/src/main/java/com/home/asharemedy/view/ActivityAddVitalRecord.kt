package com.home.asharemedy.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.home.asharemedy.R
import com.home.asharemedy.adapter.PaymentListAdapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.RequestModel
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_add_vital_record.*
import kotlinx.android.synthetic.main.activity_clinic_visit.bottomBar
import kotlinx.android.synthetic.main.activity_clinic_visit.topbar
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityAddVitalRecord : BaseActivity() {

    var adapter: PaymentListAdapter? = null
    var selectedVitalIndex = -1
    var selectedVitalName = ""

    var vital_date = ""
    var vital_reading = ""
    var vital_unit = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vital_record)

        initView()
        checkClick()
        selectedVitalIndex = 0
    }

    fun initView() {
        try {
            topbar.screenName.text = getString(R.string.add_vital)
            topbar.imageBack.setOnClickListener {
                finish()
            }
            vitalRecordDate.text = Utils.getDate()
            vitalRecordTime.text = Utils.getTime()

            vital_unit = "C"

            bpLayout.visibility = View.GONE
            heightWeightLayout.visibility = View.VISIBLE

            selectedVitalName = "temperature"
            vitalName.text = getString(R.string.temperature)
            heightWeightLabel.text = getString(R.string.temperature)
            heightWeightValue.hint = getString(R.string.celsius)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkClick() {
        try {
            topbar.imageBack.setOnClickListener {
                finish()
            }
            saveVitalRecord.setOnClickListener {

                vital_date = vitalRecordDate.text.toString()
                vital_reading = if (selectedVitalName.equals(getString(R.string.blood_pressure))) {
                    arrayOf(
                        bpSystolic.text.toString() + "/120",
                        bpDaistolic.text.toString() + "/140"
                    ).toString()
                } else {
                    heightWeightValue.text.toString()
                }
                if (vital_reading.isNotEmpty())
                    updateVital()
                else
                    showSuccessPopup("Please enter Vital Reading.")
            }

            vitalName.setOnClickListener() {
                showDialogAilment()
            }
            bottomBar.layoutSettings.setOnClickListener {
                logoutAlertDialog()
            }
            bottomBar.layoutHome.setOnClickListener {
                startActivity(
                    Intent(
                        this@ActivityAddVitalRecord,
                        DashboardActivity::class.java
                    )
                )
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showDialogAilment() {
        try {
            lateinit var dialog: AlertDialog

            val builder = AlertDialog.Builder(this)

            builder.setTitle("Select Vital")

            builder.setSingleChoiceItems(Utils.arrayColors, selectedVitalIndex) { _, which ->

                selectedVitalIndex = which
                vitalName.text = Utils.arrayColors[which]
                selectedVitalName = Utils.arrayColors[which]
                updateInputView(selectedVitalName)

                dialog.dismiss()
            }

            dialog = builder.create()
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateInputView(vitalName: String) {

        when (vitalName) {
            getString(R.string.temperature) -> {

                vital_unit = "C"
                selectedVitalName = getString(R.string.temperature)
                bpLayout.visibility = View.GONE
                heightWeightLayout.visibility = View.VISIBLE

                heightWeightLabel.text = getString(R.string.temperature)
                heightWeightValue.hint = getString(R.string.celsius)
            }
            getString(R.string.blood_pressure) -> {
                vital_unit = getString(R.string.blood_sugar_unit)
                selectedVitalName = "bloodpressure"
                bpLayout.visibility = View.VISIBLE
                heightWeightLayout.visibility = View.GONE

                heightWeightLabel.text = getString(R.string.blood_pressure)
                heightWeightValue.hint = getString(R.string.blood_sugar_unit)
            }
            getString(R.string.pulse) -> {
                vital_unit = getString(R.string.pr_min)

                selectedVitalName = getString(R.string.pulse)
                bpLayout.visibility = View.GONE
                heightWeightLayout.visibility = View.VISIBLE

                heightWeightLabel.text = getString(R.string.pulse)
                heightWeightValue.hint = getString(R.string.pr_min)
            }
            getString(R.string.height) -> {
                vital_unit = "ft.inch"

                selectedVitalName = getString(R.string.height)
                bpLayout.visibility = View.GONE
                heightWeightLayout.visibility = View.VISIBLE

                heightWeightLabel.text = getString(R.string.height)
                heightWeightValue.hint = getString(R.string.height_in_ft_inchs)

            }
            getString(R.string.weight) -> {
                vital_unit = "kg"

                selectedVitalName = getString(R.string.weight)
                bpLayout.visibility = View.GONE
                heightWeightLayout.visibility = View.VISIBLE

                heightWeightLabel.text = getString(R.string.weight)
                heightWeightValue.hint = getString(R.string.weight_in_kg)

            }
            getString(R.string.respiration_rate) -> {

                vital_unit = getString(R.string.pr_min)

                selectedVitalName = "respiration_rate"
                bpLayout.visibility = View.GONE
                heightWeightLayout.visibility = View.VISIBLE

                heightWeightLabel.text = getString(R.string.respiration_rate)
                heightWeightValue.hint = getString(R.string.pr_min)

            }
            getString(R.string.calories_burned) -> {

                vital_unit = getString(R.string.calories_burned_unit)

                selectedVitalName = "calories_burned"
                bpLayout.visibility = View.GONE
                heightWeightLayout.visibility = View.VISIBLE

                heightWeightLabel.text = getString(R.string.calories_burned)
                heightWeightValue.hint = getString(R.string.calories_burned_unit)

            }
            getString(R.string.blood_sugar) -> {

                vital_unit = getString(R.string.blood_sugar_unit)

                selectedVitalName = "bloodsugar"
                bpLayout.visibility = View.GONE
                heightWeightLayout.visibility = View.VISIBLE

                heightWeightLabel.text = getString(R.string.blood_sugar)
                heightWeightValue.hint = getString(R.string.blood_sugar_unit)

            }
            getString(R.string.oxygen_saturation) -> {

                vital_unit = getString(R.string.oxygen_saturation_unit)

                selectedVitalName = "oxygen_saturation"
                bpLayout.visibility = View.GONE
                heightWeightLayout.visibility = View.VISIBLE

                heightWeightLabel.text = getString(R.string.oxygen_saturation)
                heightWeightValue.hint = getString(R.string.oxygen_saturation_unit)

            }
        }
    }

    private fun updateVital() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            Log.d(
                "ReqData:",
                "$selectedVitalName $vital_date $vital_reading $vital_unit"
            )
            val call: Call<ResponseModelClasses.SetVitalResponseModel> =
                apiService.getSingleUnitVital(
                    AppPrefences.getUserID(this), selectedVitalName,
                    Utils.getJSONRequestBodyAny(
                        RequestModel.setVitalRequestModel(
                            vital_date, vital_reading, vital_unit
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
                            var alertDialog = AlertDialog.Builder(this@ActivityAddVitalRecord)
                            alertDialog.setTitle(getString(R.string.app_name))
                            alertDialog.setMessage("Vital Saved Successfully.")

                            alertDialog.setPositiveButton("OK") { dialog, which ->
                                dialog.dismiss()
                                finish()
                            }

                            alertDialog.show()
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