package com.home.asharemedy.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.utils.Utils.getString

class AppointmentItemViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_appointment, parent, false)) {
    private var doctorName: TextView? = null
    private var doctorSpeciality: TextView? = null
    private var address: TextView? = null
    private var consultationFees: TextView? = null

    init {
        doctorName = itemView.findViewById(R.id.doctorName)
        doctorSpeciality = itemView.findViewById(R.id.doctorSpeciality)
        address = itemView.findViewById(R.id.address)
        consultationFees = itemView.findViewById(R.id.consultationFees)
    }

    fun bind(movie: ResponseModelClasses.GetFacilityListResponseModel) {
        doctorName?.text = movie.name
        doctorSpeciality?.text = movie.specialization
        consultationFees?.text = getString(R.string.rupees_symbol) + movie.fees
        address?.text = movie.address1 + " " + movie.address2
    }

}