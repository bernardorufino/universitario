package br.com.bernardorufino.android.universitario.definitions;

public class Definitions {

    public static final String NAMESPACE = Definitions.class.getPackage().getName();
    public static final String LOG_TAG = "UNIVERSITARIO";

    public static class Domain {

        public static final double ABSENCE_WARNING_THRESHOLD = 0.8;
    }

    public static class Storage {

        public static final String PREFERENCES = "globalPreferences";
        public static final String USERNAME = "userName";
    }
}