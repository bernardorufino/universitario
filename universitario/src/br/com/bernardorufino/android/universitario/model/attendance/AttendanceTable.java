package br.com.bernardorufino.android.universitario.model.attendance;

import br.com.bernardorufino.android.universitario.model.base.Table;

public class AttendanceTable implements Table {

    public static final String NAME = "attendances";

    public static class Columns {

        public static final String ID = "_id";
        public static final String COURSE_ID = "course_id";
        public static final String ABSENCES = "absences";

        public static final String[] ALL = { ID, COURSE_ID, ABSENCES };
    }

    public static final String CREATE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
            "    " + Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "    " + Columns.COURSE_ID + " INTEGER NOT NULL,\n" +
            "    " + Columns.ABSENCES + " REAL NOT NULL\n" +
            ");";



    // Prevents instantiation
    private AttendanceTable() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
