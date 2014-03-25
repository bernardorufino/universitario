package br.com.bernardorufino.android.universitario.view.fragments.attendance;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import br.com.bernardorufino.android.universitario.helpers.CustomHelper;
import br.com.bernardorufino.android.universitario.model.ModelManagers;
import br.com.bernardorufino.android.universitario.model.attendance.Attendance;
import br.com.bernardorufino.android.universitario.model.attendance.AttendanceManager;
import br.com.bernardorufino.android.universitario.view.components.AttendanceCard;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.*;

public class AttendanceCardAdapter extends BaseAdapter {

    private List<Attendance> mAttendances = Collections.emptyList();
    private Context mContext;

    public AttendanceCardAdapter(Context context) {
        super();
        mContext = context;
    }

    public void update(List<Attendance> attendances) {
        // Don't update view if nothing to update, this happens when the UI elements
        // that triggered the update have itself updated the appropriated models
        if (mAttendances.equals(attendances)) {
            CustomHelper.log("Not redrawing, it's equal");
            return;
        }
        mAttendances = attendances;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAttendances.size();
    }

    @Override
    public Attendance getItem(int position) {
        return mAttendances.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private AttendanceCard.OnAttendanceUpdateListener mOnAttendanceUpdateListener =
            new AttendanceCard.OnAttendanceUpdateListener() {
        @Override
        public void onUpdate(final Attendance attendance) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    ModelManagers.get(AttendanceManager.class).update(attendance);
                    return null;
                }
            }.execute();
        }
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AttendanceCard card = (convertView instanceof AttendanceCard)
                              ? (AttendanceCard) convertView
                              : new AttendanceCard(mContext);
        Attendance item = checkNotNull(getItem(position));
        CustomHelper.log("Adapter.getView() called for " + item.getCourse().getTitle());
        card.setOnAttendanceUpdateListener(mOnAttendanceUpdateListener);
        if (card.getAttendance() != item) card.setAttendance(item);
        return card;
    }
}
