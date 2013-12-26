package br.com.bernardorufino.android.universitario.view.components;

import android.content.Context;
import br.com.bernardorufino.android.universitario.R;
import br.com.bernardorufino.android.universitario.application.Definitions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class AbsencesBarHelper {

    public static int getColorResourceId(double current, int total) {
        return (current < total * Definitions.Domain.ABSENCE_WARNING_THRESHOLD) ? R.color.absence_ok
             : (current < total) ? R.color.absence_warning
             : R.color.absence_overflow;
    }

    public static int getColor(Context context, double current, int total) {
        return context.getResources().getColor(getColorResourceId(current, total));
    }

    private static final NumberFormat FORMATTER = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

    public static String getStatusText(double current, int total) {
        return FORMATTER.format(current) + " / " + total;
    }

    // Prevents instantiation
    private AbsencesBarHelper() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
