package br.com.bernardorufino.android.universitario.model.attendance;

import br.com.bernardorufino.android.universitario.ext.observing.Observable;

import java.util.List;

public interface AttendanceProvider extends Observable {

    public List<Attendance> getAttendances();
}
