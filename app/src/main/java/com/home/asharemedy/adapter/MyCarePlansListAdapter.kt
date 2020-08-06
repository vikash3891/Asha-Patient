package com.home.asharemedy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.view.ListItemDetailActivity
import com.home.asharemedy.viewHolder.MyCarePlanViewHolder
import kotlinx.android.synthetic.main.item_clinic_visit.view.*

class MyCarePlansListAdapter(private val context: Context, private val list: List<ResponseModelClasses.GetMyCarePlanResponseModel>) :
    RecyclerView.Adapter<MyCarePlanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCarePlanViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyCarePlanViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyCarePlanViewHolder, position: Int) {
        val movie: ResponseModelClasses.GetMyCarePlanResponseModel = list[position]
        holder.bind(movie)

        holder.itemView.listItemLayout.setOnClickListener {
            context.startActivity(Intent(context, ListItemDetailActivity::class.java))
        }
    }

    override fun getItemCount(): Int = list.size

}