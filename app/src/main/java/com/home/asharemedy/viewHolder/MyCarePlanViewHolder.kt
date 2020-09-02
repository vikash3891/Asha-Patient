package com.home.asharemedy.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses

class MyCarePlanViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_care_plan, parent, false)) {
    private var carePlanID: TextView? = null
    private var carePlanName: TextView? = null
    private var carePlanStatus: TextView? = null

    init {
        carePlanID = itemView.findViewById(R.id.carePlanID)
        carePlanName = itemView.findViewById(R.id.carePlanName)
        carePlanStatus = itemView.findViewById(R.id.carePlanStatus)
    }

    fun bind(movie: ResponseModelClasses.GetMyCarePlanResponseModel.TableData) {
        //visitDate?.text = movie.date
        carePlanID?.text = movie.care_plan_id
        carePlanName?.text = movie.name
        carePlanStatus?.text = movie.status

    }

}