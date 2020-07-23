package com.home.asharemedy.utils

class Constants {

    companion object {
        /*http://104.215.179.29/v2/patients/{patientId}/vitals/{vitalName}*/
        const val BASE_URL =
            "http://104.215.179.29/v2/"

        const val PATIENT_REG = "patients/"
        const val REQUEST_TIMEOUT_DURATION = 10
        const val REGISTRATION_USER = "registration/user"
        const val LOGIN_USER = "api/loginuser/"
        const val GET_ALL_PRODUCTS = "api/product/"
        const val GET_ORDERID = "order/add/"
        const val GET_ORDER_ADD_CART = "order/addcart/"
        const val GET_ORDER_HISTORY = "order/listorder/"
        const val NAME = "name"
        const val EMAIL = "email"
        const val MOBILE = "mobile"
        const val PASSWORD = "password"
        const val ADDRESS = "address"
        const val ZIP = "zip"
        const val STATE = "state"
        const val CITY = "city"
        const val CREATED_DATE = "create_date"
        const val PATIENT_ID = "patientId"
        const val VITAL_NAME = "vitalName"
        const val START_DATE = "startDate"
        const val END_DATE = "endDate"

        const val PATIENT_NAME = "patient_name"
        const val PATIENT_DOB = "patient_dob"
        const val PATIENT_GENDER = "patient_gender"
        const val PATIENT_EMAIL = "patient_email"
        const val PATIENT_MOBILE = "patient_mobile"
        const val PATIENT_PASSWORD = "patient_password"
        const val PATIENT_REFCODE = "patient_refcode"
        const val COUNTRY = "COUNTRY"


    }
}