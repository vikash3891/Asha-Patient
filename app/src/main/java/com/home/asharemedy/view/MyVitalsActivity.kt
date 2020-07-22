package com.home.asharemedy.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.home.asharemedy.R
import kotlinx.android.synthetic.main.activity_my_vitals.*
import kotlinx.android.synthetic.main.topbar_layout.view.*

class MyVitalsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_vitals)

        topbar.screenName.text = getString(R.string.my_vitals)
        topbar.imageBack.setOnClickListener {
            finish()
        }
        selectedVital.setOnClickListener {
            showDialogAliment()
        }
        initView()
    }

    fun initView()
    {

    }

    fun showDialogAliment() {
        try {
            lateinit var dialog: AlertDialog
            val arrayColors = arrayOf(
                getString(R.string.blood_pressure),
                getString(R.string.pulse_rate),
                getString(R.string.weight),
                getString(R.string.body_composition),
                getString(R.string.respiratory_rate),
                getString(R.string.blood_sugar)
            )
            val arrayChecked = booleanArrayOf(false, false, false, false, false, false)
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Select Category")

            builder.setSingleChoiceItems(arrayColors, -1, { _, which ->

                selectedVital.text = arrayColors[which]
                dialog.dismiss()
            })

            builder.setPositiveButton("OK") { _, _ ->
                // Do something when click positive button
                //text_view.text = "Your preferred colors..... \n"
                for (i in 0 until arrayColors.size) {
                    val checked = arrayChecked[i]
                    if (checked) {
                        var str = "${selectedVital.text} ${arrayColors[i]},"
                        selectedVital.text = str.dropLast(1)
                    }
                }
            }

            dialog = builder.create()
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}