package br.com.bernardorufino.android.universitario.model.base;

import static com.google.common.base.Preconditions.checkState;

public abstract class AbstractModel implements Model {

    private boolean mNewRecord;
    private int mId;

    public AbstractModel() {
        mNewRecord = true;
    }

    public boolean isNewRecord() {
        return mNewRecord;
    }

    public void setNewRecord(boolean newRecord) {
        mNewRecord = newRecord;
    }

    @Override
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        checkState(mNewRecord, "Cannot set the id of an already persisted model.");
        mId = id;
    }
}
