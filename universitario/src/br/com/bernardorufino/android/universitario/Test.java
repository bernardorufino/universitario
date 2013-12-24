package br.com.bernardorufino.android.universitario;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class Test {

    public static void main(String[] args) {
        NumberFormat f = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
        double[] values = { 17, 18.5, 18.23, 24.2103, 90.00, 100.1, 100.3, 0.5, 1, 1.5, 1.25, 2.4, 0, 2, 4, 1.2345 };
        for (double value : values) {
            System.out.println(value + " = " + f.format(value));
        }
    }
}
