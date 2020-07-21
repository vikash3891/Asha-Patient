package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.asharemedy.R
import com.home.asharemedy.adapter.ListItemAdapter
import com.home.asharemedy.model.ListItemModel
import kotlinx.android.synthetic.main.activity_clinic_visit.*
import kotlinx.android.synthetic.main.topbar_layout.view.*

class ListActivity : AppCompatActivity() {

    var adapter: ListItemAdapter? = null
    var foodsList = ArrayList<ListItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic_visit)

        topbar.screenName.text = getString(R.string.app_name)
        topbar.imageBack.setOnClickListener {
            finish()
        }

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddAppointmentListActivity::class.java))
        }
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

}