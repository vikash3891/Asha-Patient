package com.home.asharemedy.utils

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.model.ListItemModel
import com.home.asharemedy.model.LoginModel
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object Utils {

    private val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    var isRegisterSuccess = false

    private var orderHistoryList =
        ArrayList<ResponseModelClasses.GetOrderHistoryResponseModel.TableData1>()
    private var userDetails = ArrayList<LoginModel>()
    private var selectedItem = ListItemModel()
    var cartTotalAmount = 0.0
    var cartTotalItem = 0
    var selectedCategory = 0
    var isOrderToCart = false
    var selectedOrderID = ""
    var isAilmentOrService = true
    var selectedDoctorFacitiyID = ""

    var appointmentSlotList = java.util.ArrayList<ResponseModelClasses.GetSlotListResponseModel>()
    var doctorFacilityList = ArrayList<ResponseModelClasses.GetFacilityListResponseModel>()

    /*
    For Checking the Internet Connectivity
     */
    fun isConnected(context: Context): Boolean {
        val cm =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    /*
    For Checking Permission While accessing Gallery
     */
    fun checkPermission(context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    )
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    /*
    Hide KeyBoard
     */
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            activity.findViewById<View>(android.R.id.content).getWindowToken(),
            0
        )
    }

    fun showProgressDialog1(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        try {
            progressDialog.show()
            if (progressDialog.window != null) {
                progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            progressDialog.setContentView(R.layout.progress_layout)
            progressDialog.isIndeterminate = true
            progressDialog.setCancelable(false)
            progressDialog.setCanceledOnTouchOutside(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return progressDialog
    }

    fun convertDateFormat(oldFormat: String, newFormat: String, dateString: String): String {
        var sdf = SimpleDateFormat(oldFormat)
        try {
            val date = sdf.parse(dateString)
            sdf = SimpleDateFormat(newFormat)
            return sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }


    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun convertDateFormatWithSuffix(oldFormat: String, dateString: String): String {
        var sdf = SimpleDateFormat(oldFormat)
        try {
            val date = sdf.parse(dateString)
            val day = date.date
            val dayWithSuffix = getDayNumberSuffix(day)
            sdf = SimpleDateFormat(" dd'$dayWithSuffix' MMMM yyyy")
            return sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun getDayNumberSuffix(day: Int): String {
        if (day in 11..13) {
            return "th"
        }
        return when (day % 10) {
            1 -> "st"

            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    fun setUserDetails(item: LoginModel) {
        userDetails.clear()
        userDetails.add(item)
    }

    fun getUserDetails(): ArrayList<LoginModel> {
        return userDetails
    }


    fun setMonth(monthOfYear: Int): String {
        when (monthOfYear) {
            1 -> return "Jan"
            2 -> return "Feb"
            3 -> return "Mar"
            4 -> return "Apr"
            5 -> return "May"
            6 -> return "Jun"
            7 -> return "Jul"
            8 -> return "Aug"
            9 -> return "Sep"
            10 -> return "Oct"
            11 -> return "Nov"
            12 -> return "Dec"
            else -> { // Note the block
                return "July"
            }
        }
    }

    fun getTime(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        println("Current Time is: $date.time")
        return inputFormat.format(date.time)
    }

    fun getDate(): String {
        /*val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        println("Current Date is: $date.date")
        return inputFormat.format(date.time)*/

        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DATE)
        val month = setMonth(cal.get(Calendar.MONTH) + 1)
        val year = cal.get(Calendar.YEAR)
        return "$month $day, $year"
    }

    fun getJSONRequestBody(stringHashMap: HashMap<String, String>?): RequestBody {
        val jsonObject = JSONObject()
        if (stringHashMap != null && stringHashMap.size > 0) {
            try {
                for ((key, value) in stringHashMap) {
                    jsonObject.put(key, value)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return jsonObject.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }

    fun getJSONRequestBodyAny(stringHashMap: HashMap<String, Any>?): RequestBody {
        val jsonObject = JSONObject()
        if (stringHashMap != null && stringHashMap.size > 0) {
            try {
                for ((key, value) in stringHashMap) {
                    jsonObject.put(key, value)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return jsonObject.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }

}
