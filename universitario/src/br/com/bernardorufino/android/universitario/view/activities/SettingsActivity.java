package br.com.bernardorufino.android.universitario.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import br.com.bernardorufino.android.universitario.R;
import br.com.bernardorufino.android.universitario.helpers.CustomViewHelper;
import br.com.bernardorufino.android.universitario.view.fragments.SettingsFragment;

import java.util.List;

public class SettingsActivity extends PreferenceActivity {

    public static final String PREFERENCES_PARAM = "preferences";

    public static Intent getIntentForPreferences(Context packageContext, String facet) {
        Intent intent = new Intent(packageContext, SettingsActivity.class);
        intent.putExtra(EXTRA_SHOW_FRAGMENT, SettingsFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putString(PREFERENCES_PARAM, facet);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, bundle);
        return intent;
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // hasHeaders() not working
                boolean hasHeaders = CustomViewHelper.isVisible(getListView());
                if (hasHeaders) {
                    NavUtils.navigateUpFromSameTask(this);
                } else {
                    startActivity(new Intent(this, SettingsActivity.class));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}