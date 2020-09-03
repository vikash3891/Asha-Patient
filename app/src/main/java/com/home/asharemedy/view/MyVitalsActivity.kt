package com.home.asharemedy.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.navigation.NavigationView
import com.home.asharemedy.R
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.RequestModel
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_my_vitals.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class MyVitalsActivity : BaseActivity() , NavigationView.OnNavigationItemSelectedListener{

    private var cDate = ""
    private var selectedVitalName = ""
    var foodsList = ArrayList<ResponseModelClasses.GetMyVitalsSingleResponseModel>()
    private var selectedVitalIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_vitals)

        try {
            selectedVitalIndex = 0
            selectedVital.text = getString(R.string.temperature)
            selectedVitalName = getString(R.string.temperature)

            initView()
            checkOnClick()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {

        try {
            topbar.screenName.text = getString(R.string.my_vitals)
            lineChart.setTouchEnabled(true)
            lineChart.setPinchZoom(false)
            setupToolDrawer()
            getPatientVitalList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkOnClick() {
        try {
            topbar.imageBack.setOnClickListener {
                finish()
            }
            selectedVital.setOnClickListener {
                showDialogAilment()
            }
            startDate.setOnClickListener {
                openCalendar(1)
            }
            endDate.setOnClickListener {
                openCalendar(2)
            }

            bottomBar.layoutSettings.setOnClickListener {
                logoutAlertDialog()
            }
            bottomBar.layoutHome.setOnClickListener {
                startActivity(
                    Intent(
                        this@MyVitalsActivity,
                        DashboardActivity::class.java
                    )
                )
                finish()
            }

            floatingActionButton.setOnClickListener {
                startActivity(Intent(this, ActivityAddVitalRecord::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun drawLineChart(lineChart: LineChart) {

        try {
            var xAxisValues = ArrayList<String>()
            val labels = arrayOf("S", "M", "T", "W", "T", "F", "S")
            var yAxisValues = ArrayList<Entry>()

            for (i in 0 until foodsList.size) {

                yAxisValues.add(
                    Entry(
                        foodsList[i].vital_reading.toFloat(),
                        foodsList[i].vital_reading.toFloat()
                    )
                )
                xAxisValues.add(foodsList[i].vital_date)
            }

            val xAxis = lineChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            //xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            //xAxis.isGranularityEnabled = true
            xAxis.textSize = 7f
            xAxis.labelRotationAngle = -45f

            var lds = LineDataSet(yAxisValues, selectedVitalName)
            lds.lineWidth = 2f

            lds.setColor(Color.RED, 100)

            lds.valueTextSize = 10f
            var lineData = LineData(lds)
            lineChart.data = lineData
            /* lineChart.setDrawGridBackground(false)
             lineChart.xAxis.textColor = Color.BLACK
             lineChart.axisLeft.textColor = Color.BLACK
             lineChart.description.text = "Vital Reading"
             lineChart.moveViewToX(5F);
             lineChart.axisRight.textColor = Color.BLACK
             lineChart.legend.isEnabled = false
             lineChart.data.setValueTextColor(Color.BLACK)
             lineChart.isEnabled = true
             lineChart.axisRight.isEnabled = false
             lineChart.setVisibleXRangeMaximum(5f)
             lineChart.isHorizontalScrollBarEnabled = true
             lineChart.canScrollHorizontally(1)
             val leftAxis = lineChart.axisLeft
             leftAxis.setDrawGridLines(false)
             leftAxis.spaceTop = 35f
             leftAxis.axisMinimum = 0f*/

            lineChart.axisRight.isEnabled = false


            lineChart.invalidate()
            lineChart.refreshDrawableState()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun drawBPLineChart(lineChart: LineChart) {

        try {
            val arr1 = ArrayList<String>(10)

            var entries = ArrayList<Entry>()
            var entry = ArrayList<Entry>()
            var lines = ArrayList<LineDataSet>()
            var xAxisValues = ArrayList<String>()
            /*for (i in 0 until 15) {

                entries.add(Entry(i.toFloat(), i.toFloat()))
                entry.add(Entry((i.toFloat() + 2), i.toFloat()))
                xAxisValues.add(i.toString())
            }*/

            for (i in 0 until foodsList.size) {
                val parts = foodsList[i].vital_reading.split(",")
                val partsLow = parts[0].split("/")
                val partsHigh = parts[1].split("/")
                entries.add(
                    Entry(
                        partsLow[0].toFloat(),
                        partsLow[0].toFloat()
                    )
                )
                entry.add(
                    Entry(
                        partsHigh[0].toFloat(),
                        partsHigh[0].toFloat()
                    )
                )
                xAxisValues.add(foodsList[i].vital_date)
            }
            val xAxis = lineChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.textSize = 7f
            xAxis.labelRotationAngle = -45f

            xAxis.isGranularityEnabled = true
            var lDataSet1 = LineDataSet(entries, selectedVitalName)
            lDataSet1.setDrawFilled(true)
            lines.add(lDataSet1)

            var lDataSet2 = LineDataSet(entry, selectedVitalName)
            lDataSet2.setDrawFilled(true)
            lines.add(lDataSet2)

            lineChart.data = LineData(lines as List<ILineDataSet>?)
            lineChart.xAxis.textColor = Color.BLACK
            lineChart.axisLeft.textColor = Color.BLACK
            lineChart.description.text = ""
            lineChart.moveViewToX(5F);
            lineChart.axisRight.isEnabled = false
            lineChart.legend.isEnabled = false
            lineChart.data.setValueTextColor(Color.BLACK)
            lineChart.isEnabled = true
            lineChart.setVisibleXRangeMaximum(5f)
            lineChart.isHorizontalScrollBarEnabled = true
            lineChart.canScrollHorizontally(1)
            lineChart.invalidate()
            lineChart.refreshDrawableState()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showDialogAilment() {
        try {
            lateinit var dialog: AlertDialog

            val builder = AlertDialog.Builder(this)

            builder.setTitle("Select Vital")

            builder.setSingleChoiceItems(Utils.arrayColors, selectedVitalIndex) { _, which ->

                selectedVitalIndex = which
                selectedVital.text = Utils.arrayColors[which]

                when (Utils.arrayColors[which]) {
                    getString(R.string.temperature) -> {
                        selectedVitalName = getString(R.string.temperature)
                    }
                    getString(R.string.blood_pressure) -> {
                        selectedVitalName = "bloodpressure"

                    }
                    getString(R.string.pulse) -> {
                        selectedVitalName = getString(R.string.pulse)
                    }
                    getString(R.string.height) -> {
                        selectedVitalName = getString(R.string.height)

                    }
                    getString(R.string.weight) -> {
                        selectedVitalName = getString(R.string.weight)

                    }
                    getString(R.string.respiration_rate) -> {
                        selectedVitalName = "respiration_rate"


                    }
                    getString(R.string.calories_burned) -> {

                        selectedVitalName = "calories_burned"

                    }
                    getString(R.string.blood_sugar) -> {

                        selectedVitalName = "bloodsugar"

                    }
                    getString(R.string.oxygen_saturation) -> {

                        selectedVitalName = "oxygen_saturation"

                    }
                }

                getPatientVitalList()



                dialog.dismiss()
            }

            dialog = builder.create()
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openCalendar(dateID: Int) {
        try {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPatientVitalList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            Log.d("selectedVitalName: ", selectedVitalName.toLowerCase())
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ArrayList<ResponseModelClasses.GetMyVitalsSingleResponseModel>> =
                apiService.getPatientVitalsList(
                    AppPrefences.getUserID(this),
                    selectedVitalName.toLowerCase()
                )
            call.enqueue(object :
                Callback<ArrayList<ResponseModelClasses.GetMyVitalsSingleResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.GetMyVitalsSingleResponseModel>>,
                    response: Response<ArrayList<ResponseModelClasses.GetMyVitalsSingleResponseModel>>
                ) {
                    try {
                        dismissDialog()
                        Log.d("VitalResponse: ", response.body().toString())
                        if (response.body() != null) {
                            foodsList.clear()
                            foodsList = response.body()!!
                            if (selectedVitalName.equals("bloodsugar"))
                                drawBPLineChart(lineChart)
                            else {
                                drawLineChart(lineChart)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.GetMyVitalsSingleResponseModel>>,
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

        headerLayout!!.title.text = getString(R.string.my_vitals)

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