package com.mozzarelly.cbthelper

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        title = "Settings"

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.contentFragment, SettingsFragment())
            .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private val sharedPrefs by lazy {
            requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE) ?: error("Can't get prefs.")
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            findPreference<SwitchPreferenceCompat>("dark")?.run {
                isChecked = sharedPrefs.getOrInit("dark"){
                    (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
                }

                onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, setting ->
                    sharedPrefs["dark"] = setting as Boolean

                    if (setting)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    else
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                    true
                }
            }

            findPreference<Preference>("wipe")?.run {
                setOnPreferenceClickListener {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Delete all?")
                        .setMessage("Delete all saved entries and analyses? Cannot be undone.")
                        .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                        .setPositiveButton(R.string.ok) { dialog, _ ->
//                            lifecycleScope.launch {
                                CBTDatabase.getDatabase(requireContext()).clearAllTables()
                                dialog.dismiss()
//                            }
                        }
                        .show()

                    true
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}