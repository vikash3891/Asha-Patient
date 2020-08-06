package com.home.asharemedy.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.asharemedy.R
import com.home.asharemedy.adapter.AppointmentItemAdapter
import com.home.asharemedy.adapter.UtilitiesListAdapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.databinding.ActivityAddAppointmentListBinding
import com.home.asharemedy.model.ListItemModel
import com.home.asharemedy.model.AilmentArrayData
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import com.home.asharemedy.utils.Utils.isAilmentOrService
import kotlinx.android.synthetic.main.activity_add_appointment_list.*
import kotlinx.android.synthetic.main.activity_add_appointment_list.topbar
import kotlinx.android.synthetic.main.activity_appointment_slots.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.dialog_layout.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddAppointmentListActivity : BaseActivity() {

    private lateinit var viewDataBinding: ActivityAddAppointmentListBinding
    var adapter: AppointmentItemAdapter? = null
    //var doctorFacilityList = ArrayList<ResponseModelClasses.GetFacilityListResponseModel>()
    var isDoctor = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_appointment_list)

        initView()
        checkClicks()
    }

    private fun initView() {
        try {
            getAilmentList()
            searchByLabel.text = "Search By Ailment: "
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadList() {
        adapter = AppointmentItemAdapter(this, Utils.doctorFacilityList)

        addAppointmentRecyc.apply {

            layoutManager = LinearLayoutManager(this@AddAppointmentListActivity)

            adapter =
                AppointmentItemAdapter(this@AddAppointmentListActivity, Utils.doctorFacilityList)
        }
    }

    private fun showDialogAilment() {
        try {
            lateinit var dialog: AlertDialog
            val arrayColors = arrayOf("EYE", "NOSE", "EAR", "MUSCLE", "BONE", "SKIN", "STOMACH")
            val arrayChecked = booleanArrayOf(false, false, false, false, false, false, false)
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Choose Ailment")

            builder.setMultiChoiceItems(arrayColors, arrayChecked) { _, which, isChecked ->
                arrayChecked[which] = isChecked
                val color = arrayColors[which]
            }

            builder.setPositiveButton("OK") { _, _ ->
                // Do something when click positive button
                //text_view.text = "Your preferred colors..... \n"
                for (i in 0 until arrayColors.size) {
                    val checked = arrayChecked[i]
                    if (checked) {
                        var str = "${ailmentSelectedValues.text} ${arrayColors[i]},"
                        ailmentSelectedValues.text = ""
                        ailmentSelectedValues.text = str.dropLast(1)
                    }
                }
            }

            dialog = builder.create()
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun changeSlotButtonBackgroundSlot(selectedView: View) {
        if (selectedView == appointmentForDoctor) {
            appointmentForDoctor.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.round_corner_green
                )
            )
            appointmentForDoctor.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

            appointmentForInstitution.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.edittext_border
                )
            )
            appointmentForInstitution.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorAppGray
                )
            )

            appointmentForFacility.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.edittext_border
                )
            )
            appointmentForFacility.setTextColor(ContextCompat.getColor(this, R.color.colorAppGray))
            isDoctor = true
            searchByLabel.text = "Search By Ailment: "
        }

        if (selectedView == appointmentForFacility) {
            appointmentForDoctor.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.edittext_border
                )
            )
            appointmentForDoctor.setTextColor(ContextCompat.getColor(this, R.color.colorAppGray))

            appointmentForInstitution.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.edittext_border
                )
            )
            appointmentForInstitution.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorAppGray
                )
            )

            appointmentForFacility.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.round_corner_green
                )
            )
            appointmentForFacility.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            isDoctor = false
            searchByLabel.text = "Search By Service: "
        }
    }

    private fun checkClicks() {
        try {
            topbar.imageBack.setOnClickListener {
                finish()
            }
            bottomBar.layoutSettings.setOnClickListener {
                logoutAlertDialog()
            }
            bottomBar.layoutHome.setOnClickListener {
                startActivity(
                    Intent(
                        this@AddAppointmentListActivity,
                        DashboardActivity::class.java
                    )
                )
                finish()
            }
            ailmentSelectedValues.setOnClickListener {
                if (isDoctor) {

                    openDialog("Choose Ailment", ailmentSelectedValues)
                    isAilmentOrService = true
                    //showDialogAilment()
                } else {
                    isAilmentOrService = false
                    openDialog("Choose Service", ailmentSelectedValues)
                    //showDialogService()
                }

            }

            appointmentForDoctor.setOnClickListener() {
                ailmentSelectedValues.setText("")
                ailmentSelectedValues.hint = "Choose Ailment name"
                getDoctorList()
                changeSlotButtonBackgroundSlot(appointmentForDoctor)
            }
            appointmentForInstitution.setOnClickListener() {
                changeSlotButtonBackgroundSlot(appointmentForInstitution)
            }

            appointmentForFacility.setOnClickListener() {
                ailmentSelectedValues.setText("")
                ailmentSelectedValues.hint = "Choose name of Service"
                getFacilityList()
                changeSlotButtonBackgroundSlot(appointmentForFacility)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getAilmentList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ArrayList<ResponseModelClasses.GetAilmentResponseModel>> =
                apiService.getAilmentList()//AppPrefences.getUserID(this))
            call.enqueue(object :
                Callback<ArrayList<ResponseModelClasses.GetAilmentResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.GetAilmentResponseModel>>,
                    response: Response<ArrayList<ResponseModelClasses.GetAilmentResponseModel>>
                ) {
                    try {
                        //dismissDialog()
                        Log.d("Response: ", response.body().toString())
                        if (response.body() != null) {
                            AilmentArrayData.clearArrayList()
                            AilmentArrayData.addArrayList(response.body()!!)
                            getServicesList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.GetAilmentResponseModel>>,
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

    private fun getServicesList() = if (Utils.isConnected(this)) {
        // showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ArrayList<ResponseModelClasses.GetServiceResponseModel>> =
                apiService.getServicesList()//AppPrefences.getUserID(this))
            call.enqueue(object :
                Callback<ArrayList<ResponseModelClasses.GetServiceResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.GetServiceResponseModel>>,
                    response: Response<ArrayList<ResponseModelClasses.GetServiceResponseModel>>
                ) {
                    try {
                        //dismissDialog()
                        Log.d("Response: ", response.body().toString())
                        if (response.body() != null) {
                            AilmentArrayData.clearServicesArrayList()
                            AilmentArrayData.addServicesArrayList(response.body()!!)
                        }
                        getDoctorList()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.GetServiceResponseModel>>,
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

    private fun getFacilityList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ArrayList<ResponseModelClasses.GetFacilityListResponseModel>> =
                apiService.getFacilitiesList()//AppPrefences.getUserID(this))
            call.enqueue(object :
                Callback<ArrayList<ResponseModelClasses.GetFacilityListResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.GetFacilityListResponseModel>>,
                    response: Response<ArrayList<ResponseModelClasses.GetFacilityListResponseModel>>
                ) {
                    try {
                        dismissDialog()
                        Log.d("FacResponse: ", response.body().toString())
                        if (response.body() != null) {
                            Utils.doctorFacilityList.clear()
                            Utils.doctorFacilityList = response.body()!!
                            loadList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.GetFacilityListResponseModel>>,
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

    private fun getDoctorList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ArrayList<ResponseModelClasses.GetFacilityListResponseModel>> =
                apiService.getDoctorsList()//AppPrefences.getUserID(this))
            call.enqueue(object :
                Callback<ArrayList<ResponseModelClasses.GetFacilityListResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.GetFacilityListResponseModel>>,
                    response: Response<ArrayList<ResponseModelClasses.GetFacilityListResponseModel>>
                ) {
                    try {
                        dismissDialog()
                        Log.d("DocResponse: ", response.body().toString())

                        if (response.body() != null) {
                            Utils.doctorFacilityList.clear()
                            Utils.doctorFacilityList = response.body()!!

                            loadList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.GetFacilityListResponseModel>>,
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

    fun updateView(data: ResponseModelClasses.GetAilmentResponseModel) {

    }

    private fun openDialog(title: String, textView: TextView) {
        try {
            val dialog = Dialog(this@AddAppointmentListActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.show()
            dialog.txtTitleTop.text = title

            val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            dialog.dialogRecycleView.layoutManager = layoutManager
            val itemDecor = DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
            dialog.dialogRecycleView.addItemDecoration(itemDecor)
            val mAdapter = UtilitiesListAdapter() { position ->
                if (isAilmentOrService) {
                    val data = AilmentArrayData.getArrayItem(position)
                    //utilityID = data.ailment_id.toString()
                    textView.text = data.ailment
                } else {
                    val data = AilmentArrayData.getServicesArrayItem(position)
                    //utilityID = data.ailment_id.toString()
                    textView.text = data.service
                }
                textView.setTextColor(resources.getColor(R.color.colorBlack))
                dialog.dismiss()
            }
            dialog.dialogRecycleView.adapter = mAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}