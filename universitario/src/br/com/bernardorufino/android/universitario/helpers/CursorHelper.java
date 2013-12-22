package br.com.bernardorufino.android.universitario.helpers;

import android.database.Cursor;

public class CursorHelper {

    private final Cursor mCursor;

    public CursorHelper(Cursor cursor) {
        mCursor = cursor;
    }

    public int getColumnIndexOrThrow(String table, String column) {
        return mCursor.getColumnIndexOrThrow(TableHelper.columnAlias(table, column));
    }
    
    public short getShort(String table, String column) {
        return mCursor.getShort(getColumnIndexOrThrow(table, column));
    }

    public int getInt(String table, String column) {
        return mCursor.getInt(getColumnIndexOrThrow(table, column));
    }

    public long getLong(String table, String column) {
        return mCursor.getLong(getColumnIndexOrThrow(table, column));
    }

    public float getFloat(String table, String column) {
        return mCursor.getFloat(getColumnIndexOrThrow(table, column));
    }

    public double getDouble(String table, String column) {
        return mCursor.getDouble(getColumnIndexOrThrow(table, column));
    }

    public int getType(String table, String column) {
        return mCursor.getType(getColumnIndexOrThrow(table, column));
    }

    public boolean isNull(String table, String column) {
        return mCursor.isNull(getColumnIndexOrThrow(table, column));
    }

    public String getString(String table, String column) {
        return mCursor.getString(getColumnIndexOrThrow(table, column));
    }

    public byte[] getBlob(String table, String column) {
        return mCursor.getBlob(getColumnIndexOrThrow(table, column));
    }
}
