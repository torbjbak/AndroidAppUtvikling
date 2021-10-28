package com.oving7.managers

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.oving7.MainActivity
import com.oving7.R

class MyPreferenceManager(activity: AppCompatActivity) {
    private val resources = activity.resources
    private val preferences =
        PreferenceManager.getDefaultSharedPreferences(activity)


    fun updateColorTheme(): Int {
        val colorThemeValues = resources.getStringArray(R.array.color_selection_values)
        val value = getString(
            resources.getString(R.string.color_selection),
            resources.getString(R.string.color_selection_default_value)
        )

        when (value) {
            colorThemeValues[0] ->
                return R.color.white
            colorThemeValues[1] ->
                return R.color.orange
            colorThemeValues[2] ->
                return R.color.pink
        }
        return 0
    }

    fun registerListener(activity:
                         SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(activity)
    }

    fun unregisterListener(activity:
                           SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.unregisterOnSharedPreferenceChangeListener(activity)
    }

    private fun getString(key: String, defaultValue: String): String {
        val ret = preferences.getString(key, defaultValue) ?: defaultValue
        Log.i("getString()", "Key: $key Default: $defaultValue Return: $ret")
        return ret
    }
}
