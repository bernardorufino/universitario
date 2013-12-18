package br.com.bernardorufino.android.universitario.definitions;

public class Definitions {

    // Copacabana, Rio de Janeiro, Brazil =)
    public static final String NAMESPACE = Definitions.class.getPackage().getName();
    public static final String LOG_TAG = "UNIVERSITARIO";

    public static class Storage {

        public static final String PREFERENCES = "globalPreferences";
        public static final String USERNAME = "userName";

        // Prevents instantiation
        private Storage() {
            throw new AssertionError("Cannot instantiate object from " + this.getClass());
        }
    }

    // Prevents instantiation
    private Definitions() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}