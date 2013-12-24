package br.com.bernardorufino.android.universitario.view.fragments.attendance;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import br.com.bernardorufino.android.universitario.model.attendance.Attendance;
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
        mAttendances = checkNotNull(attendances);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AttendanceCard card = (convertView instanceof AttendanceCard)
                              ? (AttendanceCard) convertView
                              : new AttendanceCard(mContext);
        Attendance item = checkNotNull(getItem(position));
        card.setAttendance(item);
        return card;
    }
}
