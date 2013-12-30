package br.com.bernardorufino.android.universitario.application;

import android.support.v4.app.Fragment;
import br.com.bernardorufino.android.universitario.R;
import br.com.bernardorufino.android.universitario.view.fragments.CalendarFragment;
import br.com.bernardorufino.android.universitario.view.fragments.ReportFragment;
import br.com.bernardorufino.android.universitario.view.fragments.attendance.AttendanceFragment;
import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.*;

public final class Facets {

    public static int ATTENDANCE = 0;
    public static int CALENDAR = 1;
    public static int REPORT = 2;

    public static final List<String> STRINGS = ImmutableList.of(
            "attendance",
            "calendar",
            "report"
    );

    /* Change here, change in strings */
    public static final List<String> LABELS = ImmutableList.of(
            "Faltas",
            "Trabalhos",
            "Notas"
    );

    public static final List<? extends Class<? extends Fragment>> FRAGMENTS = ImmutableList.of(
            AttendanceFragment.class,
            CalendarFragment.class,
            ReportFragment.class
    );

    public static final List<Integer> PREFERENCES = ImmutableList.of(
            R.xml.preferences_attendance,
            R.xml.preferences_calendar,
            R.xml.preferences_report
    );

    public static int fromString(String string) {
        return STRINGS.indexOf(checkNotNull(string).trim().toLowerCase());
    }

    public static String getString(int facet) {
        return STRINGS.get(facet);
    }

    public static Class<? extends Fragment> getFragment(int facet) {
        return FRAGMENTS.get(facet);
    }

    public static String getLabel(int facet) {
        return LABELS.get(facet);
    }

    public static int getPreferencesResourceId(int facet) {
        return PREFERENCES.get(facet);
    }

    // Prevents instantiation
    private Facets() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
