package com.home.asharemedy.view

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.home.asharemedy.R
import com.home.asharemedy.adapter.MyReportsListAdapter
import com.home.asharemedy.api.ApiClient
import com.home.asharemedy.api.ApiInterface
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_my_records.*
import kotlinx.android.synthetic.main.activity_my_records.bottomBar
import kotlinx.android.synthetic.main.activity_my_records.topbar
import kotlinx.android.synthetic.main.activity_view_document.*
import kotlinx.android.synthetic.main.bottombar_layout.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewDocumentActivity : BaseActivity() {

    var adapter: MyReportsListAdapter? = null
    var bundle: Bundle? = null
    var myReportList: ResponseModelClasses.GetMyRecordItemResponseModel.TableData? = null
    var medical_record_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_view_document)
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
            medical_record_id = bundle!!.getString("medical_record_id")!!

            getMedicalReports()
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

    private fun getMedicalReports() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            Log.d("RequestData: ", AppPrefences.getUserID(this) + " " + medical_record_id)
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetMyRecordItemResponseModel> =
                apiService.getMedicalRecordByID(AppPrefences.getUserID(this), medical_record_id)
            call.enqueue(object :
                Callback<ResponseModelClasses.GetMyRecordItemResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetMyRecordItemResponseModel>,
                    response: Response<ResponseModelClasses.GetMyRecordItemResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response: ", response.body().toString())
                        if (response.body() != null) {
                            myReportList = response.body()!!.data
                            Log.d(
                                "file_content.length: ",
                                myReportList!!.file_content.length.toString()
                            )
                            val imageBytes =
                                Base64.decode(myReportList!!.file_content, Base64.DEFAULT)

                            /* .docx, .pdf, .txt */
                            val decodedImage =
                                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            imageDocument.setImageBitmap(decodedImage)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.GetMyRecordItemResponseModel>,
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