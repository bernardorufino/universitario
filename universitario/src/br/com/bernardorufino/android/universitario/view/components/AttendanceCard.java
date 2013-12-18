package br.com.bernardorufino.android.universitario.view.components;

import android.content.Context;
import android.content.res.Resources;
import android.widget.FrameLayout;
import android.widget.TextView;
import br.com.bernardorufino.android.universitario.R;
import br.com.bernardorufino.android.universitario.helpers.Helper;
import br.com.bernardorufino.android.universitario.model.attendance.Attendance;
import br.com.bernardorufino.android.universitario.model.course.Course;

import static br.com.bernardorufino.android.universitario.definitions.Definitions.Domain;
import static com.google.common.base.Preconditions.*;

public class AttendanceCard extends FrameLayout {

    private TextView mCourseTitle;
    private TextView mCourseProfessor;
    private TextView mAbsencesBadge;
    private Attendance mAttendance;
    private DiscreteProgressBarView mBar;

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
    }

    public void setAttendance(Attendance attendance) {
        mAttendance = checkNotNull(attendance);
        updateView();
    }

    private void updateView() {
        Helper.log("AttendanceCard.updateView()");
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
        return (current < total * Domain.ABSENCE_WARNING_THRESHOLD)
               ? R.color.absence_ok
               : (current <= total)
                 ? R.color.absence_warning
                 : R.color.absence_overflow;
    }

    private static String getAbsenceBadgeText(Attendance attendance) {
        return attendance.getAbsences() + " / " + attendance.getCourse().getAllowedAbsences();
    }

}
