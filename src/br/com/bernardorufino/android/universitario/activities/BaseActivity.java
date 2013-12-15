package br.com.bernardorufino.android.universitario.activities;


import android.os.Bundle;
import roboguice.activity.RoboActivity;

public abstract class BaseActivity extends RoboActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeView(savedInstanceState);
        initializeListeners(savedInstanceState);
    }

    /*
    * Designed to be overridden for being automatically executed on creation
    * */
    protected void initializeView(Bundle savedInstanceState) { /* Override */ }

    /*
    * Designed to be overridden for being automatically executed on creation
    * */
    protected void initializeListeners(Bundle savedInstanceState) { /* Override */ }
}
