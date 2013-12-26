package br.com.bernardorufino.android.universitario.model.attendance;

import android.content.ContentValues;
import android.database.Cursor;
import br.com.bernardorufino.android.universitario.helpers.CursorHelper;
import br.com.bernardorufino.android.universitario.model.base.AbstractModel;
import br.com.bernardorufino.android.universitario.model.course.Course;

import static com.google.common.base.Preconditions.*;

public class Attendance extends AbstractModel implements Cloneable {

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

    public void increaseAbsences(double delta) {
        setAbsences(mAbsences + delta);
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        if (!isNewRecord()) values.put(AttendanceTable.Columns.ID, getId());
        values.put(AttendanceTable.Columns.ABSENCES, mAbsences);
        values.put(AttendanceTable.Columns.COURSE_ID, mCourse.getId());
        return values;
    }

    @Override
    public Attendance clone() {
        try {
            Attendance clone = (Attendance) super.clone();
            clone.mCourse = mCourse.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attendance)) return false;

        Attendance that = (Attendance) o;

        if (Double.compare(that.mAbsences, mAbsences) != 0) return false;
        if (mCourse != null ? !mCourse.equals(that.mCourse) : that.mCourse != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mCourse != null ? mCourse.hashCode() : 0;
        temp = Double.doubleToLongBits(mAbsences);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
