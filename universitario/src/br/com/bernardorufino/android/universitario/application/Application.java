package br.com.bernardorufino.android.universitario.application;

import br.com.bernardorufino.android.universitario.helpers.Helper;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Helper.log("Application.onCreate()");
    }
}
