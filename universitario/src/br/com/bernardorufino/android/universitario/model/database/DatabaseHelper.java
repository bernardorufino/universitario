package br.com.bernardorufino.android.universitario.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.bernardorufino.android.universitario.helpers.CustomHelper;
import br.com.bernardorufino.android.universitario.model.attendance.AttendanceTable;
import br.com.bernardorufino.android.universitario.model.course.CourseTable;

import static br.com.bernardorufino.android.universitario.application.Definitions.APP_ID;
import static com.google.common.base.Preconditions.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = APP_ID + ".main.db";

    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        /* TODO: Debug purposes, delete this after */
//        db.execSQL(dropQuery(AttendanceTable.NAME));
//        db.execSQL(dropQuery(CourseTable.NAME));
//        db.execSQL(AttendanceTable.CREATE_QUERY);
//        db.execSQL(CourseTable.CREATE_QUERY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AttendanceTable.CREATE_QUERY);
        db.execSQL(CourseTable.CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        checkArgument(oldVersion <= newVersion);
        if (oldVersion == newVersion) return;
        switch (oldVersion) {
            /* only add code here, never delete */
            case 1:
                db.execSQL(dropQuery(AttendanceTable.NAME));
                db.execSQL(dropQuery(CourseTable.NAME));
                break;
        }
        onCreate(db);
    }

    private static String dropQuery(String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        CustomHelper.log("DATABASE WRITE");
        return super.getWritableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        CustomHelper.log("DATABASE READ");
        return super.getReadableDatabase();
    }
}
