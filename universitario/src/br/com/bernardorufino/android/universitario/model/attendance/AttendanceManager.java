package br.com.bernardorufino.android.universitario.model.attendance;

import br.com.bernardorufino.android.universitario.libs.observing.AbstractObservable;
import br.com.bernardorufino.android.universitario.model.course.Course;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class AttendanceManager {

    private static class InstanceHolder {
        private static final AttendanceManager INSTANCE = new AttendanceManager();
    }

    public static AttendanceManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private Collection<AbstractAttendanceProvider> mAttendanceProviders = new LinkedList<>();

    // Prevents outside instantiation
    private AttendanceManager() {

    }

    public AttendanceProvider getAttendanceProvider(/* TODO: Refine search with ordering or selects */) {
        AbstractAttendanceProvider provider = new AbstractAttendanceProvider() {
            @Override
            public List<Attendance> getAttendances() {
                /* TODO: Implement */
                return Lists.newArrayList(
                        new Attendance(new Course("CES-11", "Armando", 6*16), 8),
                        new Attendance(new Course("CES-22", "Paulo Andre", 5*16), 10),
                        new Attendance(new Course("EEA-45", "Douglas", 3*16), 8.5),
                        new Attendance(new Course("ELE-55", "Duarte", 8*16), 3.5),
                        new Attendance(new Course("ELE-31", "Manish", 6*16), 4.5),
                        new Attendance(new Course("CES-30", "Parente", 6*16), 11),
                        new Attendance(new Course("CES-28", "Clovis", 5*16), 9)
                );
            }
        };
        mAttendanceProviders.add(provider);
        return provider;
    }

    private void notifyProviderObservers() {
        for (AbstractAttendanceProvider provider : mAttendanceProviders) provider.notifyObservers();
    }

    public void insert(Attendance attendance) {
        /* TODO: Implement */
        notifyProviderObservers();
    }

    public void remove(Attendance attendance) { // Maybe key in parameter
        /* TODO: Implement */
        notifyProviderObservers();
    }

    public void replace(Attendance attendance) {
        /* TODO: Implement */
        notifyProviderObservers();
    }

    private static abstract class AbstractAttendanceProvider extends AbstractObservable implements AttendanceProvider {

        /* For visibility issues */
        @Override
        protected void notifyObservers() {
            super.notifyObservers();
        }
    }

}
