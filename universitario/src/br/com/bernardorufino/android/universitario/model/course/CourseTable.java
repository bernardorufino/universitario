package br.com.bernardorufino.android.universitario.model.course;

import br.com.bernardorufino.android.universitario.model.base.Table;

public class CourseTable implements Table {

    public static final String NAME = "courses";

    public static class Columns {

        public static final String ID = "_id";
        public static final String ALLOWED_ABSENCES = "allowed_absences";
        public static final String TITLE = "title";
        public static final String PROFESSOR = "professor";
    }

    public static final String CREATE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
            "    " + Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "    " + Columns.ALLOWED_ABSENCES + " INTEGER NOT NULL,\n" +
            "    " + Columns.TITLE + " TEXT NOT NULL,\n" +
            "    " + Columns.PROFESSOR + " TEXT NOT NULL\n" +
            ");";

    // Prevents instantiation
    private CourseTable() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}

