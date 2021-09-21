package com.example.onik.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.onik.R


class MySettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        activity?.title = "Настройки"
    }

}