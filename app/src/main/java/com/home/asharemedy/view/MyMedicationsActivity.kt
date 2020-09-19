package com.home.asharemedy.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.home.asharemedy.R
import com.home.asharemedy.adapter.MyMedicationListAdapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_clinic_visit.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyMedicationsActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var adapter: MyMedicationListAdapter? = null
    var medicationsList = ArrayList<ResponseModelClasses.GetMyMedicationResponseModel.TableData>()
    var cDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic_visit)

        checkOnClick()

    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        topbar.screenName.text = getString(R.string.my_medications)
        header.visibility = View.GONE
        floatingActionButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        floatingActionButton.visibility = View.VISIBLE

        setupToolDrawer()
        getMyMedicationsList()
    }

    private fun checkOnClick() {

        topbar.imageBack.setOnClickListener {
            finish()
        }

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddMedicationActivity::class.java))
        }

        startDate.setOnClickListener {
            openCalendar(1)
        }
        endDate.setOnClickListener {
            openCalendar(2)
        }

        bottomBar.layoutSettings.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        bottomBar.layoutHome.setOnClickListener {
            startActivity(
                Intent(
                    this@MyMedicationsActivity,
                    DashboardActivity::class.java
                )
            )
            finish()
        }
    }

    private fun openCalendar(dateID: Int) {
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
                    cDate = formatter.format(date)

                    if (dateID == 1) {
                        startDate.text =
                            "" + dayOfMonth + " " + Utils.setMonth(monthOfYear + 1) + ", " + year
                    } else {
                        endDate.text =
                            "" + dayOfMonth + " " + Utils.setMonth(monthOfYear + 1) + ", " + year
                    }

                } catch (e1: ParseException) {
                    e1.printStackTrace()
                }
            },
            year,
            month,
            day
        )
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        dpd.show()
    }

    private fun getMyMedicationsList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetMyMedicationResponseModel> =
                apiService.getMyMedicationsList(
                    AppPrefences.getUserID(this)
                )
            call.enqueue(object :
                Callback<ResponseModelClasses.GetMyMedicationResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetMyMedicationResponseModel>,
                    response: Response<ResponseModelClasses.GetMyMedicationResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("MedicationResponse: ", response.body().toString())
                        if (response.body() != null) {
                            if (response.body()!!.data != null && response.body()!!.data.size > 0) {
                                medicationsList = response.body()!!.data
                                loadList()
                            } else {
                                Toast.makeText(
                                    this@MyMedicationsActivity,
                                    "No Payment Data available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetMyMedicationResponseModel>,
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

    private fun loadList() {
        adapter = MyMedicationListAdapter(this, medicationsList)

        listRecyc.apply {

            layoutManager = LinearLayoutManager(this@MyMedicationsActivity)

            adapter =
                MyMedicationListAdapter(this@MyMedicationsActivity, medicationsList)
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

        headerLayout!!.title.text = getString(R.string.my_medications)

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