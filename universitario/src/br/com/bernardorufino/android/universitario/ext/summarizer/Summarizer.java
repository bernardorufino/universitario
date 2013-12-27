package br.com.bernardorufino.android.universitario.ext.summarizer;

import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import br.com.bernardorufino.android.universitario.helpers.Helper;

import java.util.HashMap;
import java.util.Map;

public class Summarizer {

    private final Map<Preference, String> mSummaryPatterns = new HashMap<>();
    private final String mPlaceholder;

    public Summarizer(Preference prefs, String placeholder) {
        mPlaceholder = placeholder;
        summarize(prefs);
    }

    public void summarize(Preference pref) {
        if (pref instanceof PreferenceGroup) {
            summarizeGroup((PreferenceGroup) pref);
            return;
        }
        CharSequence value = null;
        if (pref instanceof Summarizable) {
            value = ((Summarizable) pref).getSummaryValue();
            Helper.log("Its summarizable and value = " + value);
        } else if (pref instanceof ListPreference) {
            value = ((ListPreference) pref).getEntry();
        } else if (pref instanceof EditTextPreference) {
            value = ((EditTextPreference) pref).getText();
        }
        /* CheckBoxPreference already has its android:summary{On,Off} options */
        if (value == null) return;
        replace(pref, value);
    }

    private void replace(Preference pref, CharSequence value) {
        String pattern = mSummaryPatterns.get(pref);
        if (pattern == null) {
            pattern = pref.getSummary().toString();
            mSummaryPatterns.put(pref, pattern);
        }
        String summary = pattern.replace(mPlaceholder, value);
        pref.setSummary(summary);
    }

    private void summarizeGroup(PreferenceGroup prefs) {
        for (int i = 0, n = prefs.getPreferenceCount(); i < n; i++) {
            Preference pref = prefs.getPreference(i);
            summarize(pref);
        }
    }
}
