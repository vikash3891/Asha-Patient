package com.home.asharemedy.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.asharemedy.R
import com.home.asharemedy.adapter.AppointmentItemAdapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.databinding.ActivityAddAppointmentListBinding
import com.home.asharemedy.model.ListItemModel
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_add_appointment_list.*
import kotlinx.android.synthetic.main.activity_add_appointment_list.topbar
import kotlinx.android.synthetic.main.activity_appointment_slots.*
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
    var doctorFacilityList = ArrayList<ListItemModel>()
    var isDoctor = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_appointment_list)

        initView()
        checkClicks()
    }

    private fun initView() {
        try {
            getAlimentList()
            getServicesList()
            var status = ""
            var time = ""
            for (i in 0..10) {
                if (i % 2 == 0) {
                    status = "Scheduled"
                    time = "01:00 AM"
                } else if (i % 3 == 0) {
                    status = "Pending"
                    time = "08:00 AM"
                } else {
                    status = "Confirmed"
                    time = "10:10 AM"
                }
                doctorFacilityList.add(
                    ListItemModel(
                        getString(R.string._03_june_2020),
                        time,
                        getString(R.string.american_hospital),
                        getString(R.string.dr_anna_johnson),
                        getString(R.string.ent),
                        getString(R.string.frequent_vomiting),
                        getString(R.string.test_remarks),
                        getString(R.string.perennail_allergy),
                        getString(R.string.none),
                        getString(R.string.record_pdf),
                        status
                    )
                )
            }
            adapter = AppointmentItemAdapter(this, doctorFacilityList)

            addAppointmentRecyc.apply {

                layoutManager = LinearLayoutManager(this@AddAppointmentListActivity)

                adapter =
                    AppointmentItemAdapter(this@AddAppointmentListActivity, doctorFacilityList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showDialogAliment() {
        try {
            lateinit var dialog: AlertDialog
            val arrayColors = arrayOf("EYE", "NOSE", "EAR", "MUSCLE", "BONE", "SKIN", "STOMACH")
            val arrayChecked = booleanArrayOf(false, false, false, false, false, false, false)
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Choose Aliment")

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
                        var str = "${alimentSelectedValues.text} ${arrayColors[i]},"
                        alimentSelectedValues.text = str.dropLast(1)
                    }
                }
            }

            dialog = builder.create()
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showDialogService() {
        try {
            lateinit var dialog: AlertDialog
            val arrayColors = arrayOf("OPD", "Neurology", "X-Ray")
            val arrayChecked = booleanArrayOf(false, false, false)
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Choose Aliment")

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
                        var str = "${alimentSelectedValues.text} ${arrayColors[i]},"
                        alimentSelectedValues.text = str.dropLast(1)
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
                    R.drawable.rounded_corner_blue
                )
            )
            appointmentForDoctor.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

            appointmentForInstitution.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_round_blue_stroke
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
                    R.drawable.drawable_round_blue_stroke
                )
            )
            appointmentForFacility.setTextColor(ContextCompat.getColor(this, R.color.colorAppGray))
            isDoctor = true
            searchByLabel.text = "Search By Aliment: "
        }

        if (selectedView == appointmentForInstitution) {
            appointmentForDoctor.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_round_blue_stroke
                )
            )
            appointmentForDoctor.setTextColor(ContextCompat.getColor(this, R.color.colorAppGray))

            appointmentForInstitution.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.rounded_corner_blue
                )
            )
            appointmentForInstitution.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

            appointmentForFacility.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_round_blue_stroke
                )
            )
            appointmentForFacility.setTextColor(ContextCompat.getColor(this, R.color.colorAppGray))
        }

        if (selectedView == appointmentForFacility) {
            appointmentForDoctor.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_round_blue_stroke
                )
            )
            appointmentForDoctor.setTextColor(ContextCompat.getColor(this, R.color.colorAppGray))

            appointmentForInstitution.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_round_blue_stroke
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
                    R.drawable.rounded_corner_blue
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

            alimentSelectedValues.setOnClickListener {
                if (isDoctor)
                    showDialogAliment()
                else
                    showDialogService()
            }

            appointmentForDoctor.setOnClickListener() {
                changeSlotButtonBackgroundSlot(appointmentForDoctor)
            }
            appointmentForInstitution.setOnClickListener() {
                changeSlotButtonBackgroundSlot(appointmentForInstitution)
            }
            appointmentForFacility.setOnClickListener() {
                changeSlotButtonBackgroundSlot(appointmentForFacility)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getAlimentList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetAilmentResponseModel> =
                apiService.getAlimentList()//AppPrefences.getUserID(this))
            call.enqueue(object : Callback<ResponseModelClasses.GetAilmentResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetAilmentResponseModel>,
                    response: Response<ResponseModelClasses.GetAilmentResponseModel>
                ) {
                    try {
                        dismissDialog()

                        if (response.body() != null) {
                            //showDialogAliment()
                            /*doctorFacilityList = response.body()!!.data
                            Utils.setOrderHistoryList(doctorFacilityList)
                            loadList()*/

                            updateView(response.body()!!)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetAilmentResponseModel>,
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
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetAilmentResponseModel> =
                apiService.getServicesList()//AppPrefences.getUserID(this))
            call.enqueue(object : Callback<ResponseModelClasses.GetAilmentResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetAilmentResponseModel>,
                    response: Response<ResponseModelClasses.GetAilmentResponseModel>
                ) {
                    try {
                        dismissDialog()

                        if (response.body() != null) {
                            //showDialogAliment()
                            /*doctorFacilityList = response.body()!!.data
                            Utils.setOrderHistoryList(doctorFacilityList)
                            loadList()*/

                            updateView(response.body()!!)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetAilmentResponseModel>,
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
            val call: Call<ResponseModelClasses.GetAilmentResponseModel> =
                apiService.getFacilitiesList()//AppPrefences.getUserID(this))
            call.enqueue(object : Callback<ResponseModelClasses.GetAilmentResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetAilmentResponseModel>,
                    response: Response<ResponseModelClasses.GetAilmentResponseModel>
                ) {
                    try {
                        dismissDialog()

                        if (response.body() != null) {
                            //showDialogAliment()
                            /*doctorFacilityList = response.body()!!.data
                            Utils.setOrderHistoryList(doctorFacilityList)
                            loadList()*/

                            updateView(response.body()!!)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetAilmentResponseModel>,
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

}