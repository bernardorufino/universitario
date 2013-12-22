package br.com.bernardorufino.android.universitario.model.attendance;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.bernardorufino.android.universitario.helpers.TableHelper;
import br.com.bernardorufino.android.universitario.model.ModelManagerFactory;
import br.com.bernardorufino.android.universitario.model.base.AbstractModelProvider;
import br.com.bernardorufino.android.universitario.model.base.ModelManager;
import br.com.bernardorufino.android.universitario.model.course.Course;
import br.com.bernardorufino.android.universitario.model.course.CourseManager;
import br.com.bernardorufino.android.universitario.model.course.CourseTable;

import java.util.ArrayList;
import java.util.List;

public class AttendanceManager extends ModelManager<Attendance> {

    private CourseManager mCourseManager = ModelManagerFactory.getManager(CourseManager.class);

    protected AttendanceManager() {
        super(AttendanceTable.NAME, AttendanceTable.Columns.ID);
    }

    public AttendanceProvider getAttendanceProvider() {
        /* TODO: Make provider get models from the cache, or not */
        AbstractAttendanceProvider provider = new AbstractAttendanceProvider() {
            @Override
            public List<Attendance> getAttendances() {
                return getAttendances();
            }
        };
        addProvider(provider);
        return provider;
    }

    private static final String SELECT_ATTENDANCES_COURSES_QUERY =
            "SELECT " +
            TableHelper.columnInQuery(AttendanceTable.NAME, AttendanceTable.Columns.ID) + ", " +
            TableHelper.columnInQuery(AttendanceTable.NAME, AttendanceTable.Columns.ABSENCES) + ", " +
            TableHelper.columnInQuery(CourseTable.NAME, CourseTable.Columns.ID) + ", " +
            TableHelper.columnInQuery(CourseTable.NAME, CourseTable.Columns.ALLOWED_ABSENCES) + " " +
            "FROM " + AttendanceTable.NAME +
            "INNER JOIN " + CourseTable.NAME + " " +
            "ON " + CourseTable.Columns.ID + " = " + AttendanceTable.Columns.COURSE_ID +
            "ORDER BY " + CourseTable.NAME + " ASC ";

    private List<Attendance> getAttendances() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ATTENDANCES_COURSES_QUERY, new String[] {});
        List<Attendance> attendances = new ArrayList<>();
        while (cursor.moveToNext()) {
            Course course = new Course(cursor);
            mCourseManager.loadModel(course);
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
