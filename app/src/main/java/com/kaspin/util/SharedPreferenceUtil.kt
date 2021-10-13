package com.kaspin.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtil(context: Context) {

    var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences("kaspin-prefs", Context.MODE_PRIVATE)
    }

    fun put(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).commit()
    }
    fun put(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).commit()
    }
    fun put(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).commit()
    }
    fun put(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).commit()
    }
    fun getString(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }
    fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }
    fun getFloat(key: String): Float {
        return sharedPreferences.getFloat(key, 0f)
    }
    fun getBool(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }
    fun deleteSavedData(key: String) {
        sharedPreferences.edit().remove(key).commit()
    }
}