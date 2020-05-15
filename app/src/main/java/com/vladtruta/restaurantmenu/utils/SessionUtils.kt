package com.vladtruta.restaurantmenu.utils

import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object SessionUtils {
    private const val KEY_TABLE_NAME = "table_name"
    private const val DEFAULT_TABLE_NAME = "1"

    private const val KEY_WAITER_PASSWORD = "waiter_password"
    private const val DEFAULT_WAITER_PASSWORD = "1234"

    private const val EMERGENCY_PASSWORD = "6548"

    private fun getSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(RestaurantApp.instance)
    }

    fun getTableName(): String {
        return getSharedPreferences().getString(KEY_TABLE_NAME, DEFAULT_TABLE_NAME)
            ?: DEFAULT_TABLE_NAME
    }

    fun getWaiterPassword(): String {
        return getSharedPreferences().getString(KEY_WAITER_PASSWORD, DEFAULT_WAITER_PASSWORD)
            ?: DEFAULT_WAITER_PASSWORD
    }

    fun getEmergencyPassword(): String {
        return EMERGENCY_PASSWORD
    }
}