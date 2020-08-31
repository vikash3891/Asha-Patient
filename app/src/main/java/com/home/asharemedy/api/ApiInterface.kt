package com.home.asharemedy.api

import com.home.asharemedy.model.AppointSlotListModel
import com.home.asharemedy.utils.Constants
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

interface ApiInterface {

    @POST(Constants.LOGIN_USER)
    fun getAdminLogin(@Body body: RequestBody): Call<ResponseModelClasses.LoginResponseModel>


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
    fun getFacilitiesList(): Call<ResponseModelClasses.GetFacilityListResponseModel>

    /*Get Facility List*/
    @GET(Constants.FACILITIES)
    fun getFacilitiesListByService(@Query("serviceId") ailmentId: String, @Query("city") city: String): Call<ResponseModelClasses.GetFacilityListResponseModel>

    /*Get Doctor List*/
    @GET(Constants.DOCTOR_LIST)
    fun getDoctorsList(): Call<ResponseModelClasses.GetFacilityListResponseModel>

    /*Get Doctor List*/
    @GET(Constants.DOCTOR_LIST)//ailmentId=1&city=Delhi
    fun getDoctorsListByAilment(@Query("ailmentId") ailmentId: String, @Query("city") city: String): Call<ResponseModelClasses.GetFacilityListResponseModel>

    /*Get Slot  List*/
    /*doctors/7/slots?slotDate=2020-12-13*/
    @GET(Constants.DOCTOR_SLOT_LIST + "/{doctorID}" + Constants.SLOT_SLOTDATE)
    fun getSlotList(@Path("doctorID") id: String, @Query("slotDate") date: String): Call<ArrayList<ResponseModelClasses.GetSlotListResponseModel>>

    /*Get AppointmentSlotList */
    @GET(Constants.PATIENT_REG + "/{patientID}" + Constants.HABIT)
    fun getAppointmentSlotList(@Path("patientID") id: String): Call<ArrayList<AppointSlotListModel>>

    /*Get RecordsList */
    @GET(Constants.PATIENT_REG + "/{patientID}" + Constants.MEDICAL_REPORTS)
    fun getMyRecordsList(@Path("patientID") id: String): Call<ArrayList<ResponseModelClasses.GetMyRecordResponseModel>>

    /*Get MyCarePlan */
    @GET(Constants.PATIENT_REG + "/{patientID}" + Constants.CARE_PLANS)
    fun getMyCarePlanList(@Path("patientID") id: String): Call<ArrayList<ResponseModelClasses.GetMyCarePlanResponseModel>>

    /*Get VitalsList */
    @GET(Constants.PATIENT_REG + "{patientID}" + Constants.VITALS + "{vitalName}" + "?")
    fun getPatientVitalsList(@Path("patientID") id: String, @Path("vitalName") vitalName: String): Call<ArrayList<ResponseModelClasses.GetMyVitalsSingleResponseModel>>


    /*Get MedicationsList */
    @GET(Constants.PATIENT_REG + "/{patientID}" + Constants.MEDICATIONS)
    fun getMyMedicationsList(@Path("patientID") id: String): Call<ResponseModelClasses.GetMyMedicationResponseModel>

    /*Get Appointment List */
    @GET(Constants.APPOINTMENTS)// + Constants.USER_ID + "={userId}&" + Constants.USER_TYPE + "={userType}")
    fun getMyAppointmentList(@Query("userId") userId: String, @Query("userType") userType: String): Call<ResponseModelClasses.GetMyAppointmentsResponseModel>

    /*Get Appointment List */
    @GET(Constants.APPOINTMENTS)// + Constants.USER_ID + "={userId}&" + Constants.USER_TYPE + "={userType}") startDate=2020-08-10&endDate=2020-08-22
    fun getMyAppointmentListByDate(@Query("userId") userId: String, @Query("userType") userType: String,
                                   @Query("startDate") startDate: Date, @Query("endDate") endDate: Date): Call<ResponseModelClasses.GetMyAppointmentsResponseModel>

    /*Get PaymentHistory */
    @GET(Constants.PAYMENT_HISTORY)// + "/{patientID}")   //@Path("patientID") id: String
    fun getPaymentHistoryList(): Call<ArrayList<ResponseModelClasses.GetPaymentHistoryResponseModel>>

    /*Get Habit*/
    @POST(Constants.PATIENT_REG + "/{patientID}" + Constants.HABIT)/*/patients/17/habits*/
    fun getHabit(@Path("patientID") id: String, @Body body: RequestBody): Call<ResponseModelClasses.LoginResponseModel>

    /*Get Single Unit Vital*/
    @POST(Constants.PATIENT_REG + "{patientID}" + Constants.VITALS + "{vitalName}")/*patients/13/vitals/temperature?*/
    fun getSingleUnitVital(@Path("patientID") id: String, @Path("vitalName") vitalName: String, @Body body: RequestBody): Call<ResponseModelClasses.SetVitalResponseModel>

    @POST(Constants.FORGOT_PASSWORD)
    fun forgotPassword(@Body body: RequestBody): Call<ResponseModelClasses.RegistrationResponse>

    @POST(Constants.FORGOT_PASSWORD)
    fun setPaymentStepOne(@Body body: RequestBody): Call<ResponseModelClasses.RegistrationResponse>

    @POST(Constants.PATIENT_REG + "{patientID}" + Constants.MEDICATIONS)/*/patients/17/habits*/
    fun setMedication(@Path("patientID") id: String, @Body body: RequestBody): Call<ResponseModelClasses.SetVitalResponseModel>

}