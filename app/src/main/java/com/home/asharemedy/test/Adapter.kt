package com.home.asharemedy.test

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.api.ResponseModelClasses
import com.home.asharemedy.test.TextViewHolder

class Adapter(private val strings: ArrayList<ResponseModelClasses.GetHabitResponseModel>) : RecyclerView.Adapter<TextViewHolder>() {
    override fun getItemCount() = strings.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TextViewHolder.from(parent)

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) =
        holder.bind(strings[position])
}