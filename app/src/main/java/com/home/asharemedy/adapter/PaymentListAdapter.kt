package com.home.asharemedy.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.model.PaymentItemModel
import com.home.asharemedy.viewHolder.PaymentListViewHolder
import kotlinx.android.synthetic.main.item_clinic_visit.view.*

class PaymentListAdapter(
    private val context: Context,
    private val list: List<ResponseModelClasses.GetPaymentHistoryResponseModel.TableData>
) :
    RecyclerView.Adapter<PaymentListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PaymentListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PaymentListViewHolder, position: Int) {
        try {
            val movie: ResponseModelClasses.GetPaymentHistoryResponseModel.TableData = list[position]
            holder.bind(movie)

            holder.itemView.listItemLayout.setOnClickListener {
                showPaymentDetailDialog(position)
                //context.startActivity(Intent(context, ListItemDetailActivity::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int = list.size

    private fun showPaymentDetailDialog(position: Int) {
        try {
            var dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_payment_details)

            val amountValue = dialog.findViewById(R.id.amountValue) as TextView
            val discountValue = dialog.findViewById(R.id.discountValue) as TextView
            val convenienceValue = dialog.findViewById(R.id.convenienceValue) as TextView
            val grossTotalValue = dialog.findViewById(R.id.grossTotalValue) as TextView
            val statusValue = dialog.findViewById(R.id.statusValue) as TextView
            val layoutOk = dialog.findViewById(R.id.layoutOk) as LinearLayout


            amountValue.text = list[position].amount
            discountValue.text = list[position].discount_percentage
            convenienceValue.text = list[position].convenience_fee
            grossTotalValue.text = list[position].gross_total
            statusValue.text = list[position].status
            layoutOk.setOnClickListener { dialog.dismiss() }
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}