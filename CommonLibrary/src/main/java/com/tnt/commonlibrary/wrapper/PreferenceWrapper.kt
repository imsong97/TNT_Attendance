package com.tnt.commonlibrary.wrapper

import android.app.Activity
import android.content.Context

class PreferenceWrapper(private val context: Context) {
    private val PREF_NAME = "TNT_APP_PREF"
    private val pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE)

    // keys
    private val USER_ID = "USER_ID" // id
    private val PASSWORD = "PW" // pw

    fun existUserId(): Boolean = getUserId().isNotEmpty()
    fun getUserId(): String = pref.getString(USER_ID, "") ?: ""
    fun setUserId(id: String) {
        pref.edit().putString(USER_ID, id).apply()
    }

    fun existPassword(): Boolean = getPassword().isNotEmpty()
    fun getPassword(): String = pref.getString(PASSWORD, "") ?: ""
    fun setPassword(pw: String) {
        pref.edit().putString(PASSWORD, pw).apply()
    }
}