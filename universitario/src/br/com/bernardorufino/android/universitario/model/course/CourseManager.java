package br.com.bernardorufino.android.universitario.model.course;

import br.com.bernardorufino.android.universitario.model.base.ModelManager;

public class CourseManager extends ModelManager<Course> {

    protected CourseManager() {
        super(CourseTable.NAME, CourseTable.Columns.ID);
    }

    /* Increase visibility for AttendanceManager */
    @Override
    public void loadModel(Course model) {
        super.loadModel(model);
    }
}
