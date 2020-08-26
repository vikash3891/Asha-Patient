package com.home.asharemedy.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.asharemedy.R
import com.home.asharemedy.adapter.MyMedicationListAdapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.AppPrefences
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

class MyMedicationsActivity : BaseActivity() {

    var adapter: MyMedicationListAdapter? = null
    var medicationsList = ArrayList<ResponseModelClasses.GetMyMedicationResponseModel>()
    var cDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic_visit)
        initView()
        checkOnClick()

    }

    private fun initView() {
        topbar.screenName.text = getString(R.string.my_medications)
        layoutDates.visibility = View.GONE
        floatingActionButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        floatingActionButton.visibility = View.VISIBLE
        for (i in 0..10) {

            medicationsList.add(
                ResponseModelClasses.GetMyMedicationResponseModel(
                    "Paracetamol", "10", "2", "14", "For Fever", "1"
                )
            )
        }
        adapter = MyMedicationListAdapter(this, medicationsList)

        listRecyc.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(this@MyMedicationsActivity)
            // set the custom adapter to the RecyclerView
            adapter = MyMedicationListAdapter(this@MyMedicationsActivity, medicationsList)
        }

        //getMyMedicationsList()
    }

    private fun checkOnClick() {

        topbar.imageBack.setOnClickListener {
            finish()
        }

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddMedicationActivity::class.java))
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
                    this@MyMedicationsActivity,
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
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

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

    private fun getMyMedicationsList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<java.util.ArrayList<ResponseModelClasses.GetMyMedicationResponseModel>> =
                apiService.getMyMedicationsList(
                    AppPrefences.getUserID(this)
                )
            call.enqueue(object :
                Callback<java.util.ArrayList<ResponseModelClasses.GetMyMedicationResponseModel>> {
                override fun onResponse(
                    call: Call<java.util.ArrayList<ResponseModelClasses.GetMyMedicationResponseModel>>,
                    response: Response<java.util.ArrayList<ResponseModelClasses.GetMyMedicationResponseModel>>
                ) {
                    try {
                        dismissDialog()
                        Log.d("MedicationResponse: ", response.body().toString())
                        if (response.body() != null) {
                            medicationsList = response.body()!!
                            loadList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<java.util.ArrayList<ResponseModelClasses.GetMyMedicationResponseModel>>,
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
        adapter = MyMedicationListAdapter(this, medicationsList)

        listRecyc.apply {

            layoutManager = LinearLayoutManager(this@MyMedicationsActivity)

            adapter =
                MyMedicationListAdapter(this@MyMedicationsActivity, medicationsList)
        }
    }

}