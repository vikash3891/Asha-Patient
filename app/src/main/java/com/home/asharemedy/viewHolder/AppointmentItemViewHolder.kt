package com.home.asharemedy.viewHolder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.model.ListItemModel

class AppointmentItemViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_appointment, parent, false)) {
    private var doctorName: TextView? = null
    private var doctorSpeciality: TextView? = null
    private var address: TextView? = null


    init {
        doctorName = itemView.findViewById(R.id.doctorName)
        doctorSpeciality = itemView.findViewById(R.id.doctorSpeciality)
        address = itemView.findViewById(R.id.address)
    }

    fun bind(movie: ListItemModel) {
        doctorName?.text = movie.clinician
        doctorSpeciality?.text = movie.spec
        address?.text = movie.clinician + " " + movie.spec
        /*complaint?.text = movie.complaints
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
        }*/
    }

}