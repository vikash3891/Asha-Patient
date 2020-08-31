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
import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.home.asharemedy.AshaRemedyApp
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.model.ListItemModel
import com.home.asharemedy.model.LoginModel
import com.home.asharemedy.video.utils.getString
import com.home.asharemedy.view.CheckListActivity
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object Utils {

    private const val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    var isRegisterSuccess = false

    private var userDetails = ArrayList<LoginModel>()

    var isAilmentOrService = true
    var selectedDoctorFacitiyID = ""
    var selectedDoctorFacility: ResponseModelClasses.GetFacilityListResponseModel.TableData1? = null
    var fileUploadBase64 = ""
    var userfileUploadBase64 = ""
    var selectedHealthIssues = ""

    var appointmentSlotList = ArrayList<ResponseModelClasses.GetSlotListResponseModel>()
    var doctorFacilityList =
        ArrayList<ResponseModelClasses.GetFacilityListResponseModel.TableData1>()
    var selectedAppointmentDetails =
        ArrayList<ResponseModelClasses.GetMyAppointmentsResponseModel.TableData4>()

    var profileData: ResponseModelClasses.GetPatientProfileResponseModel? = null

    var selectedAilmentOrServiceName = ""

    var selectedGridList = ArrayList<ResponseModelClasses.GetSlotListResponseModel>()

    val teachers: Array<SpiritualTeacher>
        get() =
            arrayOf(
                SpiritualTeacher("Diabetes", false), SpiritualTeacher("Heart Diseases", false),
                SpiritualTeacher("Hypertension", false), SpiritualTeacher("Allergies", false),
                SpiritualTeacher("Asthma", false), SpiritualTeacher("Others", false)
            )

    class SpiritualTeacher(var name: String?, var isSelected: Boolean) {

    }

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

    var arrayColors = arrayOf(
        getString(R.string.temperature),
        getString(R.string.blood_pressure),
        getString(R.string.pulse),
        getString(R.string.height),
        getString(R.string.weight),
        getString(R.string.respiration_rate),
        getString(R.string.calories_burned),
        getString(R.string.blood_sugar),
        getString(R.string.oxygen_saturation)
    )

    fun getTime(): String {
        val inputFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())
        val date = Date()
        println("Current Time is: $date.time")
        return inputFormat.format(date.time)
    }

    fun getDate(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        println("Current Date is: $date.date")
        return inputFormat.format(date.time)

        /* val cal = Calendar.getInstance()
         val day = cal.get(Calendar.DATE)
         val month = setMonth(cal.get(Calendar.MONTH) + 1)
         val year = cal.get(Calendar.YEAR)
         return "$month $day, $year"*/
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

    /*Kotlin Encode File/Image to Base64*/
    fun encoder(filePath: String): String {
        val bytes = File(filePath).readBytes()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(bytes)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    /* Kotlin Decode Base64 to File/Image*/
    fun decoder(base64Str: String, pathFile: String): Unit {
        val imageByteArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getDecoder().decode(base64Str)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        File(pathFile).run {
            writeBytes(imageByteArray)
        }
    }

    fun getString(@StringRes stringId: Int): String {
        return AshaRemedyApp.getInstance().getString(stringId)
    }

    class InputFilterMinMax(min: Int, max: Int) : InputFilter {
        private var min: Int = 0
        private var max: Int = 0

        init {
            this.min = min
            this.max = max
        }

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            try {
                val input = (dest.subSequence(0, dstart).toString() + source + dest.subSequence(
                    dend,
                    dest.length
                )).toInt()
                if (isInRange(min, max, input))
                    return null
            } catch (nfe: NumberFormatException) {
            }
            return ""
        }

        private fun isInRange(a: Int, b: Int, c: Int): Boolean {
            return if (b > a) c in a..b else c in b..a
        }
    }

    fun addSlot(item: ResponseModelClasses.GetSlotListResponseModel) {
        try {

            selectedGridList.add(item)

            /*if (foodsList.contains(item)) {
                var itemPosition = foodsList.indexOf(item)
                foodsList.get(itemPosition).quantity = (foodsList[itemPosition].quantity!! + 1)

            } else {
                foodsList.add(item)
            }*/

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun removeSlot(item: ResponseModelClasses.GetSlotListResponseModel) {
        try {
            selectedGridList.remove(item)

            /*if (foodsList.contains(item)) {
                val itemPosition = foodsList.indexOf(item)
                foodsList[itemPosition].quantity = (foodsList[itemPosition].quantity!! - 1)

            } else {
                foodsList.remove(item)
            }*/
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
