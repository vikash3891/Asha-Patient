package com.home.asharemedy.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.model.AppointSlotListModel
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.item_appointment_slot.view.*
import kotlinx.android.synthetic.main.item_dashboard_grid.view.*
import java.text.SimpleDateFormat

class AppointSlotListAdapter(
    context: Context,
    var gridList: ArrayList<ResponseModelClasses.GetSlotListResponseModel>
) :
    BaseAdapter() {

    var context: Context? = context

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView

        val gridItem = this.gridList[position]

        val inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val gridItemView = inflator.inflate(R.layout.item_appointment_slot, null)

        val displayFormat = SimpleDateFormat("HH:mm a")
        val parseFormat = SimpleDateFormat("hh:mm:ss")
        val date = parseFormat.parse(gridItem.start_time)
        val date2 = parseFormat.parse(gridItem.end_time)
            //.toString() + " - " + parseFormat.parse(gridItem.end_time).toString()
        var slot = displayFormat.format(date)+" - "+displayFormat.format(date2)
        Log.d("SlotDate: ", slot)
        gridItemView.appointmentSlotRange.text = slot

        when {
            gridItem.slot_status.equals("available") -> {
                gridItemView.listItemLayout.setBackground(
                    ContextCompat.getDrawable(
                        context!!,
                        R.drawable.drawable_round_corner
                    )
                )
                gridItemView.appointmentSlotRange.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.black
                    )
                )
            }
            gridItem.slot_status.equals("booked") -> {
                gridItemView.listItemLayout.setBackground(
                    ContextCompat.getDrawable(
                        context!!,
                        R.drawable.round_corner_green_disable
                    )
                )
                gridItemView.appointmentSlotRange.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.white
                    )
                )
            }
            gridItem.slot_status.equals("completed") -> {
                gridItemView.listItemLayout.setBackground(
                    ContextCompat.getDrawable(
                        context!!,
                        R.drawable.round_corner_green_disable
                    )
                )

                gridItemView.appointmentSlotRange.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.white
                    )
                )
            }
        }

        gridItemView.listItemLayout.setOnClickListener {

            try {
                if (gridItem.slot_status.equals("available")) {
                    if (gridItem.isSelected == null || gridItem.isSelected == false) {
                        gridItem.isSelected = true
                        //Utils.appointmentSlotList[position].isSelected = true
                        gridItemView.listItemLayout.background = ContextCompat.getDrawable(
                            context!!,
                            R.drawable.round_corner_green
                        )
                        gridItemView.appointmentSlotRange.setTextColor(
                            ContextCompat.getColor(
                                context!!,
                                R.color.white
                            )
                        )
                    } else {
                        gridItem.isSelected = false
                        //Utils.appointmentSlotList[position].isSelected = false
                        gridItemView.listItemLayout.background = ContextCompat.getDrawable(
                            context!!,
                            R.drawable.drawable_round_corner
                        )
                        gridItemView.appointmentSlotRange.setTextColor(
                            ContextCompat.getColor(
                                context!!,
                                R.color.black
                            )
                        )
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