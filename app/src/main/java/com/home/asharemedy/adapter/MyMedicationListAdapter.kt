package com.home.asharemedy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.model.ListItemModel
import com.home.asharemedy.model.MedicationItemModel
import com.home.asharemedy.view.ListItemDetailActivity
import com.home.asharemedy.viewHolder.MyMedicationViewHolder
import kotlinx.android.synthetic.main.item_clinic_visit.view.*

class MyMedicationListAdapter(private val context: Context, private val list: List<MedicationItemModel>) :
    RecyclerView.Adapter<MyMedicationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMedicationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyMedicationViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyMedicationViewHolder, position: Int) {
        val movie: MedicationItemModel = list[position]
        holder.bind(movie)

        holder.itemView.listItemLayout.setOnClickListener {
            //context.startActivity(Intent(context, ListItemDetailActivity::class.java))
        }
    }

    override fun getItemCount(): Int = list.size

}