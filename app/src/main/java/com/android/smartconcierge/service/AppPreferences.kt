package com.android.smartconcierge.service

import android.content.Context

class AppPreferences(private val context: Context) {

    companion object {
        const val APP_PREFERENCES = "app_preferences"
        private const val DEFAULT_INT_VALUE = 0
        private val DEFAULT_STR_VALUE = null
    }

    fun savePref(key: String, value: String) {
        getPrefsEditor().putString(key, value).apply()
    }

    fun savePref(key: String, value: Int) {
        getPrefsEditor().putInt(key, value).apply()
    }

    fun getPrefString(key: String) = getAppPreferences().getString(key, DEFAULT_STR_VALUE)

    fun getPrefInt(key: String) = getAppPreferences().getInt(key, DEFAULT_INT_VALUE)

    fun removePref(key: String) {
        getPrefsEditor().remove(key).apply()
    }

    private fun getPrefsEditor() =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).edit()

    private fun getAppPreferences() =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    object PreferencesKeys {
        const val USER_TOKEN = "token"
    }
}

