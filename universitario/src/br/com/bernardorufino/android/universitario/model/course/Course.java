package br.com.bernardorufino.android.universitario.model.course;

import android.content.ContentValues;
import android.database.Cursor;
import br.com.bernardorufino.android.universitario.helpers.CursorHelper;
import br.com.bernardorufino.android.universitario.model.base.AbstractModel;
import com.google.common.base.Strings;

import static com.google.common.base.Preconditions.*;

public class Course extends AbstractModel {

    private String mTitle;
    private String mProfessor;
    private int mAllowedAbsences;

    public Course() {
        super();
    }

    public Course(Cursor cursor) {
        CursorHelper c = new CursorHelper(cursor);
        setId(c.getInt(CourseTable.NAME, CourseTable.Columns.ID));
        mTitle = c.getString(CourseTable.NAME, CourseTable.Columns.TITLE);
        mProfessor = c.getString(CourseTable.NAME, CourseTable.Columns.PROFESSOR);
        mAllowedAbsences = c.getInt(CourseTable.NAME, CourseTable.Columns.ALLOWED_ABSENCES);
        setNewRecord(false);
    }

    public int getAllowedAbsences() {
        return mAllowedAbsences;
    }

    public void setAllowedAbsences(int allowedAbsences) {
        checkArgument(allowedAbsences > 0, "allowed absences must be greater than zero.");
        mAllowedAbsences = allowedAbsences;
    }

    public void setTotalClasses(int totalClasses) {
        checkArgument(totalClasses > 0, "total classes must be greater than zero.");
        setAllowedAbsences((int) Math.floor(totalClasses * 0.15));
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        title = Strings.nullToEmpty(title).trim();
        checkArgument(!title.isEmpty(), "title can't be blank.");
        mTitle = title;
    }

    public String getProfessor() {
        return mProfessor;
    }

    public void setProfessor(String professor) {
        professor = Strings.nullToEmpty(professor).trim();
        checkArgument(!professor.isEmpty(), "professor can't be blank.");
        mProfessor = professor;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        if (!isNewRecord()) values.put(CourseTable.Columns.ID, getId());
        values.put(CourseTable.Columns.TITLE, mTitle);
        values.put(CourseTable.Columns.PROFESSOR, mProfessor);
        values.put(CourseTable.Columns.ALLOWED_ABSENCES, mAllowedAbsences);
        return values;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Course)) return false;
        Course that = (Course) object;
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
