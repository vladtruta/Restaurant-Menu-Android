package com.vladtruta.restaurantmenu.presentation.settings

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.vladtruta.restaurantmenu.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        initResetDevicePreference()
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