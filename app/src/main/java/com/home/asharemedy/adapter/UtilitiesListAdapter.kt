package com.home.asharemedy.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.home.asharemedy.R
import com.home.asharemedy.model.AilmentArrayData
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.question_list_layout.view.*

class UtilitiesListAdapter(val itemClick: (Int) -> Unit) :
    RecyclerView.Adapter<UtilitiesListAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_list_layout, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        var count = 0
        return if (Utils.isAilmentOrService)
            AilmentArrayData.getCount()
        else
            AilmentArrayData.getServicesCount()
    }

    override fun onBindViewHolder(holder: MyHolder, p1: Int) {
        holder.bindData(p1)
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(int: Int) {

            if (Utils.isAilmentOrService) {
                var data = AilmentArrayData.getArrayItem(int)
                itemView.txtQuestion.text = data.ailment
                itemView.txtQuestion.setOnClickListener {
                    itemClick(adapterPosition)
                }
            } else {
                var data = AilmentArrayData.getServicesArrayItem(int)
                itemView.txtQuestion.text = data.service
                itemView.txtQuestion.setOnClickListener {
                    itemClick(adapterPosition)
                }
            }

        }
    }
}