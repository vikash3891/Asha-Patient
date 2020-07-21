package com.home.asharemedy.video.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.home.asharemedy.AshaRemedyApp

fun showKeyboard(editText: EditText) {
    val imm = AshaRemedyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun hideKeyboard(editText: EditText) {
    val imm = AshaRemedyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(editText.windowToken, 0)
}