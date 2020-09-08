package com.home.asharemedy.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.home.asharemedy.R
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_mood.*
import java.util.*

class UserMoodActivity : BaseActivity() {

    private var moodId = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        moodDate.text = "" + day + " " + Utils.setMonth(month + 1) + ", " + year
        //moodDate.text = Utils.getDate()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.mood_cheerful -> {
                selectMood(R.drawable.mood_cheerful, "Cheerful", "1")
            }
            R.id.mood_happy -> {
                selectMood(R.drawable.mood_happy, "Happy", "2")
            }
            R.id.mood_neutral -> {
                selectMood(R.drawable.mood_neutral, "Neutral", "3")
            }
            R.id.mood_sad -> {
                selectMood(R.drawable.mood_sad, "Sad", "4")
            }
            R.id.mood_anxious -> {
                selectMood(R.drawable.mood_anxious, "Anxious", "5")
            }
            R.id.txt_skip -> {
                moodId = "3"
                startActivity(
                    Intent(
                        this@UserMoodActivity,
                        DashboardActivity::class.java
                    )
                )
                finish()
            }
            R.id.btn_submit -> {
                startActivity(
                    Intent(
                        this@UserMoodActivity,
                        DashboardActivity::class.java
                    )
                )
            }

        }
    }

    private fun selectMood(moodImg: Int, moodTxt: String, mood: String) {
        mood_selected?.setImageResource(moodImg)
        mood_text?.text = moodTxt
        moodId = mood
    }
}