package com.home.asharemedy.api

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.home.asharemedy.model.LoginModel
import com.home.asharemedy.utils.AppPrefences
import com.home.asharemedy.utils.Constants

object RequestModel {

    /*-------Login-------*/
    fun getLoginRequestModel(
        userid: String,
        password: String
    ): HashMap<String, String> {
        var map = HashMap<String, String>()

        map.put(Constants.USERNAME, userid)
        map.put(Constants.PASSWORD, password)
        map.put("usertype", "patients")
        Log.d("Login Request: ", "" + Gson().toJson(map))
        return map;
    }

    /*-------Habit-------*/
    fun getHabitRequestModel(
        habit_name: String,
        habit_frequency: Int,
        habit_frequency_unit: String,
        status: String
    ): HashMap<String, Any> {
        var map = HashMap<String, Any>()

        map.put(Constants.HABIT_NAME, habit_name)
        map.put(Constants.HABIT_FREQUENCY, habit_frequency)
        map.put(Constants.HABIT_FREQUENCY_UNIT, habit_frequency_unit)
        map.put(Constants.STATUS, status)
        Log.d("Habit Request: ", "" + Gson().toJson(map))
        return map;
    }

    fun getVitalDetailRequestModel(
        patientId: String,
        vitalName: String,
        startDate: String,
        endDate: String
    ): Map<String, String> {
        var map = HashMap<String, String>()

        map.put(Constants.PATIENT_ID, patientId)
        map.put(Constants.VITAL_NAME, vitalName)
        map.put(Constants.START_DATE, startDate)
        map.put(Constants.END_DATE, endDate)
        Log.d("VitalDetailRequest: ", "" + Gson().toJson(map))
        return map;
    }


    fun setVitalRequestModel(
        vital_date: String,
        vital_reading: String,
        vital_unit: String
    ): HashMap<String, Any> {
        var map = HashMap<String, Any>()

        map.put(Constants.VITAL_DATE, vital_date)
        map.put(Constants.VITAL_READING, arrayOf(vital_reading))
        map.put(Constants.VITAL_UNIT, vital_unit)
        Log.d("VitalUpdateRequest: ", "" + Gson().toJson(map))
        return map;
    }


    fun getRegistrationRequestModel(
        patient_name: String,
        patient_dob: String,
        patient_gender: String,
        patient_email: String,
        patient_mobile: String,
        patient_password: String,
        patient_refcode: String,
        city: String,
        state: String,
        country: String,
        address: String,
        street: String,
        pincode: String
    ): HashMap<String, String> {

        var map = HashMap<String, String>()
        map.put(Constants.PATIENT_NAME, patient_name)
        map.put(Constants.PATIENT_DOB, patient_dob)
        map.put(Constants.PATIENT_GENDER, patient_gender.toLowerCase())
        map.put(Constants.PATIENT_EMAIL, patient_email)
        map.put(Constants.PATIENT_MOBILE, patient_mobile)
        map.put(Constants.PATIENT_PASSWORD, patient_password)
        map.put(Constants.PATIENT_REFCODE, patient_refcode)
        map.put(Constants.PATIENT_CITY, city)
        map.put(Constants.PATIENT_STATE, state)
        map.put(Constants.PATIENT_COUNTRY, country)
        map.put(Constants.PATIENT_ADDRESS1, address)
        map.put(Constants.PATIENT_ADDRESS2, street)
        map.put(Constants.PATIENT_PINCODE, pincode)


        Log.d("Reg Request: ", "" + Gson().toJson(map))
        return map;
    }

    fun getUpdateProfileRequestModel(
        context: Context, data: ResponseModelClasses.GetPatientProfileResponseModel
    ): HashMap<String, Any> {

        var map = HashMap<String, Any>()

        map.put(Constants.PATIENT_NAME, data.patient_name)
        map.put(Constants.PATIENT_DOB, data.patient_dob)
        map.put(Constants.PATIENT_GENDER, data.patient_gender)
        map.put(Constants.PATIENT_MOBILE, data.patient_mobile)
        map.put(Constants.PATIENT_PASSWORD, AppPrefences.getPassword(context))
        map.put(Constants.PATIENT_ADDRESS1, data.patient_address1)
        map.put(Constants.PATIENT_ADDRESS2, data.patient_address2)
        map.put(Constants.PATIENT_CITY, data.patient_city)
        map.put(Constants.PATIENT_STATE, data.patient_state)
        map.put(Constants.PATIENT_PINCODE, "12345")
        map.put(Constants.PATIENT_COUNTRY, data.patient_country)
        map.put(Constants.PATIENT_REFCODE, "AE9384SD")
        map.put(Constants.MEMBERSHIP_NUMBER, "cdj23727Jn")
        map.put(Constants.EMERGENCY_CONTACT_NAME, "John Doe")
        map.put(Constants.EMERGENCY_CONTACT_RELATION, "Father")
        map.put(Constants.EMERGENCY_CONTACT_NUMBER, "911234567890")
        map.put(Constants.PHOTO, "string")
        //map.put(Constants.ALLERGIES, data.allergies)
        map.put(Constants.INSURED, true)
        map.put(Constants.INSURANCE_COMPANY_NAME, "ABC")
        map.put(Constants.INSURANCE_VALIDITY, "2020-08-03")
        map.put(Constants.PRIMARY_HEALTH_ISSUE, "XYZ")
        map.put(Constants.PARENT_ID, 1)
        map.put(Constants.VERIFIED, true)
        map.put(Constants.VERIFICATION_TOKEN, "dfsgasdgsertgszdf66fdga1dg51a6dga21d1g65adfg")
        map.put(Constants.VERIFICATION_TOKEN_EXPIRY, "2020-08-16 01:44:00")
        map.put(Constants.STATUS, "active")

        Log.d("UpProfileReq.: ", "" + Gson().toJson(map))
        return map;
    }

    fun getRecordUploadRequestModel(
        appointmentID: String,
        category: String,
        storageLink: String,
        fileContent: String
    ): HashMap<String, Any> {
        var map = HashMap<String, Any>()

        map.put(Constants.APPOINTMENT_ID, appointmentID)
        map.put(Constants.CATEGORY, category)
        map.put(Constants.STORAGE_LINK, storageLink)
        map.put(Constants.FILE_CONTENT, fileContent)
        Log.d("RecordUploadRequest: ", "" + Gson().toJson(map))
        return map;
    }

    fun getForgotPasswordRequestModel(
        emailID: String
    ): HashMap<String, Any> {
        var map = HashMap<String, Any>()

        map.put(Constants.EMAIL, emailID)
        Log.d("ForgotPasswordRequest: ", "" + Gson().toJson(map))
        return map;
    }
}