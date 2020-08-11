package com.home.asharemedy.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.home.asharemedy.R
import com.home.asharemedy.adapter.AddMedicalRecordAdapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.databinding.ActivityDashboardBinding
import com.home.asharemedy.model.DashboardGridModel
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_add_medical_record.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_dashboard.bottomBar
import kotlinx.android.synthetic.main.activity_dashboard.gvDashboard
import kotlinx.android.synthetic.main.activity_dashboard.topbar
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.item_profile_details.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMedicalRecordActivity : BaseActivity() {

    var adapter: AddMedicalRecordAdapter? = null
    var foodsList = ArrayList<DashboardGridModel>()
    private lateinit var viewDataBinding: ActivityDashboardBinding
    val REQUEST_CODE = 100
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

        // getPatientProfile()

        for (i in 0..6) {
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
                finish()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE && data != null){
                layoutPreview.visibility = View.VISIBLE
                imagePreview.setImageBitmap(data.extras?.get("data") as Bitmap)
            }
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
                layoutPreview.visibility = View.VISIBLE
                imagePreview.setImageURI(data?.data) // handle chosen image
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /* private fun getPatientProfile() = if (Utils.isConnected(this)) {
         showDialog()
         try {
             val apiService =
                 ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
             val call: Call<ResponseModelClasses.GetPatientProfileResponseModel> =
                 apiService.getPatientProfile("15")//AppPrefences.getUserID(this))
             call.enqueue(object : Callback<ResponseModelClasses.GetPatientProfileResponseModel> {
                 override fun onResponse(
                     call: Call<ResponseModelClasses.GetPatientProfileResponseModel>,
                     response: Response<ResponseModelClasses.GetPatientProfileResponseModel>
                 ) {
                     try {
                         dismissDialog()
                         Log.d("Response: ", response.body().toString())
                         if (response.body() != null) {
                             updateView(response.body()!!)
                         }
                     } catch (e: Exception) {
                         e.printStackTrace()
                     }
                 }

                 override fun onFailure(
                     call: Call<ResponseModelClasses.GetPatientProfileResponseModel>,
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
     }*/

    /*fun updateView(data: ResponseModelClasses.GetPatientProfileResponseModel) {
        profileDetails.userName.text = data.patient_name
        profileDetails.patientID.text = data.patient_id
        profileDetails.dobValue.text = data.patient_dob
        profileDetails.brandPartnerName.text = data.insurance_company_name
        profileDetails.phoneNumberValue.text = data.patient_mobile
        profileDetails.emailValue.text = data.patient_email
        profileDetails.emergencyContactValue.text = data.emergency_contact_number

    }
*/
}