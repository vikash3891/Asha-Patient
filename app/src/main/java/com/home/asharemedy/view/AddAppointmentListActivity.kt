package com.home.asharemedy.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.asharemedy.R
import com.home.asharemedy.adapter.AppointmentItemAdapter
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.databinding.ActivityAddAppointmentListBinding
import com.home.asharemedy.model.ListItemModel
import kotlinx.android.synthetic.main.activity_add_appointment_list.*
import kotlinx.android.synthetic.main.activity_add_appointment_list.topbar
import kotlinx.android.synthetic.main.activity_appointment_slots.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddAppointmentListActivity : BaseActivity() {

    private lateinit var viewDataBinding: ActivityAddAppointmentListBinding
    var adapter: AppointmentItemAdapter? = null
    var foodsList = ArrayList<ListItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_appointment_list)



        checkClicks()

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
        adapter = AppointmentItemAdapter(this, foodsList)

        addAppointmentRecyc.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(this@AddAppointmentListActivity)
            // set the custom adapter to the RecyclerView
            adapter = AppointmentItemAdapter(this@AddAppointmentListActivity, foodsList)
        }

    }

    fun showDialogAliment() {
        try {
            lateinit var dialog: AlertDialog
            val arrayColors = arrayOf("EYE", "NOSE", "EAR", "MUSCLE", "BONE", "SKIN", "STOMACH")
            val arrayChecked = booleanArrayOf(false, false, false, false, false, false, false)
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Choose problem Aliment")

            builder.setMultiChoiceItems(arrayColors, arrayChecked) { dialog, which, isChecked ->
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

    fun changeSlotButtonBackgroundSlot(selectedView: View) {
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

        }
    }

    fun checkClicks() {
        try {
            topbar.imageBack.setOnClickListener {
                finish()
            }

            alimentLabel.setOnClickListener {
                showDialogAliment()
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
}