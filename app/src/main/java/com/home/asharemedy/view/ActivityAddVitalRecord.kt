package com.home.asharemedy.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.home.asharemedy.R
import com.home.asharemedy.adapter.PaymentListAdapter
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import kotlinx.android.synthetic.main.activity_add_vital_record.*
import kotlinx.android.synthetic.main.activity_clinic_visit.*
import kotlinx.android.synthetic.main.activity_clinic_visit.bottomBar
import kotlinx.android.synthetic.main.activity_clinic_visit.topbar
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*

class ActivityAddVitalRecord : BaseActivity() {

    var adapter: PaymentListAdapter? = null
    var selectedVitalIndex = -1
    var selectedVitalName = ""

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
                Toast.makeText(
                    this,
                    "Work In Progress. Please try again later.",
                    Toast.LENGTH_SHORT
                ).show()
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

            var arrayColors = arrayOf(
                getString(R.string.temperature),
                getString(R.string.pulse),
                getString(R.string.height),
                getString(R.string.weight),
                getString(R.string.respiration_rate),
                getString(R.string.oxygen_saturation),
                getString(R.string.pain_scale_score),
                getString(R.string.lmp),
                getString(R.string.coma_scale),
                getString(R.string.bca),
                getString(R.string.fat),
                getString(R.string.muscle),
                getString(R.string.hydration),
                getString(R.string.steps),
                getString(R.string.calories_burned)
            )

            lateinit var dialog: AlertDialog

            val builder = AlertDialog.Builder(this)

            builder.setTitle("Select Vital")

            builder.setSingleChoiceItems(arrayColors, selectedVitalIndex) { _, which ->

                selectedVitalIndex = which
                vitalName.text = arrayColors[which]
                selectedVitalName = arrayColors[which]
                if (selectedVitalName.equals("Blood Pressure"))
                else {

                }

                dialog.dismiss()
            }

            dialog = builder.create()
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}