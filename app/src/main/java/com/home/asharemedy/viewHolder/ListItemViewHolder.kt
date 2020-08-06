package com.home.asharemedy.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses

class ListItemViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_clinic_visit, parent, false)) {
    private var visitDate: TextView? = null
    private var facilityName: TextView? = null
    private var clinicianSpecName: TextView? = null
    private var complaint: TextView? = null
    private var appointmentTime: TextView? = null
    private var appointmentStatus: TextView? = null

    init {
        try {
            visitDate = itemView.findViewById(R.id.visitDate)
            facilityName = itemView.findViewById(R.id.facilityName)
            clinicianSpecName = itemView.findViewById(R.id.clinicianSpecName)
            complaint = itemView.findViewById(R.id.complaint)
            appointmentTime = itemView.findViewById(R.id.appointmentTime)
            appointmentStatus = itemView.findViewById(R.id.appointmentStatus)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun bind(movie: ResponseModelClasses.GetMyAppointmentsResponseModel) {

        try {
            facilityName?.text = movie.service_provider_info.provider_name
            //clinicianSpecName?.text = movie.clinician + " " + movie.spec
            complaint?.text = movie.appointment_info.purpose
            appointmentTime?.text = movie.slot_info.start_time
            appointmentStatus?.text = movie.appointment_info.status
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}