package br.com.bernardorufino.android.universitario.helpers;


import android.util.Log;
import br.com.bernardorufino.android.universitario.R;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicLong;

import static br.com.bernardorufino.android.universitario.definitions.Definitions.*;

public class Helper {

    public static final AtomicLong timer = new AtomicLong(System.nanoTime());

//    public static boolean hasPlayServices(Context context) {
//        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
//    }

    public static void log(String message) {
        Log.d(LOG_TAG, message);
    }

    public static void logException(Exception e) {
        if (e != null) Log.e(LOG_TAG, e.toString(), e);
        else Log.d(LOG_TAG, "Logging null exception");
    }

    public static long time() {
        long window, now = System.nanoTime();
        synchronized (timer) {
            window = now - timer.get();
            timer.set(now);
        }
        return window;
    }

    public static long millisTime() {
        return time() / 1000000;
    }

    public static boolean isNetworkException(Exception e) {
        return (e instanceof UnknownHostException)
            || (e instanceof ConnectException);
    }

    public static int errorMessage(Exception e) {
        return isNetworkException(e) ? R.string.network_error : R.string.generic_error;
    }

    // Prevents instantiation
    private Helper() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
