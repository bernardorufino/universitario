package br.com.bernardorufino.android.universitario.model.course;

import static com.google.common.base.Preconditions.*;

public class Course {

    private String mTitle;
    private String mProfessor;
    private int mTotalClasses;

    public Course(String title, String professor, int totalClasses) {
        mTitle = title;
        mProfessor = professor;
        mTotalClasses = totalClasses;
    }

    public int getAllowedAbsences() {
        /* TODO: Implement */
        return (int) Math.floor(mTotalClasses * 0.15);
    }

    public int getTotalClasses() {
        return mTotalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        checkArgument(totalClasses > 0);
        mTotalClasses = totalClasses;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getProfessor() {
        return mProfessor;
    }

    public void setProfessor(String professor) {
        mProfessor = professor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (mTotalClasses != course.mTotalClasses) return false;
        if (mProfessor != null ? !mProfessor.equals(course.mProfessor) : course.mProfessor != null) return false;
        if (mTitle != null ? !mTitle.equals(course.mTitle) : course.mTitle != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mTitle != null ? mTitle.hashCode() : 0;
        result = 31 * result + (mProfessor != null ? mProfessor.hashCode() : 0);
        result = 31 * result + mTotalClasses;
        return result;
    }
}
