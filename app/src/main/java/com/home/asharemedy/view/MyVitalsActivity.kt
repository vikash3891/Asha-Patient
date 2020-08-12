package com.home.asharemedy.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.home.asharemedy.R
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.RequestModel
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_my_vitals.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class MyVitalsActivity : BaseActivity() {

    var cDate = ""
    var selectedVitalName = ""
    var foodsList = ArrayList<ResponseModelClasses.GetMyVitalsSingleResponseModel>()
    var selectedVitalIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_vitals)

        initView()
        checkOnClick()
        selectedVitalIndex = 0
        selectedVital.text = getString(R.string.temperature)
    }

    private fun initView() {

        topbar.screenName.text = getString(R.string.my_vitals)
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(false)

        getPatientVitalList()
    }

    private fun checkOnClick() {
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
    }

    private fun drawLineChart(lineChart: LineChart) {

        var xAxisValues = ArrayList<String>()
        var yAxisValues = ArrayList<Entry>()

        for (i in 0 until foodsList.size) {
/*Entry(
                    foodsList[i].vital_reading!!.toFloat(),
                    foodsList[i].vital_reading!!.toFloat()
                )*/
            yAxisValues.add(
                Entry(
                    i.toFloat(),
                    i.toFloat()
                )
            )
            xAxisValues.add(foodsList[i].vital_date!!)
        }
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.isGranularityEnabled = true
        xAxis.textSize = 7f
        xAxis.labelRotationAngle = -45f

        var lds = LineDataSet(yAxisValues, selectedVitalName)
        lds.lineWidth = 2f

        lds.setColor(Color.RED, 100)

        lds.valueTextSize = 10f
        var lineData = LineData(lds)
        lineChart.data = lineData
        lineChart.setDrawGridBackground(false)
        lineChart.xAxis.textColor = Color.BLACK
        lineChart.axisLeft.textColor = Color.BLACK
        lineChart.description.text = ""
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
        leftAxis.axisMinimum = 0f
        lineChart.invalidate()
        lineChart.refreshDrawableState()

    }

    private fun drawBPLineChart(lineChart: LineChart) {

        val arr1 = ArrayList<String>(10)

        var entries = ArrayList<Entry>()
        var entry = ArrayList<Entry>()
        var lines = ArrayList<LineDataSet>()
        var xAxisValues = ArrayList<String>()
        for (i in 0 until 15) {

            entries.add(Entry(i.toFloat(), i.toFloat()))
            entry.add(Entry((i.toFloat() + 2), i.toFloat()))
            xAxisValues.add(i.toString())
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
    }

    private fun showDialogAilment() {
        try {



            lateinit var dialog: AlertDialog

            val builder = AlertDialog.Builder(this)

            builder.setTitle("Select Vital")

            builder.setSingleChoiceItems(Utils.arrayColors, selectedVitalIndex) { _, which ->

                selectedVitalIndex = which
                selectedVital.text = Utils.arrayColors[which]
                selectedVitalName = Utils.arrayColors[which]
                if (selectedVitalName.equals("Blood Pressure"))
                    drawBPLineChart(lineChart)
                else {
                    drawLineChart(lineChart)
                }

                dialog.dismiss()
            }

            dialog = builder.create()
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openCalendar(dateID: Int) {
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
    }

    private fun getPatientVitalList() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ArrayList<ResponseModelClasses.GetMyVitalsSingleResponseModel>> =
                apiService.getPatientVitalsList(
                    "13",
                    "temperature"
                )//AppPrefences.getUserID(this))
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
                            foodsList = response.body()!!
                            drawLineChart(lineChart)
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

}