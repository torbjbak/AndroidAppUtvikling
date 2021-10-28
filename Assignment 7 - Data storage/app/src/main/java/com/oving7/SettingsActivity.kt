package com.oving7

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.oving7.databinding.SettingsActivityBinding
import com.oving7.managers.MyPreferenceManager

class SettingsActivity :
    AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener,
    Preference.SummaryProvider<ListPreference> {

    private lateinit var settingsLayout: SettingsActivityBinding
    private lateinit var preferenceManager: MyPreferenceManager
    private var color = R.color.white

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsLayout = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(settingsLayout.root)

        preferenceManager = MyPreferenceManager(this)
        preferenceManager.registerListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
        }

        settingsLayout.button.setOnClickListener {
            setResult(RESULT_OK, Intent().putExtra("color", color))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceManager.unregisterListener(this)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        if (p1 == getString(R.string.color_selection))
            color = preferenceManager.updateColorTheme()
    }

    override fun provideSummary(preference: ListPreference?): CharSequence {
        return when (preference?.key) {
            getString(R.string.color_selection) -> preference.entry
            else -> "Unknown Preference"
        }
    }
}