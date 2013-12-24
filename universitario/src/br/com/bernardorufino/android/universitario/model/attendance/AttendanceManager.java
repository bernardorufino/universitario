package br.com.bernardorufino.android.universitario.model.attendance;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.bernardorufino.android.universitario.helpers.TableHelper;
import br.com.bernardorufino.android.universitario.model.ModelManagers;
import br.com.bernardorufino.android.universitario.model.base.AbstractModelProvider;
import br.com.bernardorufino.android.universitario.model.base.ModelManager;
import br.com.bernardorufino.android.universitario.model.course.Course;
import br.com.bernardorufino.android.universitario.model.course.CourseManager;
import br.com.bernardorufino.android.universitario.model.course.CourseTable;

import java.util.ArrayList;
import java.util.List;

public class AttendanceManager extends ModelManager<Attendance> {

    /* Lazy loading to prevent dead-lock on ModelManagers */
    private CourseManager mCourseManager;

    public AttendanceManager() {
        super(AttendanceTable.NAME, AttendanceTable.Columns.ID);
    }

    private CourseManager getCourseManager() {
        if (mCourseManager == null) {
            mCourseManager = ModelManagers.get(CourseManager.class);
        }
        return mCourseManager;
    }

    /* Increase visibility for access from CourseManager */
    @Override
    public void notifyProviderObservers() {
        super.notifyProviderObservers();
    }

    public AttendanceProvider getAttendanceProvider() {
        /* TODO: Make provider get models from the cache, or not */
        AbstractAttendanceProvider provider = new AbstractAttendanceProvider() {
            @Override
            public List<Attendance> getAttendances() {
                return AttendanceManager.this.getAttendances();
            }
        };
        addProvider(provider);
        return provider;
    }

    private static final String SELECT_ATTENDANCES_COURSES_QUERY =
            "SELECT " +
            TableHelper.columnsInQuery(AttendanceTable.NAME, AttendanceTable.Columns.ALL) + ", " +
            TableHelper.columnsInQuery(CourseTable.NAME, CourseTable.Columns.ALL) + " " +
            "FROM " + AttendanceTable.NAME + " " +
            "INNER JOIN " + CourseTable.NAME + " " +
            "ON " + TableHelper.columnAlias(CourseTable.NAME, CourseTable.Columns.ID)  + " = " +
                    TableHelper.columnAlias(AttendanceTable.NAME, AttendanceTable.Columns.COURSE_ID) + " " +
            "ORDER BY " + TableHelper.columnAlias(CourseTable.NAME, CourseTable.Columns.TITLE) + " ASC";

    private List<Attendance> getAttendances() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ATTENDANCES_COURSES_QUERY, new String[] {});
        List<Attendance> attendances = new ArrayList<>();
        while (cursor.moveToNext()) {
            Course course = new Course(cursor);
            getCourseManager().loadModel(course);
            Attendance attendance = new Attendance(cursor, course);
            loadModel(attendance);
            attendances.add(attendance);
        }
        return attendances;
    }

    private static abstract class AbstractAttendanceProvider
            extends AbstractModelProvider
            implements AttendanceProvider {
            /* Empty */
    }
}
