package com.delixha.githubuser

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
//import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
//import com.delixha.githubuser.ui.home.HomeFragment
import com.delixha.githubuser.ui.setting.SettingPreferences
import com.delixha.githubuser.ui.setting.SettingViewModel
import com.delixha.githubuser.utils.SettingViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val mFragmentManager = supportFragmentManager
//        val mHomeFragment = HomeFragment()
//        val fragment = mFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)
//
//        if (fragment !is HomeFragment) {
//            mFragmentManager.commit {
//                add(R.id.frame_contrainer, mHomeFragment, HomeFragment::class.java.simpleName)
//            }
//        }

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}