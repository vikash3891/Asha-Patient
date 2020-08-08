package com.home.asharemedy.api

import com.home.asharemedy.model.AppointSlotListModel
import com.home.asharemedy.utils.Constants
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST(Constants.LOGIN_USER)
    fun getAdminLogin(@Body body: RequestBody): Call<ResponseModelClasses.LoginResponseModel>

    @GET("/{patientId}" + "/vitals" + "/{vitalName}")
    fun getVitalAPI(
        @Path("patientId") patientId: String,
        @Path("vitalName") vitalName: String, @FieldMap fieldMap: Map<String, String>
    ):
            Call<ResponseModelClasses.GetOrderHistoryResponseModel>

    @POST(Constants.PATIENT_REGISTRATION)
    fun registerUser(@Body body: RequestBody): Call<ResponseModelClasses.RegistrationResponse>

    @PUT(Constants.PATIENT_REG + "{userid}")
    fun updateProfile(@Path("userid") id: String, @Body body: RequestBody): Call<ResponseModelClasses.LoginResponseModel>

    /*Get Patient Profile*/
    @GET(Constants.PATIENT_REG + "/{patientID}")
    fun getPatientProfile(@Path("patientID") id: String): Call<ResponseModelClasses.GetPatientProfileResponseModel>

    /*Get Patient Profile*/
    @GET(Constants.PATIENT_REG + "/{patientID}" + Constants.HABIT)
    fun getPatientHabits(@Path("patientID") id: String): Call<ArrayList<ResponseModelClasses.GetHabitResponseModel>>

    /*Get Ailment List*/
    @GET(Constants.AILMENTS)
    fun getAilmentList(): Call<ArrayList<ResponseModelClasses.GetAilmentResponseModel>>

    /*Get SERVICES List*/
    @GET(Constants.SERVICES)
    fun getServicesList(): Call<ArrayList<ResponseModelClasses.GetServiceResponseModel>>

    /*Get Facility List*/
    @GET(Constants.FACILITIES)
    fun getFacilitiesList(): Call<ArrayList<ResponseModelClasses.GetFacilityListResponseModel>>

    /*Get Doctor List*/
    @GET(Constants.DOCTOR_LIST)
    fun getDoctorsList(): Call<ArrayList<ResponseModelClasses.GetFacilityListResponseModel>>

    /*Get Slot  List*/
    /*doctors/7/slots?slotDate=2020-12-13*/
    @GET(Constants.DOCTOR_SLOT_LIST + "/{doctorID}" + Constants.SLOT_SLOTDATE)
    fun getSlotList(@Path("doctorID") id: String, @Query("slotDate") date: String): Call<ArrayList<ResponseModelClasses.GetSlotListResponseModel>>

    /*Get AppointmentSlotList */
    @GET(Constants.PATIENT_REG + "/{patientID}" + Constants.HABIT)
    fun getAppointmentSlotList(@Path("patientID") id: String): Call<ArrayList<AppointSlotListModel>>

    /*Get MyRecords */
    @GET(Constants.PATIENT_REG + "/{patientID}" + Constants.MEDICAL_REPORTS)
    fun getMyRecordsList(@Path("patientID") id: String): Call<ArrayList<ResponseModelClasses.GetMyRecordResponseModel>>

    /*Get MyCarePlan */
    @GET(Constants.PATIENT_REG + "/{patientID}" + Constants.CARE_PLANS)
    fun getMyCarePlanList(@Path("patientID") id: String): Call<ArrayList<ResponseModelClasses.GetMyCarePlanResponseModel>>

    /*Get MyCarePlan */
    @GET(Constants.PATIENT_REG + "/{patientID}" + Constants.VITALS + "{vitalName}" + "?")
    fun getPatientVitalsList(@Path("patientID") id: String, @Path("vitalName") vitalName: String): Call<ArrayList<ResponseModelClasses.GetMyVitalsSingleResponseModel>>


    /*Get MyCarePlan */
    @GET(Constants.PATIENT_REG + "/{patientID}" + Constants.MEDICATIONS)
    fun getMyMedicationsList(@Path("patientID") id: String): Call<ArrayList<ResponseModelClasses.GetMyMedicationResponseModel>>

    /*Get Appointment List */
    @GET(Constants.APPOINTMENTS)// + "/{patientID}") //@Path("patientID") id: String
    fun getMyAppointmentList(): Call<ArrayList<ResponseModelClasses.GetMyAppointmentsResponseModel>>

    /*Get PaymentHistory */
    @GET(Constants.PAYMENT_HISTORY)// + "/{patientID}")   //@Path("patientID") id: String
    fun getPaymentHistoryList(): Call<ArrayList<ResponseModelClasses.GetPaymentHistoryResponseModel>>


    @POST(Constants.PATIENT_REG + "/{patientID}" + Constants.HABIT)/*/patients/17/habits*/
    fun getHabit(@Path("patientID") id: String,@Body body: RequestBody): Call<ResponseModelClasses.LoginResponseModel>

}