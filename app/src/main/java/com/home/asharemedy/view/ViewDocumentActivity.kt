package com.home.asharemedy.view

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import com.home.asharemedy.R
import com.home.asharemedy.adapter.MyReportsListAdapter
import com.home.asharemedy.base.BaseActivity
import kotlinx.android.synthetic.main.activity_my_records.*
import kotlinx.android.synthetic.main.activity_my_records.bottomBar
import kotlinx.android.synthetic.main.activity_my_records.topbar
import kotlinx.android.synthetic.main.activity_view_document.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*

class ViewDocumentActivity : BaseActivity() {

    var adapter: MyReportsListAdapter? = null
    var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_my_records)
            initView()
            checkOnClick()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
        try {
            topbar.screenName.text = getString(R.string.my_records)
            bundle = intent.getExtras()
            var fileContent = bundle!!.getString("fileContent")

            val imageBytes = Base64.decode(fileContent, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            imageDocument.setImageBitmap(decodedImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkOnClick() {

        try {
            topbar.imageBack.setOnClickListener {
                finish()
            }

            bottomBar.layoutSettings.setOnClickListener {
                logoutAlertDialog()
            }
            bottomBar.layoutHome.setOnClickListener {
                startActivity(
                    Intent(
                        this@ViewDocumentActivity,
                        DashboardActivity::class.java
                    )
                )
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}