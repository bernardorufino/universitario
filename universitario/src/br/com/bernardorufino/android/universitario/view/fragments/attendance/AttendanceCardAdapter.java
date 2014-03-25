package br.com.bernardorufino.android.universitario.view.fragments.attendance;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import br.com.bernardorufino.android.universitario.application.Definitions;
import br.com.bernardorufino.android.universitario.ext.loader.ComposedDynamicView;
import br.com.bernardorufino.android.universitario.helpers.CustomHelper;
import br.com.bernardorufino.android.universitario.model.ModelManagers;
import br.com.bernardorufino.android.universitario.model.attendance.Attendance;
import br.com.bernardorufino.android.universitario.model.attendance.AttendanceManager;
import br.com.bernardorufino.android.universitario.model.util.ModelUtils;
import br.com.bernardorufino.android.universitario.view.components.AttendanceCard;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;

import java.util.Collections;
import java.util.List;

import static br.com.bernardorufino.android.universitario.model.util.ModelUtils.ModelsDelta;
import static com.google.common.base.Preconditions.*;

/* TODO: Make every animated change here in the adapter, check for the id */
public class AttendanceCardAdapter extends BaseAdapter {

    private List<Attendance> mAttendances = Collections.emptyList();

    private final AttendanceManager mAttendanceManager;
    private final Context mContext;
    private final ListView mCardsList;
    private final ComposedDynamicView<AttendanceSubView> mAttendanceComposedView;
    private ModelsDelta<Attendance> mDelta;

    public AttendanceCardAdapter(
            Context context,
            ListView cardsList,
            ComposedDynamicView<AttendanceSubView> attendanceComposedView) {
        super();
        mContext = context;
        mAttendanceManager = ModelManagers.get(AttendanceManager.class);
        mCardsList = cardsList;
        mAttendanceComposedView = attendanceComposedView;
    }

    public void update(List<Attendance> attendances) {
        // Don't update view if nothing to update, this happens when the UI elements
        // that triggered the update have itself updated the appropriated models
        if (mAttendances.equals(attendances)) {
            CustomHelper.log("Not redrawing, it's equal");
            return;
        }
        CustomHelper.log("Updating adapter...");

        // The ModelDelta below is used as mutable!
        if (mAttendances.size() == 0) mAttendances = attendances;
        mDelta = ModelUtils.delta(mAttendances, attendances);
        mAttendances = attendances;
        if (mDelta.hasChanges()) focusOnAnimation();
        if (mDelta.deleted.size() > 0) {
            String errorMessage = "Deletions are only supported alone";
            checkState(mDelta.created.size() == 0, errorMessage);
            checkState(mDelta.updated.size() == 0, errorMessage);
            checkState(mDelta.unchanged.size() + mDelta.deleted.size() == mDelta.oldModels.size(), errorMessage);
            checkState(mDelta.unchanged.size() == mDelta.freshModels.size(), errorMessage);
            animateDeletedCards(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    notifyDataSetChanged();
                }
            });
        } else {
            notifyDataSetChanged();
        }
    }

    private void focusOnAnimation() {
        Attendance any = null;
        if (mDelta.created.size() > 0) any = mDelta.created.iterator().next();
        if (mDelta.updated.size() > 0) any = mDelta.updated.iterator().next();
        if (any == null) {
            return;
        }
        int anyPosition = mAttendances.indexOf(any);
        mCardsList.smoothScrollToPosition(anyPosition);
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
                    mAttendanceManager.update(attendance);
                    // The card view already updates itself, so, cancelling the view update
                    mAttendanceComposedView.cancelSubViewUpdate(AttendanceSubView.CARDS);
                    return null;
                }
            }.execute();
        }
    };

    @Override
    public AttendanceCard getView(int position, View convertView, ViewGroup parent) {
        AttendanceCard card = (convertView instanceof AttendanceCard)
                              ? (AttendanceCard) convertView
                              : new AttendanceCard(mContext);
        Attendance item = checkNotNull(getItem(position));
        CustomHelper.log("Adapter.getView() called for " + item.getCourse().getTitle());
        card.setOnAttendanceUpdateListener(mOnAttendanceUpdateListener);
        if (card.getAttendance() != item) card.setAttendance(item);
        if (mDelta.created.contains(item)) {
            animateCreatedCard(card, position, parent);
        } else if (mDelta.updated.contains(item)) {
            animateUpdatedCard(card, position, parent);
        }
        return card;
    }

    private void animateCreatedCard(final AttendanceCard card, int position, ViewGroup parent) {
        final Attendance item = getItem(position);
        final float amplitude = parent.getWidth();
        final float displacement = 2;
        Spring spring = SpringSystem.create().createSpring();
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float x = -value * amplitude;
                float alpha = 1f - (value / displacement);
                if (alpha > 1) alpha = 1;
                if (alpha < 0) alpha = 0;
                ViewHelper.setTranslationX(card, x);
                ViewHelper.setAlpha(card, alpha);
            }
            @Override
            public void onSpringAtRest(Spring spring) {
                mDelta.created.remove(item);
            }
        });
        spring.setSpringConfig(Definitions.SPRING_CONFIG);
        spring.setCurrentValue(displacement);
        spring.setEndValue(0);
    }

    private void animateUpdatedCard(final AttendanceCard card, int position, ViewGroup parent) {
        final Attendance item = getItem(position);
        final float displacement = 2;
        Spring spring = SpringSystem.create().createSpring();
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value / displacement);
                ViewHelper.setScaleX(card, scale);
                ViewHelper.setScaleY(card, scale);
            }
            @Override
            public void onSpringAtRest(Spring spring) {
                mDelta.updated.remove(item);
            }
        });
        spring.setSpringConfig(Definitions.SPRING_CONFIG);
        spring.setCurrentValue(displacement);
        spring.setEndValue(0);
    }

    private void animateDeletedCards(AnimatorListenerAdapter listener) {
        /* TODO: Implement */
        listener.onAnimationEnd(null);
    }
}
