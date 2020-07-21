package com.home.asharemedy.viewHolder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.model.ListItemModel

class ListItemViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_clinic_visit, parent, false)) {
    private var visitDate: TextView? = null
    private var facilityName: TextView? = null
    private var clinicianSpecName: TextView? = null
    private var complaint: TextView? = null
    private var appointmentTime: TextView? = null
    private var appointmentStatus: TextView? = null


    init {
        visitDate = itemView.findViewById(R.id.visitDate)
        facilityName = itemView.findViewById(R.id.facilityName)
        clinicianSpecName = itemView.findViewById(R.id.clinicianSpecName)
        complaint = itemView.findViewById(R.id.complaint)
        appointmentTime = itemView.findViewById(R.id.appointmentTime)
        appointmentStatus = itemView.findViewById(R.id.appointmentStatus)
    }

    fun bind(movie: ListItemModel) {
        //visitDate?.text = movie.date
        facilityName?.text = movie.facility
        clinicianSpecName?.text = movie.clinician + " " + movie.spec
        complaint?.text = movie.complaints
        appointmentTime?.text = movie.time
        appointmentStatus?.text = movie.status
        if (movie.status.equals("Scheduled")) {
            appointmentStatus!!.setTextColor(Color.parseColor("#E99543"))
            appointmentTime!!.setTextColor(Color.parseColor("#E99543"))
        } else if (movie.status.equals("Pending")) {
            appointmentStatus!!.setTextColor(Color.parseColor("#fe5558"))
            appointmentTime!!.setTextColor(Color.parseColor("#fe5558"))
        } else {
            appointmentStatus!!.setTextColor(Color.parseColor("#8bd643"))
            appointmentTime!!.setTextColor(Color.parseColor("#8bd643"))
        }
    }

}