package br.com.bernardorufino.android.universitario.application;

public class Definitions {

    public static final String APP_NAME = "Universitário";
    public static final String SAFE_APP_NAME = "Universitario";
    public static final String APP_ID = Application.class.getName();
    public static final String NAMESPACE = Application.class.getPackage().getName();
    public static final String LOG_TAG = SAFE_APP_NAME.toUpperCase();

    public static class Domain {

        public static final double ABSENCE_WARNING_THRESHOLD = 0.8;
    }

    public static class Storage {

        public static final String PREFERENCES = "globalPreferences";
        public static final String USERNAME = "userName";
    }
}