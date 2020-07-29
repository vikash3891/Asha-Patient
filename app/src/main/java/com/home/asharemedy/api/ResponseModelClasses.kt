package com.home.asharemedy.api

import com.home.asharemedy.model.ListItemModel
import com.home.asharemedy.model.LoginModel

object ResponseModelClasses {

    /*-------Registration-------*/
    data class RegistrationResponse(
        val status: String,
        val data: ArrayList<LoginModel>,
        val message: String
    )

    /*-------Login-------*/
    data class LoginResponseModel(
        val status: String,
        val message: String,
        val data: ArrayList<LoginModel>
    )

    /*-------Login-------*/
    data class ProductListResponseModel(
        val status: String,
        val message: String,
        val data: ArrayList<ListItemModel>
    )

    /*-------Get Order ID before Adding Cart items-------*/
    data class GetOrderIDResponseModel(
        val status: String,
        val message: String,
        val data: ArrayList<TableData1>
    ) {
        data class TableData1(
            val order_id: String,
            val cart_id: String
        )
    }

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

    data class GetPatientProfileResponseModel(
        val age: String,
        val allergies: String,
        val birth_date: String,
        val emergency_contact_name: String,
        val emergency_contact_number: String,
        val emergency_contact_relation: String,
        val gender: String,
        val insurance_company_name: String,
        val insurance_validity: String,
        val insured: String,
        val membership_number: String,
        val parent_id: String,
        val patient_address1: String,
        val patient_address2: String,
        val patient_city: String,
        val patient_country: String,
        val patient_dob: String,
        val patient_email: String,
        val patient_gender: String,
        val patient_id: String,
        val patient_mobile: String,
        val patient_name: String,
        val patient_refcode: String,
        val photo: String,
        val primary_health_issue: String
    )

    data class GetAilmentResponseModel(
        val status: String,
        val message: String,
        val data: ArrayList<TableData1>
    ) {
        data class TableData1(
            val ailment: String,
            val ailment_id: String
        )
    }

    data class GetHabitResponseModel(
        val data: ArrayList<TableData1>
    ) {
        data class TableData1(

            val habit_frequency: String,
            val habit_frequency_unit: String,
            val habit_name: String,
            val patient_habit_id: String,
            val patient_id: String,
            val status: String
        )
    }



}