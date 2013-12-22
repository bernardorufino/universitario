package br.com.bernardorufino.android.universitario.model.base;

import android.content.ContentValues;

public interface Model {

    public int getId();

    public void setId(int id);

    public boolean isNewRecord();

    public void setNewRecord(boolean newRecord);

    public ContentValues getContentValues();
}
