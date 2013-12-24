package br.com.bernardorufino.android.universitario.view.components;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import br.com.bernardorufino.android.universitario.R;
import br.com.bernardorufino.android.universitario.ext.concurrency.TrialScheduler;
import br.com.bernardorufino.android.universitario.model.attendance.Attendance;
import br.com.bernardorufino.android.universitario.model.course.Course;
import com.google.common.collect.ImmutableList;

import static br.com.bernardorufino.android.universitario.application.Definitions.Domain;
import static com.google.common.base.Preconditions.*;

/* TODO: Make bar update values instead of redrawing */
public class AttendanceCard extends FrameLayout {

    public static interface OnAttendanceUpdateListener {

        public void onUpdate(Attendance attendance);
    }

    private static final long INTERVAL_UPDATE_TRIGGER = 500; // Milliseconds
    private static final double PLUS_ONE_ABSENCE = 1;
    private static final double PLUS_FRACTION_ABSENCE = 0.5;
    private static final double MINUS_FRACTION_ABSENCE = -0.5;
    private static final double MINUS_ONE_ABSENCE = -1;
    private static final double UNDEFINED_ABSENCES_DELTA = 0;

    private TextView mCourseTitle;
    private TextView mCourseProfessor;
    private TextView mAbsencesBadge;
    private Attendance mAttendance;
    private DiscreteProgressBarView mBar;
    private Button mPlusOne;
    private Button mPlusFraction;
    private Button mMinusFraction;
    private Button mMinusOne;
    private OnAttendanceUpdateListener mOnAttendanceUpdateListener = new OnAttendanceUpdateListener() {
        @Override
        public void onUpdate(Attendance attendance) {
            /* Empty */
        }
    };

    /* If need to use in xml, provide custom attributes and another constructor */
    public AttendanceCard(Context context) {
        super(context);
        inflate(context, R.layout.component_attendance_card, this);
        initializeView();
    }

    private void initializeView() {
        mCourseTitle = (TextView) findViewById(R.id.comp_att_card_course_title);
        mCourseProfessor = (TextView) findViewById(R.id.comp_att_card_course_prof);
        mAbsencesBadge = (TextView) findViewById(R.id.comp_att_card_absences_badge);
        mBar = (DiscreteProgressBarView) findViewById(R.id.comp_att_card_absences_bar);
        mPlusOne = (Button) findViewById(R.id.comp_att_card_plus_one);
        mPlusFraction = (Button) findViewById(R.id.comp_att_card_plus_fraction);
        mMinusFraction = (Button) findViewById(R.id.comp_att_card_minus_fraction);
        mMinusOne = (Button) findViewById(R.id.comp_att_card_minus_one);
        for (Button button : ImmutableList.of(mPlusOne, mPlusFraction, mMinusFraction, mMinusOne)) {
            button.setOnClickListener(mOnAbsencesChangeListener);
        }
    }

    public void setOnAttendanceUpdateListener(OnAttendanceUpdateListener onAttendanceUpdateListener) {
        mOnAttendanceUpdateListener = onAttendanceUpdateListener;
    }

    private TrialScheduler mUpdater = new TrialScheduler(INTERVAL_UPDATE_TRIGGER) {
        @Override
        protected void doExecute() {
            mOnAttendanceUpdateListener.onUpdate(mAttendance);
        }
    };

    private OnClickListener mOnAbsencesChangeListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            double delta = (view == mPlusOne) ? PLUS_ONE_ABSENCE
                         : (view == mPlusFraction) ? PLUS_FRACTION_ABSENCE
                         : (view == mMinusFraction) ? MINUS_FRACTION_ABSENCE
                         : (view == mMinusOne) ? MINUS_ONE_ABSENCE
                         : UNDEFINED_ABSENCES_DELTA;
            checkState(delta != UNDEFINED_ABSENCES_DELTA);
            if (mAttendance.getAbsences() + delta < 0) return;
            mAttendance.increaseAbsences(delta);
            updateView();
            mUpdater.tryExecute();
        }
    };

    public void setAttendance(Attendance attendance) {
        mAttendance = checkNotNull(attendance);
        updateView();
    }

    public Attendance getAttendance() {
        return mAttendance;
    }

    private void updateView() {
        Course course = mAttendance.getCourse();
        mCourseTitle.setText(course.getTitle());
        mCourseProfessor.setText(course.getProfessor());
        mAbsencesBadge.setText(getAbsenceBadgeText(mAttendance));
        updateBar();
    }

    private void updateBar() {
        double current = mAttendance.getAbsences();
        int total = mAttendance.getCourse().getAllowedAbsences();
        Resources res = getContext().getResources();
        int offColor = res.getColor(R.color.absence_remaining);
        int onColor = res.getColor(getOnColorId(current, total));
        mBar.setColors(onColor, offColor).setProgress(Math.min((float) current, total), total).draw();
    }

    private static int getOnColorId(double current, int total) {
        return (current < total * Domain.ABSENCE_WARNING_THRESHOLD) ? R.color.absence_ok
             : (current < total) ? R.color.absence_warning
             : R.color.absence_overflow;
    }

    private static String getAbsenceBadgeText(Attendance attendance) {
        return attendance.getAbsences() + " / " + attendance.getCourse().getAllowedAbsences();
    }

}
