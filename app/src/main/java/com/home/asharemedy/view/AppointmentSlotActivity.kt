package com.home.asharemedy.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.home.asharemedy.R
import com.home.asharemedy.adapter.AppointSlotListAdapter
import com.home.asharemedy.adapter.DashboardGridAapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.databinding.ActivityAppointmentSlotsBinding
import com.home.asharemedy.model.AppointSlotListModel
import com.home.asharemedy.model.DashboardGridModel
import com.home.asharemedy.payu.ActivityPayUMain
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import com.home.asharemedy.utils.Utils.appointmentSlotList
import kotlinx.android.synthetic.main.activity_appointment_slots.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AppointmentSlotActivity : BaseActivity() {

    var adapter: AppointSlotListAdapter? = null
    private lateinit var viewDataBinding: ActivityAppointmentSlotsBinding
    var slotList = ArrayList<ResponseModelClasses.GetSlotListResponseModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_slots)

        try {
            topbarAppointment.screenName.text = "Appointment"
            initView()
            checkClicks()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initView() {

        slotCalendar.text = Utils.getDate()
        doctorName.text = Utils.doctorFacilityList[0].name
        doctorSpeciality.text = Utils.doctorFacilityList[0].type
        address.text =
            Utils.doctorFacilityList[0].address1 + " " + Utils.doctorFacilityList[0].address2

        getSlotList()

    }

    fun loadList() {
        adapter = AppointSlotListAdapter(this, appointmentSlotList)

        gvSlots.adapter = adapter
    }

    var cdate = ""
    private fun checkClicks() {
        try {
            topbarAppointment.imageBack.setOnClickListener {
                finish()
            }
            proceedToPay.setOnClickListener {
                for (i in 0 until Utils.appointmentSlotList.size) {
                    Log.d("SeleSlotList", Utils.appointmentSlotList[i].isSelected.toString())
                }
                startActivity(Intent(this@AppointmentSlotActivity, ActivityPayUMain::class.java))
            }

            slotCalendar.setOnClickListener() {
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
                            cdate = formatter.format(date)

                            slotCalendar.text =
                                "" + setMonth(monthOfYear + 1) + " " + dayOfMonth + ", " + year
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

    private fun changeSlotButtonBackgroundSlot(selectedView: View) {
        if (selectedView == slotsMorningButton) {
            slotsMorningButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_selected
                )
            )

            slotsAfternoonButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_unselected
                )
            )

            slotsEveningButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_unselected
                )
            )

        }

        if (selectedView == slotsAfternoonButton) {
            slotsMorningButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_unselected
                )
            )

            slotsAfternoonButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_selected
                )
            )

            slotsEveningButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_unselected
                )
            )

        }

        if (selectedView == slotsEveningButton) {
            slotsMorningButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_unselected
                )
            )

            slotsAfternoonButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_unselected
                )
            )

            slotsEveningButton.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.drawable_selected
                )
            )

        }
    }

    private fun getSlotList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)

            val call: Call<ArrayList<ResponseModelClasses.GetSlotListResponseModel>> =
                apiService.getSlotList("7", "2020-08-05")//Utils.selectedDoctorFacitiyID,cdate
            call.enqueue(object :
                Callback<ArrayList<ResponseModelClasses.GetSlotListResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.GetSlotListResponseModel>>,
                    response: Response<ArrayList<ResponseModelClasses.GetSlotListResponseModel>>
                ) {
                    try {
                        dismissDialog()
                        Log.d("SlotResponse: ", response.body().toString())

                        if (response.body() != null) {
                            appointmentSlotList.clear()
                            appointmentSlotList = response.body()!!

                            loadList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.GetSlotListResponseModel>>,
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
}