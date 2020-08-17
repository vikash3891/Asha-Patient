package com.home.asharemedy.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses

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

    fun bind(movie: ResponseModelClasses.GetFacilityListResponseModel) {
        doctorName?.text = movie.name
        doctorSpeciality?.text = movie.city
        address?.text = movie.address1 + " " + movie.address2
    }

}