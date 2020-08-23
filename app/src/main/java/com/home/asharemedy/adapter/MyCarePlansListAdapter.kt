package com.home.asharemedy.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.view.ListItemDetailActivity
import com.home.asharemedy.viewHolder.MyCarePlanViewHolder
import kotlinx.android.synthetic.main.item_clinic_visit.view.*

class MyCarePlansListAdapter(
    private val context: Context,
    private val list: List<ResponseModelClasses.GetMyCarePlanResponseModel>
) :
    RecyclerView.Adapter<MyCarePlanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCarePlanViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyCarePlanViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyCarePlanViewHolder, position: Int) {
        val movie: ResponseModelClasses.GetMyCarePlanResponseModel = list[position]
        holder.bind(movie)

        holder.itemView.listItemLayout.setOnClickListener {
            showMedicationDialog(position)
        }
    }

    override fun getItemCount(): Int = list.size

    private fun showMedicationDialog(position: Int) {
        var dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.medication_listitem_details)

        val header = dialog.findViewById(R.id.header) as TextView
        val daysLabel = dialog.findViewById(R.id.daysLabel) as TextView
        val daysValue = dialog.findViewById(R.id.daysValue) as TextView
        val doseLabel = dialog.findViewById(R.id.doseLabel) as TextView
        val doseValue = dialog.findViewById(R.id.doseValue) as TextView
        val instructionValue = dialog.findViewById(R.id.instructionValue) as TextView
        val statusValue = dialog.findViewById(R.id.statusValue) as TextView
        val layoutOk = dialog.findViewById(R.id.layoutOk) as LinearLayout

/*val care_plan_id: String,
        val details_four: String,
        val details_one: String,
        val details_three: String,
        val details_two: String,
        val name: String,
        val patient_id: String,
        val status: String*/
        header.text = "Care Plan Detail"
        daysLabel.text = "Plan Name: "
        daysValue.text = list[position].name
        doseLabel.text = "Instruction 1: "
        doseValue.text = list[position].details_one
        instructionValue.text = list[position].details_two
        statusValue.text = list[position].status
        layoutOk.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

}