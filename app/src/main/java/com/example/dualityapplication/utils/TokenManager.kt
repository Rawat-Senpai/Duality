package com.example.dualityapplication.utils

import javax.inject.Inject
import android.content.Context
import android.content.SharedPreferences

import dagger.hilt.android.qualifiers.ApplicationContext


class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(Util.PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(Util.USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(Util.USER_TOKEN, "")
    }

    fun saveId(token: String) {
        val editor = prefs.edit()
        editor.putString(Util.USER_ID, token)
        editor.apply()
    }

    fun getId(): String? {
        return prefs.getString(Util.USER_ID, null)
    }

}