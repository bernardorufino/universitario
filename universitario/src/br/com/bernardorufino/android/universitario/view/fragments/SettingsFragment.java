package br.com.bernardorufino.android.universitario.view.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import br.com.bernardorufino.android.universitario.application.Facets;
import br.com.bernardorufino.android.universitario.ext.summarizer.Summarizer;
import br.com.bernardorufino.android.universitario.view.activities.SettingsActivity;

import static com.google.common.base.Preconditions.*;

public class SettingsFragment extends PreferenceFragment {

    private static final String SUMMARY_PLACEHOLDER = "?"; // Will be replaced by the current preference value

    private Summarizer mSummarizer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        checkState(arguments.containsKey(SettingsActivity.PREFERENCES_PARAM),
                "preferences fragment invoked without a resource id");
        String facet = arguments.getString(SettingsActivity.PREFERENCES_PARAM);
        int id = Facets.fromString(facet);
        int resourceId = Facets.getPreferencesResourceId(id);
        addPreferencesFromResource(resourceId);
        SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();
        prefs.registerOnSharedPreferenceChangeListener(mOnSharedPreferencesChange);
        mSummarizer = new Summarizer(getPreferenceScreen(), SUMMARY_PLACEHOLDER);
    }

    private final SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferencesChange = new SharedPreferences
            .OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference preference = findPreference(key);
            mSummarizer.summarize(preference);
        }
    };
}
