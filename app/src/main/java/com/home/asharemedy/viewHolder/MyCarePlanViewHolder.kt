package com.home.asharemedy.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.utils.Utils

class MyCarePlanViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_care_plan, parent, false)) {
    private var carePlanDate: TextView? = null
    private var carePlanMonth: TextView? = null
    private var carePlanYear: TextView? = null
    private var carePlanName: TextView? = null
    private var carePlanStatus: TextView? = null

    init {
        carePlanDate = itemView.findViewById(R.id.carePlanDate)
        carePlanMonth = itemView.findViewById(R.id.carePlanMonth)
        carePlanYear = itemView.findViewById(R.id.carePlanYear)
        carePlanName = itemView.findViewById(R.id.carePlanName)
        carePlanStatus = itemView.findViewById(R.id.carePlanStatus)
    }

    fun bind(movie: ResponseModelClasses.GetMyCarePlanResponseModel.TableData) {
        try {
            val parts = movie.careplan_date.split("-")
            carePlanDate?.text = parts[2]
            carePlanMonth?.text = Utils.setMonth(parts[1].toInt())
            carePlanYear?.text = parts[0]
            carePlanName?.text = movie.name
            carePlanStatus?.text = movie.status
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}