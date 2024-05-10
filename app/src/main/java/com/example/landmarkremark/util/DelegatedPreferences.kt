package com.example.landmarkremark.util
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.landmarkremark.R


class DelegatedPreferences<T>(private val context: Context, private val key: String, private val defaultValue: T) {

    private val prefs: SharedPreferences by lazy { context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE) }

    fun getValue(): T {
        return findPreferences(key, defaultValue)
    }

    fun setValue(value: T) {
        savePreference(key, value)
    }

    @Suppress("UNCHECKED_CAST")
    private fun findPreferences(key: String, defaultValue: T): T {
        with(prefs)
        {
            val result: Any = when (defaultValue) {
                is Boolean -> getBoolean(key, defaultValue)
                is Int -> getInt(key, defaultValue)
                is Long -> getLong(key, defaultValue)
                is Float -> getFloat(key, defaultValue)
                is String -> getString(key, defaultValue).toString()
                else -> throw IllegalArgumentException()
            }
            return result as T
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun savePreference(key: String, value: T) {
        with(prefs.edit())
        {
            when (value) {
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException()
            }.apply()
        }
    }
}