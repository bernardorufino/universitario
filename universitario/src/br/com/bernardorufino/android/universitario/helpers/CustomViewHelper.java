package br.com.bernardorufino.android.universitario.helpers;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewManager;
import android.view.ViewParent;
import android.widget.Toast;

import static br.com.bernardorufino.android.universitario.application.Definitions.NAMESPACE;
import static com.google.common.base.Preconditions.*;

public class CustomViewHelper {

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

    public static boolean tryMakeOrphan(View view) {
        ViewParent parent = view.getParent();
        if (parent instanceof ViewManager) {
            ((ViewManager) parent).removeView(view);
            return true;
        }
        return false;
    }

    public static <T extends View> T makeOrphan(T view) {
        checkArgument(tryMakeOrphan(view), "Can't make view orphan.");
        return view;
    }

    public static boolean isVisible(View view) {
        if (view.getVisibility() != View.VISIBLE) return false;
        while (view.getParent() instanceof View) {
            view = (View) view.getParent();
            if (view.getVisibility() != View.VISIBLE) return false;
        }
        return true;
    }

    // Prevents instantiation
    private CustomViewHelper() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
