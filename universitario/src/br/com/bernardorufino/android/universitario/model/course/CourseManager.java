package br.com.bernardorufino.android.universitario.model.course;

import br.com.bernardorufino.android.universitario.model.ModelManagers;
import br.com.bernardorufino.android.universitario.model.attendance.Attendance;
import br.com.bernardorufino.android.universitario.model.attendance.AttendanceManager;
import br.com.bernardorufino.android.universitario.model.base.ModelManager;

/* TODO: Maybe it's better not to have dependent triggers to create Attendance and change things in view... */
/* TODO: Notify attendance listeners on any change */
/* TODO: Create new attendance in case of new course */
/* TODO: Delete new attendance in case of delete */
public class CourseManager extends ModelManager<Course> {

    /* Lazy loading to prevent dead-lock on ModelManagers */
    private AttendanceManager mAttendanceManager;

    public CourseManager() {
        super(CourseTable.NAME, CourseTable.Columns.ID);
    }

    private AttendanceManager getAttendanceManager() {
        if (mAttendanceManager == null) {
            mAttendanceManager = ModelManagers.get(AttendanceManager.class);
        }
        return mAttendanceManager;
    }

    @Override
    public void insert(Course course) {
        super.insert(course);
        Attendance attendance = new Attendance();
        attendance.setCourse(course);
        getAttendanceManager().insert(attendance);
    }

    @Override
    public void update(Course model) {
        super.update(model);
        getAttendanceManager().notifyProviderObservers();
    }

    /* Increase visibility for access from AttendanceManager */
    @Override
    public void loadModel(Course model) {
        super.loadModel(model);
    }
}
