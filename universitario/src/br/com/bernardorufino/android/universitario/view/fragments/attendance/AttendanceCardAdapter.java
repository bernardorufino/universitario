package br.com.bernardorufino.android.universitario.view.fragments.attendance;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import br.com.bernardorufino.android.universitario.helpers.Helper;
import br.com.bernardorufino.android.universitario.model.attendance.Attendance;
import br.com.bernardorufino.android.universitario.view.components.AttendanceCard;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.*;

public class AttendanceCardAdapter extends BaseAdapter {

    private List<Attendance> mAttendances;
    private Context mContext;

    public AttendanceCardAdapter(Context context) {
        this(context, Collections.<Attendance>emptyList());
    }

    public AttendanceCardAdapter(Context context, List<Attendance> attendances) {
        super();
        mContext = context;
        mAttendances = attendances;
    }

    public void update(List<Attendance> attendances) {
        mAttendances = checkNotNull(attendances);
        Helper.log("AttendanceCardAdapter.update(attendances = " + attendances + ")");
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
        /* TODO: Implement */
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Helper.log("AttendanceCardAdapter.getView(position = " + position + ", convertView = " + convertView + ", parent = " + parent + ")");
        AttendanceCard card = (convertView instanceof AttendanceCard)
                              ? (AttendanceCard) convertView
                              : new AttendanceCard(mContext);
        Attendance item = checkNotNull(getItem(position));
        card.setAttendance(item);
        return card;
    }
}
