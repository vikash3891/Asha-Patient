package com.home.asharemedy.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
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

class MyVitalsActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var cDate = ""
    private var selectedVitalName = ""
    var foodsList = ArrayList<ResponseModelClasses.GetMyVitalsSingleResponseModel.TableData>()
    private var selectedVitalIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_vitals)

        try {
            selectedVitalIndex = 0
            selectedVital.text = getString(R.string.temperature)
            selectedVitalName = getString(R.string.temperature)

            //initView()
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

    override fun onResume() {
        super.onResume()
        initView()
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
                drawerLayout.openDrawer(GravityCompat.START)
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
            var yAxisValues = ArrayList<Entry>()

            try {
                for (i in 0 until foodsList.size) {

                    yAxisValues.add(
                        Entry(
                            i.toFloat(),
                            foodsList[i].vital_reading.toFloat()
                        )
                    )
                    xAxisValues.add(foodsList[i].vital_date)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val xAxis = lineChart.xAxis
            Log.d("Size: ", yAxisValues.size.toString() + " " + xAxisValues.size)
            xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.textSize = 7f
            xAxis.labelRotationAngle = -45f
            xAxis.axisMinimum = 0f
            xAxis.labelCount = xAxisValues.size

            var lds = LineDataSet(yAxisValues, selectedVitalName)
            lds.lineWidth = 2f
            lds.setColor(ContextCompat.getColor(this, R.color.colorAccent), 100)
            lds.valueTextSize = 10f

            var lineData = LineData(lds)
            lineChart.data = lineData
            lineChart.animateXY(1000, 1000)

            val leftAxis = lineChart.axisLeft
            leftAxis.setDrawGridLines(false)
            leftAxis.spaceTop = 35f
            leftAxis.axisMinimum = 0f

            lineChart.legend.isEnabled = false
            lineChart.isHorizontalScrollBarEnabled = true
            lineChart.canScrollHorizontally(1)
            lineChart.axisRight.isEnabled = false

            lineChart.setVisibleXRangeMaximum(5f)

            lineChart.refreshDrawableState()
            lineChart.invalidate()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun drawBPLineChart(lineChart: LineChart) {

        try {
            var entries = ArrayList<Entry>()
            var entry = ArrayList<Entry>()
            var lines = ArrayList<LineDataSet>()
            var xAxisValues = ArrayList<String>()

            for (i in 0 until foodsList.size) {
                val parts = foodsList[i].vital_reading.split(",")

                entries.add(
                    Entry(
                        i.toFloat(),
                        parts[0].toFloat()
                    )
                )
                entry.add(
                    Entry(
                        i.toFloat(),
                        parts[1].toFloat()
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
            lDataSet1.setDrawFilled(false)
            lDataSet1.setColors(ContextCompat.getColor(this, R.color.colorComparePreviousYear))
            lines.add(lDataSet1)

            var lDataSet2 = LineDataSet(entry, selectedVitalName)
            lDataSet2.setDrawFilled(false)
            lDataSet2.setColors(ContextCompat.getColor(this, R.color.colorAccent))
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
            lineChart.refreshDrawableState()
            lineChart.invalidate()

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
            dpd.datePicker.maxDate = System.currentTimeMillis() - 1000;
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
            val call: Call<ResponseModelClasses.GetMyVitalsSingleResponseModel> =
                apiService.getPatientVitalsList(
                    AppPrefences.getUserID(this),
                    selectedVitalName.toLowerCase()
                )
            call.enqueue(object :
                Callback<ResponseModelClasses.GetMyVitalsSingleResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetMyVitalsSingleResponseModel>,
                    response: Response<ResponseModelClasses.GetMyVitalsSingleResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("VitalResponse: ", response.body().toString())
                        if (response.body() != null) {
                            foodsList.clear()

                            Log.d("VitalResponseSize: ", response.body()!!.data.size.toString())

                            if (response.body()!!.data.size > 0) {
                                lineChart.visibility = View.VISIBLE
                                foodsList = response.body()!!.data
                                if (selectedVitalName.equals("bloodpressure"))
                                    drawBPLineChart(lineChart)
                                else {
                                    drawLineChart(lineChart)
                                }
                            } else {
                                lineChart.visibility = View.GONE
                                Toast.makeText(
                                    this@MyVitalsActivity,
                                    "Vital reading not available.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetMyVitalsSingleResponseModel>,
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