package com.home.asharemedy.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.home.asharemedy.utils.Utils
import com.home.asharemedy.utils.Utils.selectedHealthIssues
import com.home.asharemedy.utils.Utils.teachers
import kotlinx.android.synthetic.main.activity_check_list.*
import java.util.*

class CheckListActivity : AppCompatActivity() {

    internal var sb: StringBuilder? = null
    internal lateinit var adapter: MyAdapter

    internal class MyAdapter(var c: Context, var teachers: Array<Utils.SpiritualTeacher>) :
        RecyclerView.Adapter<MyAdapter.MyHolder>() {
        var checkedTeachers = ArrayList<Utils.SpiritualTeacher>()

        //VIEWHOLDER IS INITIALIZED
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.model, null)
            return MyHolder(v)
        }

        //DATA IS BOUND TO VIEWS
        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            val teacher = teachers[position]
            holder.nameTxt.text = teacher.name
            holder.myCheckBox.isChecked = teacher.isSelected

            holder.setItemClickListener(object : MyHolder.ItemClickListener {
                override fun onItemClick(v: View, pos: Int) {
                    val myCheckBox = v as CheckBox
                    val currentTeacher = teachers[pos]

                    if (myCheckBox.isChecked) {
                        currentTeacher.isSelected = true
                        checkedTeachers.add(currentTeacher)
                    } else if (!myCheckBox.isChecked) {
                        currentTeacher.isSelected = false
                        checkedTeachers.remove(currentTeacher)
                    }
                }
            })
        }

        override fun getItemCount(): Int {
            return teachers.size
        }

        internal class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {

            var nameTxt: TextView
            var myCheckBox: CheckBox

            lateinit var myItemClickListener: ItemClickListener

            init {

                nameTxt = itemView.findViewById(R.id.nameTextView)
                myCheckBox = itemView.findViewById(R.id.myCheckBox)

                myCheckBox.setOnClickListener(this)
            }

            fun setItemClickListener(ic: ItemClickListener) {
                this.myItemClickListener = ic
            }

            override fun onClick(v: View) {
                this.myItemClickListener.onItemClick(v, layoutPosition)
            }

            internal interface ItemClickListener {

                fun onItemClick(v: View, pos: Int)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_list)

        adapter = MyAdapter(this, teachers)

        fab.setOnClickListener {
            sb = StringBuilder()

            var i = 0
            do {
                val spiritualTeacher = adapter.checkedTeachers[i]
                sb!!.append(spiritualTeacher.name)
                if (i != adapter.checkedTeachers.size - 1) {
                    sb!!.append(", ")
                }
                i++

            } while (i < adapter.checkedTeachers.size)

            if (adapter.checkedTeachers.size > 0) {
                selectedHealthIssues = sb!!.toString()
                Toast.makeText(this@CheckListActivity, sb!!.toString(), Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(
                    this@CheckListActivity,
                    "Please Check An Item First",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        //RECYCLER
        val rv = findViewById(R.id.myRecycler) as RecyclerView
        rv.layoutManager = LinearLayoutManager(this)
        rv.itemAnimator = DefaultItemAnimator()

        //SET ADAPTER
        rv.adapter = adapter

    }
// end
}
