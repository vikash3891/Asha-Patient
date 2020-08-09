package com.home.asharemedy.chat.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.home.asharemedy.R
import com.home.asharemedy.chat.utils.isDialogNameValid

private const val CLICK_DELAY = 1000L

class NewGroupActivity : BaseActivity() {

    private lateinit var menu: Menu
    private lateinit var etDialogName: EditText
    private lateinit var tvDialogNameHint: TextView
    private lateinit var ivClear: ImageView
    private var lastClickTime = 0L

    companion object {
        fun startForResult(activity: Activity, code: Int) {
            val intent = Intent(activity, NewGroupActivity::class.java)
            activity.startActivityForResult(intent, code)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_enter_chat_name)
            etDialogName = findViewById(R.id.et_dialog_name)
            tvDialogNameHint = findViewById(R.id.tv_group_name_hint)
            ivClear = findViewById(R.id.iv_clear)
            etDialogName.addTextChangedListener(TextWatcherListener(etDialogName))
            ivClear.setOnClickListener {
                etDialogName.setText("")
            }
            supportActionBar?.title = getString(R.string.select_users_create_chat_title)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_activity_new_group, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        try {
            if (SystemClock.uptimeMillis() - lastClickTime < CLICK_DELAY) {
                return super.onOptionsItemSelected(item)
            }
            lastClickTime = SystemClock.uptimeMillis()

            when (item.itemId) {
                R.id.menu_finish -> {
                    passResultToCallerActivity()
                }
                else -> return super.onOptionsItemSelected(item)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    private fun passResultToCallerActivity() {
        try {
            val dialogName = etDialogName.text.toString().trim { it <= ' ' }
            val intent = Intent()
            if (!TextUtils.isEmpty(dialogName)) {
                intent.putExtra(EXTRA_CHAT_NAME, dialogName)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun validateFields() {
        try {
            menu.getItem(0).isVisible = isDialogNameValid(etDialogName)
            tvDialogNameHint.visibility = if (isDialogNameValid(etDialogName)) {
                View.GONE
            } else {
                View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inner class TextWatcherListener(private var editText: EditText) : TextWatcher {

        override fun beforeTextChanged(
            charSequence: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {

        }

        override fun onTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            try {
                val text = charSequence.toString().replace("  ", " ")
                if (editText.text.toString() != text) {
                    editText.setText(text)
                    editText.setSelection(text.length)
                }
                validateFields()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }
}