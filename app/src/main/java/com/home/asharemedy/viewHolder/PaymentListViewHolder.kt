package com.home.asharemedy.viewHolder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.model.PaymentItemModel

class PaymentListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_payment, parent, false)) {
    private var paymentDate: TextView? = null
    private var paymentMonth: TextView? = null
    private var paymentAmount: TextView? = null
    private var paymentStatus: TextView? = null
    private var paymentID: TextView? = null

    init {
        paymentDate = itemView.findViewById(R.id.paymentDate)
        paymentMonth = itemView.findViewById(R.id.paymentMonth)
        paymentAmount = itemView.findViewById(R.id.paymentAmount)
        paymentStatus = itemView.findViewById(R.id.paymentStatus)
        paymentID = itemView.findViewById(R.id.paymentID)
    }

    fun bind(movie: ResponseModelClasses.GetPaymentHistoryResponseModel) {

        //val parts = movie..split(delimiter)
//        paymentDate?.text = movie.paymentDate
//        paymentMonth?.text = movie.paymentMonth
        paymentAmount?.text = "$" + movie.amount
        paymentStatus?.text = movie.status
        paymentID?.text = "ID: " + movie.transanction_id

        when {
            movie.status.equals("successful") -> paymentStatus!!.setTextColor(Color.parseColor("#8bd643"))
            movie.status.equals("Pending") -> paymentStatus!!.setTextColor(Color.parseColor("#E99543"))
            else -> paymentStatus!!.setTextColor(Color.parseColor("#ff0000"))
        }
    }

}