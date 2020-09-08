package com.home.asharemedy.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.utils.Utils

class ListItemViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_clinic_visit, parent, false)) {
    private var visitDate: TextView? = null
    private var visitMonth: TextView? = null
    private var visitYear: TextView? = null
    private var facilityName: TextView? = null
    private var clinicianSpecName: TextView? = null
    private var complaint: TextView? = null
    private var appointmentTime: TextView? = null
    private var startTime: TextView? = null
    private var endTime: TextView? = null
    private var appointmentStatus: TextView? = null

    init {
        try {
            visitDate = itemView.findViewById(R.id.visitDate)
            visitMonth = itemView.findViewById(R.id.visitMonth)
            visitYear = itemView.findViewById(R.id.visitYear)
            facilityName = itemView.findViewById(R.id.facilityName)
            clinicianSpecName = itemView.findViewById(R.id.clinicianSpecName)
            complaint = itemView.findViewById(R.id.complaint)
            appointmentTime = itemView.findViewById(R.id.appointmentTime)
            startTime = itemView.findViewById(R.id.startTime)
            endTime = itemView.findViewById(R.id.endTime)
            appointmentStatus = itemView.findViewById(R.id.appointmentStatus)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun bind(movie: ResponseModelClasses.GetMyAppointmentsResponseModel.TableData4) {

        try {
            facilityName?.text = movie.appointment_provider_info.provider_name

            var str = movie.appointment_slot_info.slot_date
            var delimiter = "-"

            val parts = str.split(delimiter)
            visitDate?.text = parts[2]
            visitMonth?.text = Utils.setMonth(parts[1].toInt())
            visitYear?.text = parts[0]
            complaint?.text = movie.appointment_info.purpose
            appointmentTime?.text = movie.appointment_slot_info.start_time
            startTime?.text = Utils.get12HourTime(movie.appointment_slot_info.start_time)
            endTime?.text = "- " + Utils.get12HourTime(movie.appointment_slot_info.end_time)
            appointmentStatus?.text = movie.appointment_info.status
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}