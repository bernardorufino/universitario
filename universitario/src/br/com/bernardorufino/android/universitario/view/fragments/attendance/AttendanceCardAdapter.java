package br.com.bernardorufino.android.universitario.view.fragments.attendance;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import br.com.bernardorufino.android.universitario.helpers.Helper;
import br.com.bernardorufino.android.universitario.model.ModelManagers;
import br.com.bernardorufino.android.universitario.model.attendance.Attendance;
import br.com.bernardorufino.android.universitario.model.attendance.AttendanceManager;
import br.com.bernardorufino.android.universitario.view.components.AttendanceCard;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/*
* FIXME: Description below
*
* Consider the following scenario:
*   The user starts a sequence of +1 clicks on some card empty card, that is, a card with
*   currently 0 absences, the first will instantly trigger as expected (see TrialScheduler
*   and AttendanceCard), yielding 1 absence to this card. The next ones will constantly
*   re-schedule the call to the appropriate listener (AttendanceCard L.72) until the user
*   gives a pause between his clicks of something more than the INTERVAL_UPDATE_TRIGGER
*   (AttendanceCard L.25), but less than 2 times it (think of one click happening t seconds
*   before the next update, it will re-schedule this update for more u seconds, so the time
*   the user must wait for this update to be triggered is u + t, for which holds: u < u + t < 2*u
*   since 0 < t < u). Let's say he decided to stop clicking when there was 15 absences in the
*   respective card. But unfortunately, our user won't simply wait 'till the update to the
*   database (which is what the real listener do, see AttendanceCardAdapter L.74), the user
*   fires another click to +1 of ANOTHER card, which naturally enough will instantly trigger
*   an update (since it's the first click), thus happening before the first update that has
*   been re-scheduled a couple of times. The fact is that there is no mechanism to handle
*   those collisions, so, the result is that this intrusive update will re-render the former
*   card and reset its absences to 1 (which was the last modification to the database coming
*   from the first card), what happens behind the curtains is that there is no locking to
*   the re-scheduling mechanism, it just re-schedules the task, however this task uses an
*   instance variable, namely mAttendance (see parameter on method call aat AttendanceCall
*   L.72). So, before our update on hold gets called, the re-render (which is a result from
*   the loader at AttendanceFragment L.166 and AttendanceCardAdapter L.69) will end up at
*   AttendanceCardAdapter L.125 -> AttendanceCard L.92, resetting the internal variable
*   mAttendance. Thus, when the time arrives for the update, it will be triggered, but the
*   object passed will no longer be the one expected before (which probably this time has
*   already been garbage collected, or not =P), that is, on AttendanceCard L.72 mAttendance
*   is the new fresh object received from the database, so the update won't have the desired
*   effect of setting the absences to 15. Unfortunately, this is not the only problem, after
*   the intrusive call (a.k.a. the hated click of the user), the user will be negatively
*   surprised with a UI refresh that will reset the absences of the first card to 1 abruptly
*   without his requesting it nor expecting it, which is really bad.
*
* A few solutions to the problem were thought and I'll sketch some of them here. The first
* one was to create a middle man between the DB updates (which are in fact quite independent,
* and, indeed, you can feel the need for a ThreadPool or something similar) to enqueue them,
* and some sort of mechanism for the cards to tell this middle man to request resources and
* release them (the implementation could be a simple counter), and this middle man would only
* commit the updates to the DB once all resources have been released (which would imply the
* user is not clicking anymore in any +1 or -1, or whatever that triggers DB updates). One
* problem with this approach is that it no longer follows the appreciated pattern on in-line
* editions of android, that as soon as you leave the focus of some command, its modification
* is stored. Another possible problem is that, although in the present the only way to trigger
* updates are through this modifications and edition/creating of new courses, in the future
* db updates could come from different sources and the problem of some update modifying a card
* which the user is currently interacting with would continue and result in unpleasant
* experiences to the user. So the slight favored solution is the second. It also consists of
* having a middle man, but not in the model side, rather in the view side. It would be an
* object between the AttendanceFragment and the AttendanceCardAdapter, possibly it could be a
* object playing similar role of the AttendanceCardAdapter but composing one of BaseAdapter,
* instead of inheriting from it. It would also give resources to cards and let them release
* those, but it wouldn't manage db operations, it would manage view renders. In other words,
* it would only update the whole list of cards once all current modifications have finished,
* so it would have to enqueue view updates (perhaps in the form of the data received, the
* lists of Attendances at AttendanceFragment L.165) and dequeue* and render them when all
* resources have been released (i.e. the uses is no longer clicking in critical buttons).
* Finally, it's worthy saying that AttendanceCard would have to have a reference to this
* manager in order to be able to release his requested resources when the callback gets called.
*
* * Maybe only consider the last one.
*
* */
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
            Helper.log("Not redrawing, it's equal");
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
        Helper.log("Adapter.getView() called for " + item.getCourse().getTitle());
        card.setOnAttendanceUpdateListener(mOnAttendanceUpdateListener);

        if (card.getAttendance() != item) {
            if (card.getAttendance() == null) {
                Helper.log("-- setting empty card");
            } else {
                Helper.log("-- using card of " + card.getAttendance().getCourse().getTitle());
                if (card.getAttendance().equals(item)) {
                    Helper.log("-- they are .equals() but not ==");
                } else {
                    Helper.log("-- they are NOT .equals() nor ==");
                }
            }
        } else {
            Helper.log("Not resetting card because it's ==");
        }
        if (card.getAttendance() != item) card.setAttendance(item);
        return card;
    }
}
