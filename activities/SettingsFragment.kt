package com.example.assignmenttest3.activities
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.assignmenttest3.R

//Settings Fragment - this fragment extends the class PreferenceFragmentCompact that will hold our shared preferences

class SettingsFragment: PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    //code adapted from official Android developers documents
    //https://developer.android.com/reference/androidx/preference/PreferenceFragmentCompat
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)

        val userEmailPreference = findPreference<EditTextPreference>("user_email")
        userEmailPreference?.onPreferenceChangeListener = this

        val userNamePreference = findPreference<EditTextPreference>("user_name")
        userNamePreference?.onPreferenceChangeListener = this

        // Set the summary to the current value
        userEmailPreference?.summary = userEmailPreference?.text ?: ""
        userNamePreference?.summary = userNamePreference?.text ?: ""
    }

    //code adapted from JavaCourseDrive, Android Tutorials by JavaCourseDrive - Preference Change Listener | Shared Preferences (2021)
    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        preference.summary = newValue.toString()
        return true
    }
}
