package com.home.asharemedy.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.home.asharemedy.R
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.databinding.ActivityAppointmentSlotsBinding
import com.home.asharemedy.payu.ActivityPayUMain
import kotlinx.android.synthetic.main.activity_appointment_slots.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AppointmentSlotActivity : BaseActivity() {

    private lateinit var viewDataBinding: ActivityAppointmentSlotsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_slots)

        try {
            topbarAppointment.screenName.text = "Appointment"

            checkClicks()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun checkClicks() {
        try {
            topbarAppointment.imageBack.setOnClickListener {
                finish()
            }
            proceedToPay.setOnClickListener {
                startActivity(Intent(this@AppointmentSlotActivity, ActivityPayUMain::class.java))
            }

            slotCalendar.setOnClickListener() {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                        try {
                            val date = Date(year - 1900, monthOfYear, dayOfMonth)
                            val formatter = SimpleDateFormat("dd-MM-yyyy")
                            val cdate = formatter.format(date)

                            // Display Selected date in textbox
                            slotCalendar.setText("" + setMonth(monthOfYear + 1) + " " + dayOfMonth + ", " + year)
                            //getDatesBetweenStartAndFinish(cdate.toString())
                        } catch (e1: ParseException) {
                            e1.printStackTrace()
                        }
                    },
                    year,
                    month,
                    day
                )
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show()
            }

            slotsMorningButton.setOnClickListener() {
                changeSlotButtonBackgroundSlot(slotsMorningButton)
            }
            slotsAfternoonButton.setOnClickListener() {
                changeSlotButtonBackgroundSlot(slotsAfternoonButton)
            }
            slotsEveningButton.setOnClickListener() {
                changeSlotButtonBackgroundSlot(slotsEveningButton)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setMonth(monthOfYear: Int): String {
        when (monthOfYear) {
            1 -> return "Jan"
            2 -> return "Feb"
            3 -> return "Mar"
            4 -> return "Apr"
            5 -> return "May"
            6 -> return "Jun"
            7 -> return "Jul"
            8 -> return "Aug"
            9 -> return "Sep"
            10 -> return "Oct"
            11 -> return "Nov"
            12 -> return "Dec"
            else -> { // Note the block
                return "June"
            }
        }
    }

    fun changeSlotButtonBackgroundSlot(selectedView: View) {
        if (selectedView == slotsMorningButton) {
            slotsMorningButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.rounded_corner_blue
                )
            )

            slotsAfternoonButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_round_blue_stroke
                )
            )

            slotsEveningButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_round_blue_stroke
                )
            )

        }

        if (selectedView == slotsAfternoonButton) {
            slotsMorningButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_round_blue_stroke
                )
            )

            slotsAfternoonButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.rounded_corner_blue
                )
            )

            slotsEveningButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_round_blue_stroke
                )
            )

        }

        if (selectedView == slotsEveningButton) {
            slotsMorningButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_round_blue_stroke
                )
            )

            slotsAfternoonButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_round_blue_stroke
                )
            )

            slotsEveningButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.rounded_corner_blue
                )
            )

        }
    }
}