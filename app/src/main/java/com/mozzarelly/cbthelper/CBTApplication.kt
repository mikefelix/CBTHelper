package com.mozzarelly.cbthelper

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.jakewharton.threetenabp.AndroidThreeTen

@Suppress("unused") // It's not, check the manifest.
class CBTApplication : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        CBTDatabase.getDatabase(this)

        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE) ?: error("Can't get prefs.")
        val dark = prefs.getOrInit("dark"){
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        }

        AppCompatDelegate.setDefaultNightMode(
            if (dark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}