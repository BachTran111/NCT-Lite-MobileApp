package com.example.nct_lite.data

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "nct_session"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_ROLE = "user_role"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveAuth(context: Context, token: String, role: String) {
        prefs(context).edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_ROLE, role)
            .apply()
    }

    fun getToken(context: Context): String? = prefs(context).getString(KEY_TOKEN, null)

    fun getRole(context: Context): String? = prefs(context).getString(KEY_ROLE, null)

    fun clear(context: Context) {
        prefs(context).edit().clear().apply()
    }
}
