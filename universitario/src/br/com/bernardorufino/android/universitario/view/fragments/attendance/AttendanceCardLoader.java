package br.com.bernardorufino.android.universitario.view.fragments.attendance;

import android.content.Context;
import br.com.bernardorufino.android.universitario.libs.loader.AsyncDataLoader;
import br.com.bernardorufino.android.universitario.model.attendance.Attendance;
import br.com.bernardorufino.android.universitario.model.attendance.AttendanceProvider;
import br.com.bernardorufino.android.universitario.libs.observing.Observable;
import br.com.bernardorufino.android.universitario.libs.observing.Observer;

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
        onContentChanged();
    }

    @Override
    protected void releaseResources(List<Attendance> data) {
        /* No-op */
    }
}
