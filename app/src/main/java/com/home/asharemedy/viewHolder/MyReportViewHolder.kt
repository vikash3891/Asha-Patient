package com.home.asharemedy.viewHolder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses

class MyReportViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_my_reports, parent, false)) {
    private var reportDate: TextView? = null
    private var reportName: TextView? = null
    private var reportLink: TextView? = null

    init {
        reportDate = itemView.findViewById(R.id.reportDate)
        reportName = itemView.findViewById(R.id.reportName)
        reportLink = itemView.findViewById(R.id.reportLink)
    }

    fun bind(movie: ResponseModelClasses.GetMyRecordResponseModel) {
        //visitDate?.text = movie.date
        reportDate?.text = movie.storage_link
        reportName?.text = movie.record_name
        reportLink?.text = movie.medical_record_id

    }

}