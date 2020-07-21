package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.home.asharemedy.R
import com.home.asharemedy.adapter.DashboardGridAapter
import com.home.asharemedy.databinding.ActivityDashboardBinding
import com.home.asharemedy.model.DashboardGridModel
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.item_profile_details.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*

class DashboardActivity : AppCompatActivity() {

    var adapter: DashboardGridAapter? = null
    var foodsList = ArrayList<DashboardGridModel>()
    private lateinit var viewDataBinding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        /*viewDataBinding = ActivityDashboardBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProviders.of(this@RepoListFragment).get(RepoListViewModel::class.java)
            setLifecycleOwner(viewLifecycleOwner)
        }
        return viewDataBinding.root*/

        topbar.screenName.text = getString(R.string.dashboard)
        topbar.imageBack.visibility = View.GONE
        try {
            profileDetails.viewProfile.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, MyProfile::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // load foods
        foodsList.add(
            DashboardGridModel(
                getString(R.string.my_health_record),
                R.drawable.ic_health_record
            )
        )
        foodsList.add(
            DashboardGridModel(
                getString(R.string.appointments),
                R.drawable.ic_appointment
            )
        )
        foodsList.add(DashboardGridModel(getString(R.string._247_doctor), R.drawable.ic_call))
        foodsList.add(DashboardGridModel(getString(R.string.talk_to_thb), R.drawable.ic_dialer_sip))
        foodsList.add(DashboardGridModel(getString(R.string.health_info), R.drawable.ic_timeline))
        foodsList.add(DashboardGridModel(getString(R.string.more_thb), R.drawable.ic_group))
        adapter = DashboardGridAapter(this, foodsList)

        gvDashboard.adapter = adapter

    }

}