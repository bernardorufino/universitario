package br.com.bernardorufino.android.universitario.view.fragments.attendance;

import android.content.Context;
import br.com.bernardorufino.android.universitario.ext.loader.AsyncDataLoader;
import br.com.bernardorufino.android.universitario.ext.observing.Observable;
import br.com.bernardorufino.android.universitario.ext.observing.Observer;
import br.com.bernardorufino.android.universitario.helpers.Helper;
import br.com.bernardorufino.android.universitario.model.attendance.Attendance;
import br.com.bernardorufino.android.universitario.model.attendance.AttendanceProvider;

import java.util.List;

import static com.google.common.base.Preconditions.*;

public class AttendanceCardLoader extends AsyncDataLoader<List<Attendance>> implements Observer {

    private final AttendanceProvider mAttendanceProvider;

    public AttendanceCardLoader(Context context, AttendanceProvider attendanceProvider) {
        super(context);
        mAttendanceProvider = attendanceProvider;
    }

    @Override
    public List<Attendance> loadInBackground() {
        Helper.log("Requesting new attendance list and redrawing...");
        return mAttendanceProvider.getAttendances();
    }

    @Override
    protected void startObserving() {
        mAttendanceProvider.registerObserver(this);
    }

    @Override
    protected void stopObserving() {
        mAttendanceProvider.unregisterObserver(this);
    }

    @Override
    public void onChange(Observable subject) {
        // Sanity checking
        checkState(subject == mAttendanceProvider, "Observable received should be the attendance provider.");
        Helper.log("Model change detected");
        onContentChanged();
    }

    @Override
    protected void releaseResources(List<Attendance> data) {
        /* No-op */
    }
}
