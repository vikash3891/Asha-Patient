package com.home.asharemedy.video.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.video.utils.getColor
import com.home.asharemedy.video.utils.getColorCircleDrawable
import com.home.asharemedy.video.utils.getColoredCircleDrawable
import com.quickblox.users.model.QBUser
import kotlinx.android.synthetic.main.item_opponents_list.view.*


class UsersAdapter : RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    val context: Context
    private var usersList: ArrayList<QBUser>
    private val _selectedUsers: MutableList<QBUser>
    val selectedUsers: List<QBUser>
        get() = _selectedUsers
    private lateinit var selectedItemsCountsChangedListener: SelectedItemsCountsChangedListener

    constructor(context: Context, usersList: ArrayList<QBUser>) : super() {
        this.context = context
        this.usersList = usersList
        this._selectedUsers = ArrayList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = usersList[position]
        holder.opponentName.text = user.fullName
        if (_selectedUsers.contains(user)) {
            holder.rootLayout.setBackgroundResource(R.color.background_color_selected_user_item)
            holder.opponentIcon.setBackgroundDrawable(
                    getColoredCircleDrawable(getColor(R.color.icon_background_color_selected_user)))
            holder.opponentIcon.setImageResource(R.drawable.ic_checkmark)
        } else {
            holder.rootLayout.setBackgroundResource(R.color.background_color_normal_user_item)
            holder.opponentIcon.setBackgroundDrawable(getColorCircleDrawable(user.id))
            holder.opponentIcon.setImageResource(R.drawable.ic_person)
        }
        holder.rootLayout.setOnClickListener { v ->
            toggleSelection(user)
            selectedItemsCountsChangedListener.onCountSelectedItemsChanged(_selectedUsers.size)
        }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    fun updateUsersList (usersList: ArrayList<QBUser>) {
        this.usersList = usersList
        notifyDataSetChanged()
    }

    fun addUsers(users: ArrayList<QBUser>) {
        for (user in users) {
            if (!usersList.contains(user)) {
                usersList.add(user)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_opponents_list, parent, false))
    }

    private fun toggleSelection(qbUser: QBUser) {
        if (_selectedUsers.contains(qbUser)) {
            _selectedUsers.remove(qbUser)
        } else {
            _selectedUsers.add(qbUser)
        }
        notifyDataSetChanged()
    }

    fun setSelectedItemsCountsChangedListener(selectedItemsCountsChangedListener: SelectedItemsCountsChangedListener) {
        this.selectedItemsCountsChangedListener = selectedItemsCountsChangedListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val opponentIcon = view.image_opponent_icon
        val opponentName = view.opponents_name
        val rootLayout = view.root_layout
    }

    interface SelectedItemsCountsChangedListener {
        fun onCountSelectedItemsChanged(count: Int)
    }
}