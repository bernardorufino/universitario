package br.com.bernardorufino.android.universitario.application;

import com.facebook.rebound.SpringConfig;

/* TODO: <FEATURE> Make most alarming absences up in the list  */
public final class Definitions {

    public static final String APP_NAME = "Universitário";
    public static final String SAFE_APP_NAME = "Universitario";
    public static final String APP_ID = Application.class.getName();
    public static final String NAMESPACE = Application.class.getPackage().getName();
    public static final String LOG_TAG = SAFE_APP_NAME.toUpperCase();

    public static final SpringConfig SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(45, 4);

    public static class Domain {

        public static final double ABSENCE_WARNING_THRESHOLD = 0.8;
    }

    public static class Storage {

        public static final String PREFERENCES = "globalPreferences";
        public static final String USERNAME = "userName";
    }

    /* Change here, change in xml/preferences_<facet> */
    public static class Preferences {

        public static class Attendance {

            public static final String TOTAL_ABSENCES_MULTIPLIER = "attendance_total_absences_multiplier";
        }

        public static class Calendar {
            /* Empty */
        }

        public static class Report {
            /* Empty */
        }

    }

    // Prevents instantiation
    private Definitions() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}