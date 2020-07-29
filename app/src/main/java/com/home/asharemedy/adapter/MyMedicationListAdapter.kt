package com.home.asharemedy.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.model.ListItemModel
import com.home.asharemedy.model.MedicationItemModel
import com.home.asharemedy.view.ListItemDetailActivity
import com.home.asharemedy.viewHolder.MyMedicationViewHolder
import kotlinx.android.synthetic.main.item_clinic_visit.view.*

class MyMedicationListAdapter(
    private val context: Context,
    private val list: List<MedicationItemModel>
) :
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

    private fun showAddServiceDialog(title: String) {
        var dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.medication_listitem_details)

        val daysValue = dialog.findViewById(R.id.daysValue) as TextView
        val doseValue = dialog.findViewById(R.id.doseValue) as TextView
        val instructionValue = dialog.findViewById(R.id.instructionValue) as TextView
        val statusValue = dialog.findViewById(R.id.statusValue) as TextView
        val layoutOk = dialog.findViewById(R.id.layoutOk) as LinearLayout

        layoutOk.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }
}