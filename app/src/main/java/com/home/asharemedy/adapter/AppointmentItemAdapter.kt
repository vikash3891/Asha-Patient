package com.home.asharemedy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.model.ListItemModel
import com.home.asharemedy.utils.Utils
import com.home.asharemedy.view.AppointmentSlotActivity
import com.home.asharemedy.view.ListItemDetailActivity
import com.home.asharemedy.viewHolder.AppointmentItemViewHolder
import kotlinx.android.synthetic.main.item_appointment.view.*
import kotlinx.android.synthetic.main.item_clinic_visit.view.*

class AppointmentItemAdapter(
    private val context: Context,
    private val list: List<ResponseModelClasses.GetFacilityListResponseModel.TableData1>
) :
    RecyclerView.Adapter<AppointmentItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AppointmentItemViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: AppointmentItemViewHolder, position: Int) {
        val movie: ResponseModelClasses.GetFacilityListResponseModel.TableData1 = list[position]
        holder.bind(movie)

        holder.itemView.listDoctorDetails.setOnClickListener {
            if (Utils.isDoctor)
                Utils.selectedDoctorFacitiyID = list[position].doctor_id
            else
                Utils.selectedDoctorFacitiyID = list[position].facility_id
            Utils.selectedDoctorFacility = list[position]
            context.startActivity(Intent(context, AppointmentSlotActivity::class.java))
        }
    }

    override fun getItemCount(): Int = list.size

}