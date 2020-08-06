package com.home.asharemedy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.view.ListItemDetailActivity
import com.home.asharemedy.viewHolder.MyReportViewHolder
import kotlinx.android.synthetic.main.item_clinic_visit.view.*

class MyReportsListAdapter(private val context: Context, private val list: List<ResponseModelClasses.GetMyRecordResponseModel>) :
    RecyclerView.Adapter<MyReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReportViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyReportViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyReportViewHolder, position: Int) {
        val movie: ResponseModelClasses.GetMyRecordResponseModel = list[position]
        holder.bind(movie)

        holder.itemView.listItemLayout.setOnClickListener {
            context.startActivity(Intent(context, ListItemDetailActivity::class.java))
        }
    }

    override fun getItemCount(): Int = list.size

}