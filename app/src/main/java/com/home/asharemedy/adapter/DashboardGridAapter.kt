package com.home.asharemedy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.home.asharemedy.R
import com.home.asharemedy.chat.ui.activity.ChatLoginActivity
import com.home.asharemedy.view.HealthRecordActivity
import com.home.asharemedy.model.DashboardGridModel
import com.home.asharemedy.video.activities.LoginActivity
import com.home.asharemedy.view.MyProfile
import com.home.asharemedy.view.ListActivity
import com.home.asharemedy.view.MyVitalsActivity
import kotlinx.android.synthetic.main.item_dashboard_grid.view.*

class DashboardGridAapter(context: Context, var gridList: ArrayList<DashboardGridModel>) :
    BaseAdapter() {

    var context: Context? = context

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView

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

                    context!!.getString(R.string.appointments) ->
                        context!!.startActivity(
                            Intent(
                                context,
                                ListActivity::class.java
                            )
                        )

                    context!!.getString(R.string._247_doctor) ->
                        context!!.startActivity(Intent(context, MyProfile::class.java))

                    context!!.getString(R.string.talk_to_thb) ->
                        context!!.startActivity(Intent(context, ChatLoginActivity::class.java))

                    context!!.getString(R.string.health_info) ->
                        context!!.startActivity(Intent(context, MyProfile::class.java))

                    context!!.getString(R.string.more_thb) ->
                        context!!.startActivity(Intent(context, LoginActivity::class.java))


                    context!!.getString(R.string.my_vitals) ->
                        context!!.startActivity(Intent(context, MyVitalsActivity::class.java))

                    context!!.getString(R.string.my_clinical_visits) ->
                        context!!.startActivity(Intent(context, ListActivity::class.java))

                    context!!.getString(R.string.my_medications) ->
                        context!!.startActivity(Intent(context, ListActivity::class.java))

                    context!!.getString(R.string.my_records) ->
                        context!!.startActivity(Intent(context, ListActivity::class.java))

                    context!!.getString(R.string.my_care_plan) ->
                        context!!.startActivity(Intent(context, MyProfile::class.java))

                    context!!.getString(R.string.my_tele_consult) ->
                        context!!.startActivity(Intent(context, MyProfile::class.java))

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