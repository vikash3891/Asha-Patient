package com.home.asharemedy.utils

class Constants {

    companion object {
        /*http://104.215.179.29/v2/patients/{patientId}/vitals/{vitalName}*/
        const val BASE_URL =
            "http://104.215.179.29/v1/"

        const val PATIENT_REG = "patients/"
        const val PATIENT_REGISTRATION = "patients"
        const val HABIT = "/habits"
        const val MEDICAL_REPORTS = "/medicalReports"
        const val CARE_PLANS = "/careplans"
        const val MEDICATIONS = "/medications"
        const val VITALS = "/vitals/"
        const val AILMENTS = "ailments"
        const val SERVICES = "services"
        const val FACILITIES = "facilities?"
        const val DOCTOR_LIST = "doctors?"//ailmentId=0&city=Bangalore"//
        const val DOCTOR_SLOT_LIST = "doctors"//
        const val SLOT_SLOTDATE = "/slots?slotDate="//
        const val REQUEST_TIMEOUT_DURATION = 10
        const val REGISTRATION_USER = "registration/user"
        const val LOGIN_USER = "sessions"
        const val APPOINTMENTS = "appointments?"
        const val PAYMENT_HISTORY = "payments?"//5

        const val NAME = "name"
        const val EMAIL = "email"
        const val MOBILE = "mobile"
        const val USERNAME = "username"
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
        const val PATIENT_COUNTRY = "patient_country"
        const val PATIENT_CITY = "patient_city"
        const val PATIENT_STATE = "patient_state"
        const val PATIENT_PINCODE = "patient_pincode"
        const val PATIENT_ADDRESS1 = "patient_address1"
        const val PATIENT_ADDRESS2 = "patient_address2"

        const val MERCHANT_KEY = "CLAMB2ZM"
        const val MERCHANT_SALT = "DEWA5PQd0s"
        const val MERCHANT_EMAIL = "shubh24aug.work@gmail.com"


    }
}