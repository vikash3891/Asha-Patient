package com.home.asharemedy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.model.PaymentItemModel
import com.home.asharemedy.viewHolder.PaymentListViewHolder
import kotlinx.android.synthetic.main.item_clinic_visit.view.*

class PaymentListAdapter(private val context: Context, private val list: List<ResponseModelClasses.GetPaymentHistoryResponseModel>) :
    RecyclerView.Adapter<PaymentListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PaymentListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PaymentListViewHolder, position: Int) {
        val movie: ResponseModelClasses.GetPaymentHistoryResponseModel = list[position]
        holder.bind(movie)

        holder.itemView.listItemLayout.setOnClickListener {
            //context.startActivity(Intent(context, ListItemDetailActivity::class.java))
        }
    }

    override fun getItemCount(): Int = list.size

}