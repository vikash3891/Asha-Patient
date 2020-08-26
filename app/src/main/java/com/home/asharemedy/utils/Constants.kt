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
        const val PAYMENT_HISTORY = "payments?"
        const val PAYMENTS = "payments"

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
        const val PATIENTID = "patientId"
        const val VITAL_NAME = "vitalName"
        const val START_DATE = "startDate"
        const val END_DATE = "endDate"

        const val VITAL_DATE = "vital_date"
        const val VITAL_READING = "vital_reading"
        const val VITAL_UNIT = "vital_unit"

        const val PATIENT_NAME = "patient_name"
        const val PATIENT_DOB = "patient_dob"
        const val PATIENT_GENDER = "patient_gender"
        const val PATIENT_EMAIL = "patient_email"
        const val PATIENT_MOBILE = "patient_mobile"
        const val PATIENT_PASSWORD = "patient_password"
        const val PATIENT_REFCODE = "patient_refcode"
        const val PATIENT_ADDRESS1 = "patient_address1"
        const val PATIENT_ADDRESS2 = "patient_address2"
        const val PATIENT_CITY = "patient_city"
        const val PATIENT_STATE = "patient_state"
        const val PATIENT_COUNTRY = "patient_country"
        const val PATIENT_PINCODE = "patient_pincode"
        const val MEMBERSHIP_NUMBER = "membership_number"
        const val EMERGENCY_CONTACT_NAME = "emergency_contact_name"
        const val EMERGENCY_CONTACT_RELATION = "emergency_contact_relation"
        const val EMERGENCY_CONTACT_NUMBER = "emergency_contact_number"

        const val PHOTO = "photo"
        const val ALLERGIES = "allergies"
        const val INSURED = "insured"
        const val INSURANCE_COMPANY_NAME = "insurance_company_name"
        const val INSURANCE_VALIDITY = "insurance_validity"
        const val PRIMARY_HEALTH_ISSUE = "primary_health_issue"
        const val PARENT_ID = "parent_id"
        const val VERIFIED = "verified"
        const val VERIFICATION_TOKEN = "verification_token"
        const val VERIFICATION_TOKEN_EXPIRY = "verification_token_expiry"

        const val MERCHANT_KEY = "CLAMB2ZM"
        const val MERCHANT_SALT = "DEWA5PQd0s"
        const val MERCHANT_EMAIL = "shubh24aug.work@gmail.com"

        const val HABIT_NAME = "habit_name"
        const val HABIT_FREQUENCY = "habit_frequency"
        const val HABIT_FREQUENCY_UNIT = "habit_frequency_unit"
        const val STATUS = "status"

        const val APPOINTMENT_ID = "appointment_id"
        const val CATEGORY = "category"
        const val STORAGE_LINK = "storage_link"
        const val FILE_CONTENT = "file_content"

        const val FAQ = "https://www.ashacares.com/faq"
        const val TERMS_AND_CONDITION = "https://www.ashacares.com/terms-of-use"
        const val PRIVACY_POLICY = "https://www.ashacares.com/privacy-policy"

        /*https://www.ashacares.com/forgot-password*/

        const val FORGOT_PASSWORD = "forgot-password"

        const val TRANSACTION_ID = "transanction_id"
        const val AMOUNT = "amount"
        const val CGST_PERCENT = "cgst_percentage"
        const val SGST_PERCENT = "sgst_percentage"
        const val IGST_PERCENT = "igst_percentage"
        const val GROSS_TOTAL = "gross_total"
        const val DISCOUNT_PERCENT = "discount_percentage"
        const val CONVENIENCE_FEE = "convenience_fee"
        const val PAYER_ID = "payer_id"
        const val PAYER_TYPE = "payer_type"
        const val RECEIVER_ID = "receiver_id"
        const val RECEIVER_TYPE = "receiver_type"

        /* patient_id, doctor_slot_id, payment_id,purpose*/
        const val PATIENT_ID = "patient_id"
        const val DOCTOR_SLOT_ID = "doctor_slot_id"
        const val PAYMENT_ID = "payment_id"
        const val PURPOSE = "purpose"
        const val USER_ID = "userId"
        const val USER_TYPE = "userType"

        const val MEDICATION_TYPE = "medication_type"
        const val DRUG_NAME = "drug_name"
        const val DOSAGE_INSTRUCTIONS = "dosage_instructions"
        const val DAYS = "days"
        const val DOSE_PER_DAY = "dose_per_day"
        const val OTHER_INSTRUCTION = "other_instruction"
    }
}