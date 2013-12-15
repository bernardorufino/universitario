package br.com.bernardorufino.android.universitario.helpers;


import android.content.Context;
import android.widget.Toast;

import static br.com.bernardorufino.android.universitario.definitions.Definitions.NAMESPACE;

public class ViewHelper {

    public static void flash(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void flash(Context context, int stringResource) {
        String message = context.getResources().getString(stringResource);
        flash(context, message);
    }

    public static String withNamespace(String string) {
        return NAMESPACE + "." + string;
    }

    // Prevents instantiation
    private ViewHelper() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
