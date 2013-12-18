package br.com.bernardorufino.android.universitario.model.attendance;

import br.com.bernardorufino.android.universitario.model.course.Course;

import static com.google.common.base.Preconditions.*;

/* TODO: Implement hashCode() and equals() and other stuff */
public class Attendance {

    private Course mCourse;
    private int mAbsences;

    public Attendance(Course course, int absences) {
        mCourse = checkNotNull(course);
        mAbsences = absences;
    }

    public Course getCourse() {
        return mCourse;
    }

    public int getAbsences() {
        return mAbsences;
    }

    public void setAbsences(int absences) {
        checkArgument(absences >= 0);
        mAbsences = absences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attendance that = (Attendance) o;

        if (mAbsences != that.mAbsences) return false;
        if (mCourse != null ? !mCourse.equals(that.mCourse) : that.mCourse != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mCourse != null ? mCourse.hashCode() : 0;
        result = 31 * result + mAbsences;
        return result;
    }
}
