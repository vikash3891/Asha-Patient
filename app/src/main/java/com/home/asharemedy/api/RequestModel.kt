package com.home.asharemedy.api

import android.util.Log
import com.google.gson.Gson
import com.home.asharemedy.model.LoginModel
import com.home.asharemedy.utils.Constants

object RequestModel {

    /*-------Login-------*/
    fun getLoginRequestModel(
        userid: String,
        password: String
    ): Map<String, String> {
        var map = HashMap<String, String>()

        map.put(Constants.MOBILE, userid)
        map.put(Constants.PASSWORD, password)
        Log.d("Login Request: ", "" + Gson().toJson(map))
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
        country: String
    ): Map<String, String> {

        var map = HashMap<String, String>()
        map.put(Constants.PATIENT_NAME, patient_name)
        map.put(Constants.PATIENT_DOB, patient_dob)
        map.put(Constants.PATIENT_GENDER, patient_gender)
        map.put(Constants.PATIENT_EMAIL, patient_email)
        map.put(Constants.PATIENT_MOBILE, patient_mobile)
        map.put(Constants.PATIENT_PASSWORD, patient_password)
        map.put(Constants.PATIENT_REFCODE, patient_refcode)
        //map.put(Constants.CITY, city)
        //map.put(Constants.STATE, state)
       // map.put(Constants.COUNTRY, country)

        Log.d("Reg Request: ", "" + Gson().toJson(map))
        return map;
    }

    fun getPlaceOrderCartRequestModel(
        orderID: String
    ): Map<String, String> {

        val map = HashMap<String, String>()

        map.put("card_value", orderID)


        Log.d("Reg Request: ", "" + Gson().toJson(map))
        return map;
    }

    fun getUpdateProfileRequestModel(
        password: String, item: LoginModel
    ): Map<String, String> {

        var map = HashMap<String, String>()
        map.put(Constants.NAME, item.name!!)
        map.put(Constants.MOBILE, item.mobile!!)
        map.put(Constants.PASSWORD, password)
        map.put(Constants.EMAIL, item.email!!)
        map.put(Constants.ADDRESS, item.address.orEmpty())
        map.put(Constants.ZIP, item.pin.orEmpty())
        map.put(Constants.STATE, item.state!!)
        map.put(Constants.CITY, item.city!!)
        map.put(Constants.CREATED_DATE, "")

        Log.d("ProUpdReq.: ", "" + Gson().toJson(map))
        return map;
    }
}