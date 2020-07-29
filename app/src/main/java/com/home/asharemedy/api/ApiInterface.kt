package com.home.asharemedy.api

import com.home.asharemedy.model.AppointSlotListModel
import com.home.asharemedy.utils.Constants
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST(Constants.LOGIN_USER)
    fun getAdminLogin(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.LoginResponseModel>

    @GET("/{patientId}" + "/vitals" + "/{vitalName}")
    fun getVitalAPI(
        @Path("patientId") patientId: String,
        @Path("vitalName") vitalName: String, @FieldMap fieldMap: Map<String, String>
    ):
            Call<ResponseModelClasses.GetOrderHistoryResponseModel>

    @FormUrlEncoded
    @POST(Constants.PATIENT_REG)
    fun registerUser(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.RegistrationResponse>

    @FormUrlEncoded
    @POST(Constants.REGISTRATION_USER + "/{userid}")
    fun updateProfile(@Path("userid") id: String, @FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.RegistrationResponse>

    /*Get Patient Profile*/
    @GET(Constants.PATIENT_REG + "/{patientID}")
    fun getPatientProfile(@Path("patientID") id: String): Call<ResponseModelClasses.GetPatientProfileResponseModel>

    /*Get Patient Profile*/
    @GET(Constants.PATIENT_REG + "/{patientID}" + Constants.HABIT)
    fun getPatientHabits(@Path("patientID") id: String): Call<ResponseModelClasses.GetHabitResponseModel>

    /*Get Aliment List*/
    @GET(Constants.AILMENTS)
    fun getAlimentList(): Call<ResponseModelClasses.GetAilmentResponseModel>

    /*Get SERVICES List*/
    @GET(Constants.SERVICES)
    fun getServicesList(): Call<ResponseModelClasses.GetAilmentResponseModel>

    /*Get Facility List*/
    @GET(Constants.FACILITIES)
    fun getFacilitiesList(): Call<ResponseModelClasses.GetAilmentResponseModel>

    /*Get Patient Profile*/
    @GET(Constants.PATIENT_REG + "/{patientID}" + Constants.HABIT)
    fun getAppointmentSlotList(@Path("patientID") id: String): Call<ArrayList<AppointSlotListModel>>
}