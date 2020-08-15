package com.home.asharemedy.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.home.asharemedy.R
import com.home.asharemedy.adapter.AddMedicalRecordAdapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.RequestModel
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.chat.utils.getFilePath
import com.home.asharemedy.model.DashboardGridModel
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_add_medical_record.*
import kotlinx.android.synthetic.main.activity_dashboard.bottomBar
import kotlinx.android.synthetic.main.activity_dashboard.gvDashboard
import kotlinx.android.synthetic.main.activity_dashboard.topbar
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AddMedicalRecordActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        category = reportType[position]
    }

    var adapter: AddMedicalRecordAdapter? = null
    var foodsList = ArrayList<DashboardGridModel>()
    val REQUEST_CODE = 100
    val DOCUMENT_REQUEST_CODE = 111
    val CAMERA_REQUEST_CODE = 200
    var appointmentID = ""
    var category = ""
    var storageLink = ""
    var fileContent = ""

    var reportType =
        arrayOf("CT/MRI Report", "X-Ray Report", "Blood Reports", "Prescription", "Other Reports")

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
        try {
            topbar.screenName.text = getString(R.string.dashboard)
            topbar.imageBack.visibility = View.GONE
            setupPermissions()

            spinnerHabit.onItemSelectedListener = this@AddMedicalRecordActivity

            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, reportType)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerHabit.adapter = aa
            loadList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadList() {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
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
            uploadAttachment.setOnClickListener {
                if (appointmentID.isNotEmpty() && category.isNotEmpty() && storageLink.isNotEmpty() && fileContent.isNotEmpty()) {
                    addRecordApi()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
                layoutPreview.visibility = View.VISIBLE
                imagePreview.setImageBitmap(data!!.extras?.get("data") as Bitmap)
            }
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
                layoutPreview.visibility = View.VISIBLE
                imagePreview.setImageURI(data?.data) // handle chosen image

                Log.d("FilePath", getFilePath(applicationContext, data?.data!!))
                Log.d("FileBase64", Utils.encoder(getFilePath(applicationContext, data.data!!)!!))
                Utils.fileUploadBase64 =
                    Utils.encoder(getFilePath(applicationContext, data.data!!)!!)
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

    private fun setupPermissions() {
        try {
            val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission", "Permission to record denied")
                makeRequest()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            101
        )
    }

    private fun addRecordApi() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.LoginResponseModel> =
                apiService.getHabit(
                    AppPrefences.getUserID(this),
                    Utils.getJSONRequestBodyAny(
                        RequestModel.getRecordUploadRequestModel(
                            appointmentID, category, storageLink, fileContent
                        )
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
                        if (response.code() == 400) {
                            Log.v("Error code 400", response.errorBody().toString())
                        }
                        if (response.body() != null) {
                            if (response.body()!!.message == "fail") {
                                showSuccessPopup(response.body()!!.message)
                            } else {

                                showSuccessPopup("Habit added Successfully.")
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

}