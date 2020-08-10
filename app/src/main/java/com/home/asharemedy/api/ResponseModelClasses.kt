package com.home.asharemedy.api

import com.home.asharemedy.model.ListItemModel
import com.home.asharemedy.model.LoginModel

object ResponseModelClasses {

    /*-------Registration-------*/
    data class RegistrationResponse(
        val description: String,
        val type: String,
        val message: String
    )

    /*-------Login-------*/
    data class LoginResponseModel(
        val description: String,
        val message: String,
        val type: String,
        val data: ArrayList<LoginModel>
    )

    /*-------Get Order History-------*/
    data class GetOrderHistoryResponseModel(
        val status: String,
        val message: String,
        val data: ArrayList<TableData1>
    ) {
        data class TableData1(
            val id: String,
            val user_id: String,
            val cutomer_name: String,
            val cutomer_email: String,
            val cutomer_phone: String,
            val cutomer_billing_city: String,
            val cutomer_billing_address: String,
            val cutomer_shipping_city: String,
            val cutomer_shipping_address: String,
            val cutomer_billing_state: String,
            val cutomer_shipping_state: String,
            val sub_total: String,
            val total: String,
            val order_date: String,
            val payment_mode: String,
            val payment_status: String,
            val payment_gateway_response: String,
            val cod: String,
            val shipping_method: String,
            val delivery_charge: String,
            val order_status: String,
            val items: ArrayList<TableData2>
        ) {
            data class TableData2(
                val id: String,
                val order_id: String,
                val product_id: String,
                val product_name: String,
                val product_price: String,
                val product_quantity: String,
                val create_date: String
            )
        }
    }

    /*Patient Profile*/
    data class GetPatientProfileResponseModel(
        var age: String,
        var allergies: Any,
        var birth_date: String,
        var emergency_contact_name: String,
        var emergency_contact_number: String,
        var emergency_contact_relation: String,
        var gender: String,
        var insurance_company_name: String,
        var insurance_validity: String,
        var insured: String,
        var membership_number: String,
        var parent_id: String,
        var patient_address1: String,
        var patient_address2: String,
        var patient_city: String,
        var patient_country: String,
        var patient_dob: String,
        var patient_email: String,
        var patient_gender: String,
        var patient_id: String,
        var patient_mobile: String,
        var patient_name: String,
        var patient_refcode: String,
        var photo: String,
        var primary_health_issue: String
    )

    /*Ailment List*/
    data class GetAilmentResponseModel(
        val ailment: String,
        val ailment_id: String
    )

    /*Services List*/
    data class GetServiceResponseModel(
        val service: String,
        val service_id: Int
    )

    /*Facility List*/
    data class GetFacilityListResponseModel(

        val address1: String,
        val address2: String,
        val city: String,
        val country: String,
        val doe: String,
        val dob: String,
        val email: String,
        val facility_id: String,
        val gender: String,
        val name: String,
        val phone: String,
        val pincode: String,
        val state: String,
        val type: String

    )

    /*Facility List*/
    data class GetSlotListResponseModel(

        val end_time: String,
        val slot_date: String,
        val slot_id: String,
        val slot_status: String,
        var isSelected: Boolean = false,
        val start_time: String

    )

    /*User Habit List*/
    data class GetHabitResponseModel(
        val habit_frequency: String,
        val habit_frequency_unit: String,
        val habit_name: String,
        val patient_habit_id: String,
        val patient_id: String,
        val status: String
    )

    /*User MyRecord List*/
    data class GetMyRecordResponseModel(
        val category: String,
        val file_content: String,
        val medical_record_id: String,
        val patient_id: String,
        val storage_link: String
    )

    /*User MyCarePlan List*/
    data class GetMyCarePlanResponseModel(

        val care_plan_id: String,
        val details_four: String,
        val details_one: String,
        val details_three: String,
        val details_two: String,
        val name: String,
        val patient_id: String,
        val status: String
    )

    /*User Vitals List*/
    data class GetMyVitalsSingleResponseModel(

        val vital_date: String,
        val vital_id: String,
        val vital_reading: String,
        val vital_unit: String
    )

    /*User Medication List*/
    data class GetMyMedicationResponseModel(

        val appointment_id: String,
        val days: String,
        val dosage_instructions: String,
        val drug_name: String,
        val medication_id: String,
        val patient_id: String
    )

    /*User Appointments List*/
    data class GetMyAppointmentsResponseModel(

        val appointment_info: TableData1,
        val service_provider_info: TableData2,
        val slot_info: TableData3
    ) {
        data class TableData1(
            val appointment_id: String,
            val patient_id: String,
            val doctor_slot_id: String,
            val facility_slot_id: String,
            val payment_id: String,
            val purpose: String,
            val remarks: String,
            val status: String,
            val payment_amount: String
        )

        data class TableData2(
            val provider_id: String,
            val provider_name: String,
            val provider_type: String
        )

        data class TableData3(
            val start_time: String,
            val end_time: String,
            val slot_date: String
        )
    }

    /*User PaymentHistory List*/
    data class GetPaymentHistoryResponseModel(

        val amount: String,
        val cgst_percentage: String,
        val convenience_fee: String,
        val discount_percentage: String,
        val gross_total: String,
        val igst_percentage: String,
        val payer_id: String,
        val payer_type: String,
        val payment_id: String,
        val receiver_id: String,
        val receiver_type: String,
        val sgst_percentage: String,
        val status: String,
        val transanction_id: String
    )

}