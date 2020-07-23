package com.home.asharemedy.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.asharemedy.R
import com.home.asharemedy.adapter.ListItemAdapter
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.model.ListItemModel
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_clinic_visit.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListActivity : BaseActivity() {

    var adapter: ListItemAdapter? = null
    var foodsList = ArrayList<ListItemModel>()
    var cDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic_visit)
        initView()
        checkOnClick()

    }

    private fun initView() {
        topbar.screenName.text = getString(R.string.app_name)

        floatingActionButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))

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
            foodsList.add(
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
        adapter = ListItemAdapter(this, foodsList)

        listRecyc.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(this@ListActivity)
            // set the custom adapter to the RecyclerView
            adapter = ListItemAdapter(this@ListActivity, foodsList)
        }

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
}