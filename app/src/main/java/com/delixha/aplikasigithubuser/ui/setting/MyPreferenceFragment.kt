package com.delixha.aplikasigithubuser.ui.setting

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.*
import com.delixha.aplikasigithubuser.R
import com.delixha.aplikasigithubuser.data.receiver.AlarmReceiver
import java.util.*

class MyPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var reminder: String
    private lateinit var reminderPreference: SwitchPreferenceCompat

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
    }

    private fun init() {
        val LANGUAGE = resources.getString(R.string.key_change_language)
        reminder = resources.getString(R.string.key_reminder)

        val changeLanguagePreference = findPreference<PreferenceScreen>(LANGUAGE) as PreferenceScreen
        changeLanguagePreference.summary = resources.getString(R.string.language).format(Locale.getDefault().displayLanguage)
        changeLanguagePreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            true
        }

        reminderPreference = findPreference<SwitchPreferenceCompat>(reminder) as SwitchPreferenceCompat
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val alarmReceiver = AlarmReceiver()
        if (key == reminder) {
            if (reminderPreference.isChecked) {
                val repeatTitle = resources.getString(R.string.title_reminder)
                val repeatTime = resources.getString(R.string.time_reminder)
                val repeatMessage = resources.getString(R.string.message_reminder)
                activity?.let { alarmReceiver.setRepeatingAlarm(it, repeatTitle, repeatTime, repeatMessage) }
            } else {
                activity?.let { alarmReceiver.cancelAlarm(it) }
            }
        }
    }
}