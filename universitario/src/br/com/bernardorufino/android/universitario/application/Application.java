package br.com.bernardorufino.android.universitario.application;

import android.preference.PreferenceManager;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        for (int resourceId : Facets.PREFERENCES) {
            PreferenceManager.setDefaultValues(this, resourceId, false);
        }
    }
}
