package com.home.asharemedy.adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.view.ViewDocumentActivity
import com.home.asharemedy.viewHolder.MyReportViewHolder
import kotlinx.android.synthetic.main.item_clinic_visit.view.*

class MyReportsListAdapter(private val context: Context, private val list: List<ResponseModelClasses.GetMyRecordResponseModel.TableData>) :
    RecyclerView.Adapter<MyReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReportViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyReportViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyReportViewHolder, position: Int) {
        val movie: ResponseModelClasses.GetMyRecordResponseModel.TableData = list[position]
        holder.bind(movie)

        holder.itemView.listItemLayout.setOnClickListener {

            val intent = Intent(context, ViewDocumentActivity::class.java)
            intent.putExtra("fileContent", list[position].file_content)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = list.size

}