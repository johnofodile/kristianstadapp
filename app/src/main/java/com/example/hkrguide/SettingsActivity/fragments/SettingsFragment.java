package com.example.hkrguide.SettingsActivity.fragments;

import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.hkrguide.R;
import com.example.hkrguide.SettingsActivity.util.DataStore;
import com.example.hkrguide.Utilities;

public class SettingsFragment extends PreferenceFragmentCompat {

    // Object Instances
    private DataStore dataStore;

    // Preference Change Listeners
    // Update Period
    private Preference.OnPreferenceChangeListener updatePeriodListener = (preference, newValue) -> {
        // Do Nothing
        return true;
    };

    // Night Mode
    private Preference.OnPreferenceChangeListener nightModeListener = (preference, newValue) -> {
        Utilities.setNightMode(getResources().getConfiguration(), (Boolean) newValue);
        return true;
    };

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        dataStore = new DataStore(requireContext());

        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setPreferenceDataStore(dataStore);

        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        findPreference("update_period").setOnPreferenceChangeListener(updatePeriodListener);
        findPreference("night_mode").setOnPreferenceChangeListener(nightModeListener);
    }
}