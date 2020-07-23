package com.home.asharemedy.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.home.asharemedy.R
import com.home.asharemedy.databinding.ActivityHealthRecordDashboardBinding
import com.home.asharemedy.model.DashboardGridModel
import com.home.asharemedy.adapter.DashboardGridAapter
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.topbar_layout.view.*

class HealthRecordActivity : AppCompatActivity() {

    var adapter: DashboardGridAapter? = null
    var foodsList = ArrayList<DashboardGridModel>()
    private lateinit var viewDataBinding: ActivityHealthRecordDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_record_dashboard)

        /*viewDataBinding = ActivityDashboardBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProviders.of(this@RepoListFragment).get(RepoListViewModel::class.java)
            setLifecycleOwner(viewLifecycleOwner)
        }
        return viewDataBinding.root*/

        topbar.screenName.text = getString(R.string.my_health_record)
        topbar.imageBack.setOnClickListener {
            finish()
        }
        // load foods
        foodsList.add(
            DashboardGridModel(
                getString(R.string.my_vitals),
                R.drawable.ic_vitals
            )
        )
        foodsList.add(
            DashboardGridModel(
                getString(R.string.my_clinical_visits),
                R.drawable.ic_clinical_visits
            )
        )
        foodsList.add(
            DashboardGridModel(
                getString(R.string.my_medications),
                R.drawable.ic_medication
            )
        )
        foodsList.add(
            DashboardGridModel(
                getString(R.string.my_records),
                R.drawable.ic_records
            )
        )
        foodsList.add(
            DashboardGridModel(
                getString(R.string.my_care_plan),
                R.drawable.ic_care_plan
            )
        )
        foodsList.add(
            DashboardGridModel(
                getString(R.string.my_tele_consult),
                R.drawable.ic_tele_consult
            )
        )
        adapter = DashboardGridAapter(this, foodsList)

        gvDashboard.adapter = adapter

    }

}