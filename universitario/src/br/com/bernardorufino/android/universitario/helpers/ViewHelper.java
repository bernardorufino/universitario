package br.com.bernardorufino.android.universitario.helpers;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewManager;
import android.view.ViewParent;
import android.widget.Toast;

import static br.com.bernardorufino.android.universitario.application.Definitions.NAMESPACE;

public class ViewHelper {

    public static void flash(Fragment fragment, String message) {
        flash(fragment.getActivity(), message);
    }

    public static void flash(Fragment fragment, int stringResource) {
        flash(fragment.getActivity(), stringResource);
    }

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

    public static boolean makeOrphan(View view) {
        ViewParent parent = view.getParent();
        if (parent instanceof ViewManager) {
            ((ViewManager) parent).removeView(view);
            return true;
        }
        return false;
    }

    // Prevents instantiation
    private ViewHelper() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
