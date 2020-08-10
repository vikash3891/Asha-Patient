package com.home.asharemedy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.home.asharemedy.R
import com.home.asharemedy.model.DashboardGridModel
import com.home.asharemedy.view.*
import kotlinx.android.synthetic.main.item_dashboard_grid.view.*

class AddMedicalRecordAdapter(context: Context, var gridList: ArrayList<DashboardGridModel>) :
    BaseAdapter() {

    var context: Context? = context

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val gridItem = this.gridList[position]

        val inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val gridItemView = inflator.inflate(R.layout.item_dashboard_grid, null)
        gridItemView.itemImage.setImageResource(gridItem.image!!)
        gridItemView.itemName.text = gridItem.name!!

        gridItemView.gridItemLayout.setOnClickListener {

            try {
                when (gridItemView.itemName.text.toString()) {

                    context!!.getString(R.string.my_health_record) ->
                        context!!.startActivity(Intent(context, HealthRecordActivity::class.java))

                    context!!.getString(R.string.payment_history) ->
                        context!!.startActivity(Intent(context, ActivityPaymentHistory::class.java))

                    else -> {
                        // Note the block
                        print("View not found.")
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return gridItemView
    }

    override fun getItem(position: Int): Any {
        return gridList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return gridList.size
    }
}