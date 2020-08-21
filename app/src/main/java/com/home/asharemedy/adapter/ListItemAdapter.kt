package com.home.asharemedy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.model.ListItemModel
import com.home.asharemedy.utils.Utils.selectedAppointmentDetails
import com.home.asharemedy.view.ListItemDetailActivity
import com.home.asharemedy.viewHolder.ListItemViewHolder
import kotlinx.android.synthetic.main.item_clinic_visit.view.*

class ListItemAdapter(private val context: Context, private val list: List<ResponseModelClasses.GetMyAppointmentsResponseModel.TableData4>) :
    RecyclerView.Adapter<ListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListItemViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val movie: ResponseModelClasses.GetMyAppointmentsResponseModel.TableData4 = list[position]
        selectedAppointmentDetails.add(movie)
        holder.bind(movie)

        holder.itemView.listItemLayout.setOnClickListener {
            context.startActivity(Intent(context, ListItemDetailActivity::class.java))
        }
    }

    override fun getItemCount(): Int = list.size

}