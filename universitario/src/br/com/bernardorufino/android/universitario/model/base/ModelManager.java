package br.com.bernardorufino.android.universitario.model.base;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

/* DAO abstraction, provides some useful methods for subclasses and alongside the factory ensures that every manager
 * uses only one connection
 * */
public abstract class ModelManager<T extends Model> {

    private SQLiteOpenHelper mHelper;
    private Collection<AbstractModelProvider> mModelProviders = new LinkedList<>();
    private Map<Integer, T> mModelCache = new HashMap<>();
    private String mTableName;
    private String mColumnIdName;

    protected ModelManager(String tableName, String columnIdName) {
        mTableName = tableName;
        mColumnIdName = columnIdName;
    }

    public void setHelper(SQLiteOpenHelper helper) {
        mHelper = helper;
    }

    public SQLiteOpenHelper getHelper() {
        return mHelper;
    }

    protected void notifyProviderObservers() {
        for (AbstractModelProvider provider : mModelProviders) provider.notifyObservers();
        onNotifyProviderObservers();
    }

    protected void onNotifyProviderObservers() { /* Override */ }

    protected void addProvider(AbstractModelProvider modelProvider) {
        mModelProviders.add(modelProvider);
    }

    protected void loadModel(T model) {
        mModelCache.put(model.getId(), model);
    }

    protected T retrieveModel(int id) {
        return mModelCache.get(id);
    }

    public void close() {
        mHelper.close();
    }

    public void insert(T model) {
        checkState(model.isNewRecord(), "Cannot insert an already existent record, try update.");
        SQLiteDatabase db = getWritableDatabase();
        int id = (int) db.insertOrThrow(mTableName, null, model.getContentValues());
        model.setId(id);
        notifyProviderObservers();
    }

    public void update(T model) {
        SQLiteDatabase db = getWritableDatabase();
        String where = mColumnIdName + " = " + model.getId();
        int rows = db.update(mTableName, model.getContentValues(), where, null);
        checkState(rows == 1, "Expected to modify one record, but modified " + rows);
        notifyProviderObservers();
    }

    public void save(T model) {
        if (model.isNewRecord()) insert(model);
        else update(model);
    }

    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(mTableName, mColumnIdName + " = " + id, null);
        checkState(rows == 1, "Expected to modify one record, but modified " + rows);
        notifyProviderObservers();
    }

    public SQLiteDatabase getReadableDatabase() {
        return mHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        return mHelper.getWritableDatabase();
    }
}
