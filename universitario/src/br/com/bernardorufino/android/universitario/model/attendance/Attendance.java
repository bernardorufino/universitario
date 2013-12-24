package br.com.bernardorufino.android.universitario.model.attendance;

import android.content.ContentValues;
import android.database.Cursor;
import br.com.bernardorufino.android.universitario.helpers.CursorHelper;
import br.com.bernardorufino.android.universitario.model.base.AbstractModel;
import br.com.bernardorufino.android.universitario.model.course.Course;

import static com.google.common.base.Preconditions.*;

public class Attendance extends AbstractModel {

    private Course mCourse;
    private double mAbsences = 0;

    public Attendance() {
        super();
    }

    public Attendance(Cursor cursor, Course course) {
        CursorHelper c = new CursorHelper(cursor);
        setId(c.getInt(AttendanceTable.NAME, AttendanceTable.Columns.ID));
        mAbsences = c.getDouble(AttendanceTable.NAME, AttendanceTable.Columns.ABSENCES);
        setCourse(course);
        setNewRecord(false);
    }

    public Course getCourse() {
        return mCourse;
    }

    public void setCourse(Course course) {
        mCourse = checkNotNull(course);
    }

    public double getAbsences() {
        return mAbsences;
    }

    public void setAbsences(double absences) {
        checkArgument(absences >= 0, "absences must be greater than or equal to zero.");
        mAbsences = absences;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        if (!isNewRecord()) values.put(AttendanceTable.Columns.ID, getId());
        values.put(AttendanceTable.Columns.ABSENCES, mAbsences);
        values.put(AttendanceTable.Columns.COURSE_ID, mCourse.getId());
        return values;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Course)) return false;
        Attendance that = (Attendance) object;
        /* TODO: Allow comparison of new records */
        return !isNewRecord() && !that.isNewRecord() && getId() == that.getId();
    }

    @Override
    public int hashCode() {
        /* TODO: Produce proper hashCode for new records */
        int result = (isNewRecord() ? 1 : 0);
        result = 31 * result + getId();
        return result;
    }
}
