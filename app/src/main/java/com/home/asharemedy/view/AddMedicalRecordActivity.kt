package com.home.asharemedy.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.home.asharemedy.R
import com.home.asharemedy.adapter.AddMedicalRecordAdapter
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.databinding.ActivityDashboardBinding
import com.home.asharemedy.model.DashboardGridModel
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_add_medical_record.*
import kotlinx.android.synthetic.main.activity_dashboard.bottomBar
import kotlinx.android.synthetic.main.activity_dashboard.gvDashboard
import kotlinx.android.synthetic.main.activity_dashboard.topbar
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import java.io.IOException
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream


class AddMedicalRecordActivity : BaseActivity() {

    var adapter: AddMedicalRecordAdapter? = null
    var foodsList = ArrayList<DashboardGridModel>()
    private lateinit var viewDataBinding: ActivityDashboardBinding
    val REQUEST_CODE = 100
    val DOCUMENT_REQUEST_CODE = 111
    val CAMERA_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medical_record)

        try {
            initView()
            checkClicks()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
        topbar.screenName.text = getString(R.string.dashboard)
        topbar.imageBack.visibility = View.GONE

        loadList()
    }

    private fun loadList() {
        for (i in 0..1) {
            foodsList.add(
                DashboardGridModel(
                    "Document " + i + 1,
                    R.drawable.doc_icon
                )
            )
        }

        adapter = AddMedicalRecordAdapter(this, foodsList)

        gvDashboard.adapter = adapter
    }

    private fun checkClicks() {
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
                        this@AddMedicalRecordActivity,
                        DashboardActivity::class.java
                    )
                )
                finish()
            }

            layoutCamera.setOnClickListener {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, REQUEST_CODE)
            }
            layoutGallery.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }
            layoutCloud.setOnClickListener {
                val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE && data != null) {
                layoutPreview.visibility = View.VISIBLE
                imagePreview.setImageBitmap(data.extras?.get("data") as Bitmap)
            }
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
                layoutPreview.visibility = View.VISIBLE
                imagePreview.setImageURI(data?.data) // handle chosen image


                val bm = BitmapFactory.decodeFile("/path/to/image.jpg")
                val baos = ByteArrayOutputStream()
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object
                val b = baos.toByteArray()

                val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)

                /* var fileBase64 = Utils.encoder(data?.data.toString())*/
                Log.d("FileBase64", encodedImage)
            }
            if (resultCode == Activity.RESULT_OK && requestCode == DOCUMENT_REQUEST_CODE) {
                val selectedFile = data?.data //The uri with the location of the file

                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        this@AddMedicalRecordActivity.contentResolver,
                        selectedFile
                    )
                    val thumbBitmap = ThumbnailUtils.extractThumbnail(bitmap, 120, 120)
                    imagePreview.setImageBitmap(thumbBitmap);
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}