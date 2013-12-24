package br.com.bernardorufino.android.universitario.view.fragments;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.drawable.RotateDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import br.com.bernardorufino.android.universitario.R;
import br.com.bernardorufino.android.universitario.helpers.AnimationHelper;
import br.com.bernardorufino.android.universitario.helpers.Helper;
import br.com.bernardorufino.android.universitario.helpers.ViewHelper;
import br.com.bernardorufino.android.universitario.model.ModelManagers;
import br.com.bernardorufino.android.universitario.model.attendance.Attendance;
import br.com.bernardorufino.android.universitario.model.course.Course;
import br.com.bernardorufino.android.universitario.model.course.CourseManager;
import com.google.common.collect.ImmutableList;
import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;

import java.util.Collection;

public class CourseEditFragment extends RoboDialogFragment {

    private static final String NEW_COURSE_TITLE = "Novo Curso";
    private static final String NEW_COURSE_OK_BUTTON = "Criar";
    private static final String EDIT_COURSE_TITLE = "Editar Curso";
    private static final String EDIT_COURSE_OK_BUTTON = "Salvar";
    private static final int DEFAULT_ALLOWED_ABSENCES = 8;
    private static final String DEFAULT_TAG = "course_edit";

    public static CourseEditFragment show(Course course, FragmentManager manager) {
        CourseEditFragment fragment = new CourseEditFragment(course);
        fragment.show(manager, DEFAULT_TAG + course.hashCode());
        return fragment;
    }

    @InjectView(R.id.course_edit_title) private TextView mTitle;
    @InjectView(R.id.course_edit_ok_button) private Button mOkButton;
    @InjectView(R.id.course_edit_cancel_button) private Button mCancelButton;
    @InjectView(R.id.edit_course_title) private EditText mCourseTitle;
    @InjectView(R.id.edit_course_professor) private EditText mCourseProfessor;
    @InjectView(R.id.edit_course_allowed_absences) private NumberPicker mCourseAllowedAbsences;

    private Collection<View> mUiViews; /* To be temporary enabled and disabled */
    private final CourseManager mCourseManager = ModelManagers.get(CourseManager.class);
    private final Course mCourse;
    private ObjectAnimator mLoadingAnimator;
    private RotateDrawable mSpinner;

    public CourseEditFragment(Course course) {
        mCourse = course;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpinner = (RotateDrawable) getResources().getDrawable(R.drawable.spinner_32_rotate);
        mLoadingAnimator = AnimationHelper.loopDrawable(mSpinner).setDuration(1000);
        mLoadingAnimator.setInterpolator(new LinearInterpolator());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUiViews = ImmutableList.of(mCancelButton, mCourseTitle, mCourseProfessor, mCourseAllowedAbsences);
        mCourseAllowedAbsences.setMaxValue(100);
        mCourseAllowedAbsences.setMinValue(1);
        if (mCourse.isNewRecord()) {
            mTitle.setText(NEW_COURSE_TITLE);
            mOkButton.setText(NEW_COURSE_OK_BUTTON);
            mCourseAllowedAbsences.setValue(DEFAULT_ALLOWED_ABSENCES);
        } else {
            mTitle.setText(EDIT_COURSE_TITLE);
            mOkButton.setText(EDIT_COURSE_OK_BUTTON);
            mCourseProfessor.setText(mCourse.getProfessor());
            mCourseTitle.setText(mCourse.getTitle());
            mCourseAllowedAbsences.setValue(mCourse.getAllowedAbsences());
        }
        mOkButton.setOnClickListener(mOnOkClickListener);
        mCancelButton.setOnClickListener(mOnCancelClickListener);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private View.OnClickListener mOnOkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                mCourse.setTitle(mCourseTitle.getText().toString());
                mCourse.setProfessor(mCourseProfessor.getText().toString());
                mCourse.setAllowedAbsences(mCourseAllowedAbsences.getValue());
            } catch (IllegalArgumentException e) {
                Helper.log("Error saving course: " + e.getMessage());
                ViewHelper.flash(getActivity(), "Dados invalidos");
                return;
            }
            new SaveCourseTask().execute();
        }
    };

    private View.OnClickListener mOnCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private class SaveCourseTask extends AsyncTask<Void, Void, Exception> {

        @Override
        protected void onPreExecute() {
            // Starts loading animation
            mOkButton.setCompoundDrawablesWithIntrinsicBounds(mSpinner, null, null, null);
            mLoadingAnimator.start();
            // Disable UI
            for (View view : mUiViews) view.setEnabled(false);
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                mCourseManager.save(mCourse);
                return null;
            } catch (Exception e) {
                return e;
            }
        }

        @Override
        protected void onPostExecute(Exception e) {
            // Clears loading animation
            mLoadingAnimator.end();
            mOkButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_accept, 0, 0, 0);
            // Re-enable views
            for (View view : mUiViews) view.setEnabled(true);
            if (e == null) {
                // Closes dialog
                dismiss();
            } else {
                Helper.log("Error saving course: " + e.getMessage());
                ViewHelper.flash(getActivity(), "Ocorreu um erro =(");
            }
        }
    }
}