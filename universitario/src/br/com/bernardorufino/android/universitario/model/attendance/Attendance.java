package br.com.bernardorufino.android.universitario.model.attendance;

import br.com.bernardorufino.android.universitario.model.course.Course;

import static com.google.common.base.Preconditions.*;

/* TODO: Implement hashCode() and equals() and other stuff */
public class Attendance {

    private Course mCourse;
    private double mAbsences;

    public Attendance(Course course, double absences) {
        checkArgument(absences >= 0, "absences must be greater than or equal to zero");
        mCourse = checkNotNull(course);
        mAbsences = absences;
    }

    public Course getCourse() {
        return mCourse;
    }

    public double getAbsences() {
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
