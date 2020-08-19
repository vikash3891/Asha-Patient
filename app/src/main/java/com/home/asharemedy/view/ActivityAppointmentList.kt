package com.home.asharemedy.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.asharemedy.R
import com.home.asharemedy.adapter.ListItemAdapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_clinic_visit.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivityAppointmentList : BaseActivity() {

    var adapter: ListItemAdapter? = null
    var appointmentList = ArrayList<ResponseModelClasses.GetMyAppointmentsResponseModel>()
    var cDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic_visit)
        initView()
        checkOnClick()

    }

    private fun initView() {
        topbar.screenName.text = getString(R.string.appointments)

        floatingActionButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        getAppointmentList()
    }

    private fun checkOnClick() {

        topbar.imageBack.setOnClickListener {
            finish()
        }

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddAppointmentListActivity::class.java))
        }

        startDate.setOnClickListener {
            openCalendar(1)
        }
        endDate.setOnClickListener {
            openCalendar(2)
        }

        bottomBar.layoutSettings.setOnClickListener {
            logoutAlertDialog()
        }
        bottomBar.layoutHome.setOnClickListener {
            startActivity(
                Intent(
                    this@ActivityAppointmentList,
                    DashboardActivity::class.java
                )
            )
            finish()
        }

    }

    private fun openCalendar(dateID: Int) {
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

                    if (dateID == 1) {
                        startDate.text =
                            "" + dayOfMonth + " " + Utils.setMonth(monthOfYear + 1) + ", " + year
                    } else {
                        endDate.text =
                            "" + dayOfMonth + " " + Utils.setMonth(monthOfYear + 1) + ", " + year
                    }

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

    private fun getAppointmentList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ArrayList<ResponseModelClasses.GetMyAppointmentsResponseModel>> =
                apiService.getMyAppointmentList()//"7")//AppPrefences.getUserID(this))
            call.enqueue(object :
                Callback<ArrayList<ResponseModelClasses.GetMyAppointmentsResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.GetMyAppointmentsResponseModel>>,
                    response: Response<ArrayList<ResponseModelClasses.GetMyAppointmentsResponseModel>>
                ) {
                    try {
                        dismissDialog()
                        Log.d("MyAppointmentResponse: ", response.body().toString())

                        if (response.body() != null) {
                            appointmentList.clear()
                            appointmentList = response.body()!!

                            loadList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.GetMyAppointmentsResponseModel>>,
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

    private fun loadList() {
        try {
            adapter = ListItemAdapter(this, appointmentList)

            listRecyc.apply {

                layoutManager = LinearLayoutManager(this@ActivityAppointmentList)

                adapter =
                    ListItemAdapter(this@ActivityAppointmentList, appointmentList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}