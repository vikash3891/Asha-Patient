package com.home.asharemedy.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
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
import com.home.asharemedy.utils.ManagePermissions
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_add_medical_record.*
import kotlinx.android.synthetic.main.activity_dashboard.bottomBar
import kotlinx.android.synthetic.main.activity_dashboard.gvDashboard
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream

class AddMedicalRecordActivity : BaseActivity(), AdapterView.OnItemSelectedListener,
    NavigationView.OnNavigationItemSelectedListener {
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
    private var compressedImageFile: File? = null

    var reportType =
        arrayOf("CT/MRI Report", "X-Ray Report", "Blood Reports", "Prescription", "Other Reports")

    val list = listOf<String>(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

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
            setupToolDrawer()
            topbar.screenName.text = getString(R.string.medical_record)
            topbar.imageBack.visibility = View.GONE
            //setupPermissions()
            managePermissions = ManagePermissions(this,list,PermissionsRequestCode)
            managePermissions.checkPermissions()
            spinnerHabit.onItemSelectedListener = this@AddMedicalRecordActivity

            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, reportType)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerHabit.adapter = aa
            category = reportType[0]
            loadList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Receive the permissions request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PermissionsRequestCode ->{
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(requestCode,permissions,grantResults)

                if(isPermissionsGranted){
                    // Do the task now
                    ///toast("Permissions granted.")
                }else{
                    //toast("Permissions denied.")
                }
                return
            }
        }
    }

    private fun loadList() {
        /* try {
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
         }*/
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
                if (category.isNotEmpty() && storageLink.isNotEmpty() && compressedImageFile != null) {
                    //addRecordApi()
                    uploadProfileImage()
                } else {
                    showSuccessPopup("Please select from provided options.")
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
                storageLink = "capturedImage"
                var imageUri = getImageUriFromBitmap(this, data.extras?.get("data") as Bitmap)
                Utils.fileUploadBase64 =
                    Utils.encoder(getFilePath(applicationContext, imageUri)!!)

                compressedImageFile = File(getFilePath(applicationContext, imageUri)!!)
            }
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
                layoutPreview.visibility = View.VISIBLE
                imagePreview.setImageURI(data?.data) // handle chosen image

                Log.d("FilePath", getFilePath(applicationContext, data?.data!!))
                Log.d("FileBase64", Utils.encoder(getFilePath(applicationContext, data.data!!)!!))
                storageLink = getFilePath(applicationContext, data.data!!)!!
                Utils.fileUploadBase64 =
                    Utils.encoder(getFilePath(applicationContext, data.data!!)!!)
                //compressedImageFile = File(data.data!!.path)
                compressedImageFile = File(getFilePath(applicationContext, data.data!!)!!)
            }
            if (resultCode == Activity.RESULT_OK && requestCode == DOCUMENT_REQUEST_CODE) {
                val selectedFile = data?.data //The uri with the location of the file
                storageLink = getFilePath(applicationContext, data?.data!!)!!
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        this@AddMedicalRecordActivity.contentResolver,
                        selectedFile
                    )
                    val thumbBitmap = ThumbnailUtils.extractThumbnail(bitmap, 120, 120)
                    imagePreview.setImageBitmap(thumbBitmap)
                    //compressedImageFile = File(data.data!!.path)
                    compressedImageFile = File(getFilePath(applicationContext, data.data!!)!!)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    private fun setupPermissions() {
        try {
            val permission = Utils.checkPermission(
                this
            )

            if (permission) {
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
                            appointmentID, category, storageLink, Utils.fileUploadBase64
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

        headerLayout!!.title.text = getString(R.string.medical_record)

        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer_icon)
        toggle.setToolbarNavigationClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    private fun uploadProfileImage() {
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)

            val imageFile =
                compressedImageFile!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val imgBody = MultipartBody.Part.createFormData(
                "file_content",
                compressedImageFile?.name,
                imageFile
            )

            val call = apiService.addProfileImage(
                AppPrefences.getUserID(this),
                category,
                storageLink,
                imgBody
            )
            call.enqueue(object : Callback<ResponseModelClasses.GetUploadRecordResponseModel> {
                override fun onFailure(
                    call: Call<ResponseModelClasses.GetUploadRecordResponseModel>,
                    t: Throwable
                ) {
                    Log.d("Failure: ", t.message)
                }

                override fun onResponse(
                    call: Call<ResponseModelClasses.GetUploadRecordResponseModel>,
                    response: Response<ResponseModelClasses.GetUploadRecordResponseModel>
                ) {
                    try {
                        Log.d("Response: ", response.body()!!.message)
                        showSuccessPopup("Medical Record added Successfully.")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

   }