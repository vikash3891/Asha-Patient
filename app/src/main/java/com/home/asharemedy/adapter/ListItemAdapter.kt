package com.home.asharemedy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.model.ListItemModel
import com.home.asharemedy.view.ListItemDetailActivity
import com.home.asharemedy.viewHolder.ListItemViewHolder
import kotlinx.android.synthetic.main.item_clinic_visit.view.*

class ListItemAdapter(private val context: Context, private val list: List<ListItemModel>) :
    RecyclerView.Adapter<ListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListItemViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val movie: ListItemModel = list[position]
        holder.bind(movie)

        holder.itemView.listItemLayout.setOnClickListener {
            context.startActivity(Intent(context, ListItemDetailActivity::class.java))
        }
    }

    override fun getItemCount(): Int = list.size

}