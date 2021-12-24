package com.joaquim.joaquim_teste.data.commom

import android.content.Context
import android.content.SharedPreferences
import com.joaquim.joaquim_teste.MyApplication.Companion.globalContext

private const val PREFS_KEY = "APP_PREFS"
const val USER_UID = "USER_UID"

open class SharedPrefs {

    companion object {

        private var sharedPrefs: SharedPreferences? = globalContext.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

        fun getPrefs(context: Context): SharedPreferences {

            if (sharedPrefs == null) {
                sharedPrefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
            }
            return checkNotNull(sharedPrefs)
        }

        fun setUserData(key: String, value: String) {
            getPrefs(globalContext).edit().putString(key, value).apply()
        }

        fun getUserData(key: String?): String {
            return getPrefs(globalContext).getString(key, "") ?: ""
        }

        fun clearSharedPreferences() {
            getPrefs(globalContext).edit().clear().apply()
        }

    }
}