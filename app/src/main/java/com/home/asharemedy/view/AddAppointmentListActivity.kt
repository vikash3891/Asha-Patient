package com.home.asharemedy.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.home.asharemedy.R
import com.home.asharemedy.adapter.AppointmentItemAdapter
import com.home.asharemedy.adapter.UtilitiesListAdapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.databinding.ActivityAddAppointmentListBinding
import com.home.asharemedy.model.AilmentArrayData
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import com.home.asharemedy.utils.Utils.isAilmentOrService
import com.home.asharemedy.utils.Utils.isDoctor
import com.home.asharemedy.utils.Utils.selectedAilmentOrServiceID
import com.home.asharemedy.utils.Utils.selectedAilmentOrServiceName
import kotlinx.android.synthetic.main.activity_add_appointment_list.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.dialog_layout.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAppointmentListActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var adapter: AppointmentItemAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_appointment_list)

        initView()
        checkClicks()
    }

    private fun initView() {
        try {
            setupToolDrawer()
            getAilmentList()
            searchByLabel.text = "Search By Ailment: "
            ailmentSelectedValues.hint = "Choose Ailment"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadList() {
        try {
            adapter = AppointmentItemAdapter(this, Utils.doctorFacilityList)

            addAppointmentRecyc.apply {

                layoutManager = LinearLayoutManager(this@AddAppointmentListActivity)

                adapter =
                    AppointmentItemAdapter(
                        this@AddAppointmentListActivity,
                        Utils.doctorFacilityList
                    )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun changeSlotButtonBackgroundSlot(selectedView: View) {
        if (selectedView == appointmentForDoctor) {
            appointmentForDoctor.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.round_corner_green
                )
            )
            appointmentForDoctor.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

            appointmentForInstitution.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.edittext_border
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
                    R.drawable.edittext_border
                )
            )
            appointmentForFacility.setTextColor(ContextCompat.getColor(this, R.color.colorAppGray))
            isDoctor = true
            searchByLabel.text = "Search By Ailment: "
        }

        if (selectedView == appointmentForFacility) {
            appointmentForDoctor.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.edittext_border
                )
            )
            appointmentForDoctor.setTextColor(ContextCompat.getColor(this, R.color.colorAppGray))

            appointmentForInstitution.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.edittext_border
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
                    R.drawable.round_corner_green
                )
            )
            appointmentForFacility.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            isDoctor = false
            searchByLabel.text = "Search By Service: "
        }
    }

    private fun checkClicks() {
        try {
            topbar.imageBack.setOnClickListener {
                finish()
            }
            bottomBar.layoutSettings.setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            bottomBar.layoutHome.setOnClickListener {
                startActivity(
                    Intent(
                        this@AddAppointmentListActivity,
                        DashboardActivity::class.java
                    )
                )
                finish()
            }
            ailmentSelectedValues.setOnClickListener {
                if (isDoctor) {
                    isAilmentOrService = true
                    openDialog("Choose Ailment", ailmentSelectedValues)
                } else {
                    isAilmentOrService = false
                    openDialog("Choose Service", ailmentSelectedValues)
                }
            }

            appointmentForDoctor.setOnClickListener {
                ailmentSelectedValues.text = ""
                ailmentSelectedValues.hint = "Choose Ailment"
                getDoctorList()
                isAilmentOrService = true
                changeSlotButtonBackgroundSlot(appointmentForDoctor)
            }
            appointmentForInstitution.setOnClickListener {
                changeSlotButtonBackgroundSlot(appointmentForInstitution)
            }

            appointmentForFacility.setOnClickListener {
                ailmentSelectedValues.text = ""
                ailmentSelectedValues.hint = "Choose Service"
//                getFacilityList()
                isAilmentOrService = false
                openDialog("Choose Service", ailmentSelectedValues)
                changeSlotButtonBackgroundSlot(appointmentForFacility)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getAilmentList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ArrayList<ResponseModelClasses.GetAilmentResponseModel>> =
                apiService.getAilmentList()
            call.enqueue(object :
                Callback<ArrayList<ResponseModelClasses.GetAilmentResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.GetAilmentResponseModel>>,
                    response: Response<ArrayList<ResponseModelClasses.GetAilmentResponseModel>>
                ) {
                    try {
                        //dismissDialog()
                        Log.d("Response: ", response.body().toString())
                        if (response.body() != null) {
                            AilmentArrayData.clearArrayList()
                            AilmentArrayData.addArrayList(response.body()!!)
                            getServicesList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.GetAilmentResponseModel>>,
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

    private fun getServicesList() = if (Utils.isConnected(this)) {
        // showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ArrayList<ResponseModelClasses.GetServiceResponseModel>> =
                apiService.getServicesList()
            call.enqueue(object :
                Callback<ArrayList<ResponseModelClasses.GetServiceResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.GetServiceResponseModel>>,
                    response: Response<ArrayList<ResponseModelClasses.GetServiceResponseModel>>
                ) {
                    try {
                        //dismissDialog()
                        Log.d("Response: ", response.body().toString())
                        if (response.body() != null) {
                            AilmentArrayData.clearServicesArrayList()
                            AilmentArrayData.addServicesArrayList(response.body()!!)
                        }
                        getDoctorList()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.GetServiceResponseModel>>,
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

    private fun getFacilityList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetFacilityListResponseModel> =
                apiService.getFacilitiesList(Utils.profileData!!.patient_city)
            call.enqueue(object :
                Callback<ResponseModelClasses.GetFacilityListResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetFacilityListResponseModel>,
                    response: Response<ResponseModelClasses.GetFacilityListResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("FacResponse: ", response.body().toString())
                        if (response.body() != null) {
                            Utils.doctorFacilityList.clear()
                            Utils.doctorFacilityList = response.body()!!.data
                            loadList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetFacilityListResponseModel>,
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

    private fun getFacilityListByService() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetFacilityListResponseModel> =
                apiService.getFacilitiesListByService(
                    selectedAilmentOrServiceID,
                    Utils.profileData!!.patient_city
                )
            call.enqueue(object :
                Callback<ResponseModelClasses.GetFacilityListResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetFacilityListResponseModel>,
                    response: Response<ResponseModelClasses.GetFacilityListResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("FacResponse: ", response.body().toString())
                        if (response.body() != null) {
                            Utils.doctorFacilityList.clear()
                            Utils.doctorFacilityList = response.body()!!.data
                            loadList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetFacilityListResponseModel>,
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

    private fun getDoctorList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetFacilityListResponseModel> =
                apiService.getDoctorsList(Utils.profileData!!.patient_city)
            call.enqueue(object :
                Callback<ResponseModelClasses.GetFacilityListResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetFacilityListResponseModel>,
                    response: Response<ResponseModelClasses.GetFacilityListResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("DocResponse: ", response.body().toString())

                        if (response.body() != null) {
                            Utils.doctorFacilityList.clear()
                            Utils.doctorFacilityList = response.body()!!.data

                            loadList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetFacilityListResponseModel>,
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

    private fun getDoctorListByAilment() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetFacilityListResponseModel> =
                apiService.getDoctorsListByAilment(
                    selectedAilmentOrServiceID,
                    Utils.profileData!!.patient_city
                )
            call.enqueue(object :
                Callback<ResponseModelClasses.GetFacilityListResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetFacilityListResponseModel>,
                    response: Response<ResponseModelClasses.GetFacilityListResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("DocResponse: ", response.body().toString())

                        if (response.body() != null) {
                            Utils.doctorFacilityList.clear()
                            Utils.doctorFacilityList = response.body()!!.data

                            loadList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetFacilityListResponseModel>,
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

    fun updateView(data: ResponseModelClasses.GetAilmentResponseModel) {

    }

    private fun openDialog(title: String, textView: TextView) {
        try {
            val dialog = Dialog(this@AddAppointmentListActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.show()
            dialog.txtTitleTop.text = title

            val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            dialog.dialogRecycleView.layoutManager = layoutManager
            val itemDecor = DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
            dialog.dialogRecycleView.addItemDecoration(itemDecor)
            val mAdapter = UtilitiesListAdapter { position ->
                if (isAilmentOrService) {
                    var data = AilmentArrayData.getArrayItem(position)
                    textView.text = data.ailment
                    selectedAilmentOrServiceID = data.ailment_id
                    selectedAilmentOrServiceName = data.ailment
                    getDoctorListByAilment()
                } else {
                    var data = AilmentArrayData.getServicesArrayItem(position)
                    textView.text = data.service
                    selectedAilmentOrServiceID = data.service_id.toString()
                    selectedAilmentOrServiceName = data.service
                    getFacilityListByService()
                }
                textView.setTextColor(resources.getColor(R.color.colorBlack))
                dialog.dismiss()
            }
            dialog.dialogRecycleView.adapter = mAdapter
        } catch (e: Exception) {
            e.printStackTrace()
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