package com.home.asharemedy.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.utils.Utils

class MyReportViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_my_reports, parent, false)) {
    private var reportDate: TextView? = null
    private var reportMonth: TextView? = null
    private var reportYear: TextView? = null
    private var reportName: TextView? = null
    private var reportLink: TextView? = null

    init {
        reportDate = itemView.findViewById(R.id.reportDate)
        reportMonth = itemView.findViewById(R.id.reportMonth)
        reportYear = itemView.findViewById(R.id.reportYear)
        reportName = itemView.findViewById(R.id.reportName)
        reportLink = itemView.findViewById(R.id.reportLink)
    }

    fun bind(movie: ResponseModelClasses.GetMyRecordResponseModel.TableData) {

        try {
            val parts = movie.created_at.split("-")
            reportDate?.text = parts[2]
            reportMonth?.text = Utils.setMonth(parts[1].toInt())
            reportYear?.text = parts[0]
            reportName?.text = movie.record_name
            reportLink?.text = movie.category
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}