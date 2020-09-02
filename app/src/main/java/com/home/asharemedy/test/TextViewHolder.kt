package com.home.asharemedy.test

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.api.ResponseModelClasses
import  com.home.asharemedy.databinding.TextViewHolderBinding

class TextViewHolder(private val binding: TextViewHolderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): TextViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TextViewHolderBinding.inflate(inflater, parent, false)
            return TextViewHolder(binding)
        }
    }

    fun bind(text: ResponseModelClasses.GetHabitResponseModel) {
        binding.habitLabel.text = text.habit_name
        binding.habitValue.text = text.habit_frequency + " " + text.habit_frequency_unit
    }
}