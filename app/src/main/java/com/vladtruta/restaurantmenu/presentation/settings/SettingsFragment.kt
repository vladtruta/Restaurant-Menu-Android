package com.vladtruta.restaurantmenu.presentation.settings

import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.utils.SessionUtils

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        initTableNamePreference()
        initWaiterPasswordPreference()
        initResetDevicePreference()
    }

    private fun initTableNamePreference() {
        val tableNamePreference = findPreference<EditTextPreference>("table_name")

        tableNamePreference?.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
            it.setSelection(it.text.length)
        }
    }

    private fun initWaiterPasswordPreference() {
        val waiterPasswordPreference = findPreference<EditTextPreference>("waiter_password")

        waiterPasswordPreference?.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            it.setSelection(it.text.length)
        }
    }

    private fun initResetDevicePreference() {
        val resetDevicePreference = findPreference<Preference>("reset_device")
        resetDevicePreference?.intent = SessionUtils.getResetIntent()
    }
}