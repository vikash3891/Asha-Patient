package com.home.asharemedy.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.home.asharemedy.R
import com.home.asharemedy.adapter.AppointSlotListAdapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.payu.ActivityPayU
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import com.home.asharemedy.utils.Utils.appointmentSlotList
import kotlinx.android.synthetic.main.activity_appointment_slots.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AppointmentSlotActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var adapter: AppointSlotListAdapter? = null
    var cdate = ""

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

        try {
            setupToolDrawer()
            slotCalendar.text = Utils.getDate()
            doctorName.text = Utils.selectedDoctorFacility!!.name
            doctorSpeciality.text = Utils.selectedDoctorFacility!!.specialization
            consultationFees.text =
                getString(R.string.rupees_symbol) + Utils.selectedDoctorFacility!!.fees
            address.text =
                Utils.selectedDoctorFacility!!.address1 + " " + Utils.selectedDoctorFacility!!.address2
            cdate = Utils.getDate()
            getSlotList()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun loadList() {
        adapter = AppointSlotListAdapter(this, appointmentSlotList)

        gvSlots.adapter = adapter
    }

    private fun checkClicks() {
        try {
            topbarAppointment.imageBack.setOnClickListener {
                finish()
            }
            proceedToPay.setOnClickListener {
                for (i in 0 until Utils.appointmentSlotList.size) {
                    Log.d("SelSlotList", Utils.appointmentSlotList[i].isSelected.toString())
                }
                if (Utils.selectedGridList.size > 0)
                    startActivity(Intent(this@AppointmentSlotActivity, ActivityPayU::class.java))
                else {

                }
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
                                "" + Utils.setMonth(monthOfYear + 1) + " " + dayOfMonth + ", " + year
                        } catch (e1: ParseException) {
                            e1.printStackTrace()
                        }
                    },
                    year,
                    month,
                    day
                )
                dpd.datePicker.minDate = System.currentTimeMillis() - 1000;
                dpd.show()
            }

            slotsMorningButton.setOnClickListener {
                changeSlotButtonBackgroundSlot(slotsMorningButton)
            }
            slotsAfternoonButton.setOnClickListener {
                changeSlotButtonBackgroundSlot(slotsAfternoonButton)
            }
            slotsEveningButton.setOnClickListener {
                changeSlotButtonBackgroundSlot(slotsEveningButton)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun changeSlotButtonBackgroundSlot(selectedView: View) {
        if (selectedView == slotsMorningButton) {
            slotsMorningButton.background = ContextCompat.getDrawable(
                this,
                R.drawable.drawable_selected
            )

            slotsAfternoonButton.background = ContextCompat.getDrawable(
                this,
                R.drawable.drawable_unselected
            )

            slotsEveningButton.background = ContextCompat.getDrawable(
                this,
                R.drawable.drawable_unselected
            )

        }

        if (selectedView == slotsAfternoonButton) {
            slotsMorningButton.background = ContextCompat.getDrawable(
                this,
                R.drawable.drawable_unselected
            )

            slotsAfternoonButton.background = ContextCompat.getDrawable(
                this,
                R.drawable.drawable_selected
            )

            slotsEveningButton.background = ContextCompat.getDrawable(
                this,
                R.drawable.drawable_unselected
            )

        }

        if (selectedView == slotsEveningButton) {
            slotsMorningButton.background = ContextCompat.getDrawable(
                this,
                R.drawable.drawable_unselected
            )

            slotsAfternoonButton.background = ContextCompat.getDrawable(
                this,
                R.drawable.drawable_unselected
            )

            slotsEveningButton.background = ContextCompat.getDrawable(
                this,
                R.drawable.drawable_selected
            )

        }
    }

    private fun getSlotList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)

            val call: Call<ArrayList<ResponseModelClasses.GetSlotListResponseModel>> =
                apiService.getSlotList(
                    Utils.selectedDoctorFacitiyID,
                    cdate
                )//Utils.selectedDoctorFacitiyID,cdate
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        try {
            Log.e("item.itemId", item.itemId.toString())
            when (item.itemId) {

                R.id.logout -> {
                    logoutAlertDialog()
                }

                R.id.navProfile -> {
                    startActivity(Intent(this, MyProfile::class.java))
                }

                R.id.navChangePassword -> {
                    startWebActivity(getString(R.string.privacy_policy), Constants.FAQ)
                }

                R.id.navPayment -> {
                    startActivity(Intent(this, ActivityPaymentHistory::class.java))
                }

                R.id.navFaq -> {
                    startWebActivity(getString(R.string.privacy_policy), Constants.FAQ)
                }

                R.id.navTerms -> {
                    startWebActivity(
                        getString(R.string.terms_and_conditions),
                        Constants.TERMS_AND_CONDITION
                    )
                }

                R.id.navPrivacy -> {
                    startWebActivity(getString(R.string.privacy_policy), Constants.PRIVACY_POLICY)
                }

            }
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false

        }
    }

    private fun setupToolDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            headerLayout!!.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        headerLayout!!.title.text = getString(R.string.appointments)

        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer_icon)
        toggle.setToolbarNavigationClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

}