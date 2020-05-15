package com.vladtruta.restaurantmenu.presentation.settings

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.vladtruta.restaurantmenu.R

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
            it.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            it.setSelection(it.text.length)
        }
    }

    private fun initResetDevicePreference() {
        val resetDevicePreference = findPreference<Preference>("reset_device")
        val intent =
            requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
                ?: return
        intent.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        resetDevicePreference?.intent = intent
    }
}