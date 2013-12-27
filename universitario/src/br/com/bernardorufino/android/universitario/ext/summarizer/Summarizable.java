package br.com.bernardorufino.android.universitario.ext.summarizer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public interface Summarizable {

    public static final NumberFormat FORMATTER = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

    public String getSummaryValue();
}
