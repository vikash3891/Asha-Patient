package com.home.asharemedy.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.home.asharemedy.R
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.chat.ui.activity.ChatLoginActivity
import com.home.asharemedy.databinding.LayoutAppointmentDetailBinding
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.layout_appointment_detail.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ListItemDetailActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var doctorFacilityDetails:
            ResponseModelClasses.GetFacilityListResponseModel.TableData1? = null
    var paymentDetails:
            ResponseModelClasses.GetPaymentHistoryResponseModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_appointment_detail)

        try {
            topbar.screenName.text = "Appointment Details"

            initView()
            checkClick()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initView() {

        try {
            setupToolDrawer()
            purposeValue.text =
                Utils.selectedAppointmentDetails[0].appointment_info.purpose
            remarksValue.text =
                Utils.selectedAppointmentDetails[0].appointment_info.remarks
            statusValue.text =
                Utils.selectedAppointmentDetails[0].appointment_info.status

            providerTypeValue.text =
                Utils.selectedAppointmentDetails[0].appointment_provider_info.provider_type
            facilityNameValue.text =
                Utils.selectedAppointmentDetails[0].appointment_provider_info.provider_name


            amountValue.text =
                getString(R.string.rupees_symbol) + " " + Utils.selectedAppointmentDetails[0].appointment_info.payment_amount

            dateValue.text = Utils.selectedAppointmentDetails[0].appointment_slot_info.slot_date
            startTimeValue.text =
                Utils.selectedAppointmentDetails[0].appointment_slot_info.start_time
            endTimeValue.text = Utils.selectedAppointmentDetails[0].appointment_slot_info.end_time

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkClick() {

        viewPayment.setOnClickListener {
            getPaymentByID()
        }
        viewProfile.setOnClickListener {
            getDoctorByID()
        }
        ivChat.setOnClickListener {
            val intent = Intent(this@ListItemDetailActivity, ChatLoginActivity::class.java);
            intent.putExtra("Username", AppPrefences.getUserName(this))
            startActivity(intent)
        }
        topbar.imageBack.setOnClickListener {
            finish()
        }
        bottomBar.layoutSettings.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        bottomBar.layoutHome.setOnClickListener {
            startActivity(
                Intent(
                    this@ListItemDetailActivity,
                    DashboardActivity::class.java
                )
            )
            finish()
        }
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

        headerLayout!!.title.text = getString(R.string.appointment_detail)

        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer_icon)
        toggle.setToolbarNavigationClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    private fun getDoctorByID() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)

            val call: Call<ResponseModelClasses.GetFacilityListResponseModel.TableData1> =
                apiService.getDoctorByID(
                    Utils.selectedAppointmentDetails[0].appointment_provider_info.provider_id
                )
            call.enqueue(object :
                Callback<ResponseModelClasses.GetFacilityListResponseModel.TableData1> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetFacilityListResponseModel.TableData1>,
                    response: Response<ResponseModelClasses.GetFacilityListResponseModel.TableData1>
                ) {
                    try {
                        dismissDialog()
                        Log.d("getDoctorByIDResponse: ", response.body().toString())

                        if (response.body() != null) {
                            doctorFacilityDetails = response.body()!!
                            showProviderDetailDialog()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetFacilityListResponseModel.TableData1>,
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

    private fun getPaymentByID() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)

            val call: Call<ResponseModelClasses.GetPaymentHistoryResponseModel> =
                apiService.getPaymentDetailsByID(
                    Utils.selectedAppointmentDetails[0].appointment_info.payment_id
                )
            call.enqueue(object :
                Callback<ResponseModelClasses.GetPaymentHistoryResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetPaymentHistoryResponseModel>,
                    response: Response<ResponseModelClasses.GetPaymentHistoryResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("getPaymentByID: ", response.body().toString())

                        if (response.body() != null) {
                            paymentDetails = response.body()!!
                            showPaymentDetailDialog()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetPaymentHistoryResponseModel>,
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

    private fun showProviderDetailDialog() {
        try {
            var dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_provider_detail)

            val doctorName = dialog.findViewById(R.id.doctorName) as TextView
            val doctorGender = dialog.findViewById(R.id.doctorGender) as TextView
            val doctorSpeciality = dialog.findViewById(R.id.doctorSpeciality) as TextView
            val experience = dialog.findViewById(R.id.experience) as TextView
            val consultationFees = dialog.findViewById(R.id.consultationFees) as TextView

            val layoutOk = dialog.findViewById(R.id.layoutOk) as LinearLayout

            doctorName.text = doctorFacilityDetails!!.name
            doctorGender.text = doctorFacilityDetails!!.gender
            doctorSpeciality.text = doctorFacilityDetails!!.specialization
            experience.text = "Experience: " + doctorFacilityDetails!!.experience
            consultationFees.text = doctorFacilityDetails!!.fees

            layoutOk.setOnClickListener { dialog.dismiss() }
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun showPaymentDetailDialog() {
        try {
            var dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_payment_details)

            val amountValue = dialog.findViewById(R.id.amountValue) as TextView
            val discountValue = dialog.findViewById(R.id.discountValue) as TextView
            val convenienceValue = dialog.findViewById(R.id.convenienceValue) as TextView
            val grossTotalValue = dialog.findViewById(R.id.grossTotalValue) as TextView
            val statusValue = dialog.findViewById(R.id.statusValue) as TextView
            val layoutOk = dialog.findViewById(R.id.layoutOk) as LinearLayout

            amountValue.text = paymentDetails!!.amount
            discountValue.text = paymentDetails!!.discount_percentage
            convenienceValue.text = paymentDetails!!.convenience_fee
            grossTotalValue.text = paymentDetails!!.gross_total
            statusValue.text = paymentDetails!!.status
            layoutOk.setOnClickListener { dialog.dismiss() }
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}