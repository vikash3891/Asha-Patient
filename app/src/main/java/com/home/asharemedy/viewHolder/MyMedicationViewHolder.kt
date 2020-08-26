package com.home.asharemedy.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.model.MedicationItemModel

class MyMedicationViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_medication, parent, false)) {
    private var medicineName: TextView? = null
    private var daysCount: TextView? = null
    private var medicineCourse: TextView? = null
    private var medicinePrice: TextView? = null


    init {
        medicineName = itemView.findViewById(R.id.medicineName)
        daysCount = itemView.findViewById(R.id.daysCount)
        medicineCourse = itemView.findViewById(R.id.medicineCourse)
        medicinePrice = itemView.findViewById(R.id.medicinePrice)
    }

    fun bind(movie: ResponseModelClasses.GetMyMedicationResponseModel) {

        medicineName?.text = movie.drug_name
        daysCount?.text = movie.days + " days"
        medicineCourse?.text = movie.dosage_instructions
        //medicinePrice?.text = getString(R.string.rupees_symbol)+movie.medicinePrice

    }

}