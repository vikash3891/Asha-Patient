package com.home.asharemedy.view

import android.app.AlertDialog
import android.app.DatePickerDialog
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

    var selectedVitalIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_vitals)

        initView()
        checkOnClick()
        //drawLineChart(lineChart)
        selectedVitalIndex = 0
        selectedVital.text = getString(R.string.blood_pressure)
        drawBPLineChart(lineChart)
    }

    private fun initView() {

        topbar.screenName.text = getString(R.string.my_vitals)
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
    }

    private fun checkOnClick() {
        topbar.imageBack.setOnClickListener {
            finish()
        }
        selectedVital.setOnClickListener {
            showDialogAliment()
        }
        startDate.setOnClickListener {
            openCalendar(1)
        }
        endDate.setOnClickListener {
            openCalendar(2)
        }
    }

    private fun drawLineChart(lineChart: LineChart) {

        var xAxisValues = ArrayList<String>()
        var yAxisValues = ArrayList<Entry>()

        for (i in 0 until 15) {

            yAxisValues.add(Entry(i.toFloat(), i.toFloat()))
            xAxisValues.add(i.toString())
        }
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

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
        lineChart.axisRight.textColor = Color.BLACK
        lineChart.legend.isEnabled = false
        lineChart.data.setValueTextColor(Color.BLACK)
        lineChart.isEnabled = true
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
        lineChart.axisRight.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.data.setValueTextColor(Color.BLACK)
        lineChart.isEnabled = true
        lineChart.invalidate()
        lineChart.refreshDrawableState()
    }

    private fun showDialogAliment() {
        try {

            var arrayColors = arrayOf(
                getString(R.string.blood_pressure),
                getString(R.string.pulse_rate),
                getString(R.string.weight),
                getString(R.string.body_composition),
                getString(R.string.respiratory_rate),
                getString(R.string.blood_sugar)
            )

            lateinit var dialog: AlertDialog

            val builder = AlertDialog.Builder(this)

            builder.setTitle("Select Category")

            builder.setSingleChoiceItems(arrayColors, selectedVitalIndex) { _, which ->

                selectedVitalIndex = which
                selectedVital.text = arrayColors[which]
                selectedVitalName = arrayColors[which]
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

    private fun loginApi() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.LoginResponseModel> =
                apiService.getVitalAPI(
                    "1","1", RequestModel.getVitalDetailRequestModel(
                        "1", "Pulse", startDate.text.toString(),endDate.text.toString()
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.LoginResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.LoginResponseModel>,
                    response: Response<ResponseModelClasses.LoginResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response:", response.body().toString())
                        if (response.body() != null) {
                            if (response.body()!!.status == "fail") {
                                showSuccessPopup(response.body()!!.message)
                            } else {

                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.LoginResponseModel>,
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

    }