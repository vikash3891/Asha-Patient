package com.home.asharemedy.api

import com.home.asharemedy.utils.Constants
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST(Constants.LOGIN_USER)
    fun getAdminLogin(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.LoginResponseModel>

    @GET("/{patientId}" + "/vitals" + "/{vitalName}")
    fun getVitalAPI(@Path("patientId") patientId: String,
                    @Path("vitalName") vitalName: String,@FieldMap fieldMap: Map<String, String>):
            Call<ResponseModelClasses.GetOrderHistoryResponseModel>

    @GET(Constants.GET_ALL_PRODUCTS)
    fun getAllProduct(): Call<ResponseModelClasses.ProductListResponseModel>

    @FormUrlEncoded
    @POST(Constants.PATIENT_REG)
    fun registerUser(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.RegistrationResponse>

    @FormUrlEncoded
    @POST(Constants.GET_ORDERID)
    fun getOrderIDLogin(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.GetOrderIDResponseModel>

    @FormUrlEncoded
    @POST(Constants.GET_ORDER_ADD_CART)
    fun addCartRequest(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.GetOrderIDResponseModel>

    @FormUrlEncoded
    @POST(Constants.REGISTRATION_USER + "/{userid}")
    fun updateProfile(@Path("userid") id: String, @FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.RegistrationResponse>

    @GET(Constants.GET_ORDER_HISTORY + "/{userid}")
    fun getOrderHistoryAPI(@Path("userid") id: String): Call<ResponseModelClasses.GetOrderHistoryResponseModel>

}