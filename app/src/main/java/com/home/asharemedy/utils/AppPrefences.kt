package com.home.asharemedy.utils

import android.content.Context
import com.google.gson.Gson

object AppPrefences {

    private val PREFS_FILE_NAME = "Trackle"
    private val Login = "login"
    private val RememberMe = "RememberMe"
    private val UserID = "UserID"
    private val Password = "Password"

    private val UserDetails = "user_details"
    private val UserAddress = "user_address"
    private val UserName = "user_name"


    fun setUserName(ctx: Context, data: String) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(UserName, data)
        editor.commit()
    }

    fun getUserName(ctx: Context): String? {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .getString(UserName, "")
    }

    fun setUserAddress(ctx: Context, data: String) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(UserAddress, data)
        editor.commit()
    }

    fun getUserAddress(ctx: Context): String? {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .getString(UserAddress, "")
    }

    fun getLogin(ctx: Context): Boolean? {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .getBoolean(Login, false)
    }

    fun setLogin(ctx: Context, data: Boolean) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putBoolean(Login, data)
        editor.commit()
    }

    fun getRememberMe(ctx: Context): Boolean? {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .getBoolean(RememberMe, false)
    }

    fun setRememberMe(ctx: Context, data: Boolean) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putBoolean(RememberMe, data)
        editor.commit()
    }

    fun getUserID(ctx: Context): String {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .getString(UserID, "")
    }

    fun setUserID(ctx: Context, data: String) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(UserID, data)
        editor.commit()
    }

    fun getPassword(ctx: Context): String {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .getString(Password, "")
    }

    fun setPassword(ctx: Context, data: String) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(Password, data)
        editor.commit()
    }

}