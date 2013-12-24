package br.com.bernardorufino.android.universitario.view.fragments.attendance;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import br.com.bernardorufino.android.universitario.R;
import br.com.bernardorufino.android.universitario.helpers.Helper;
import br.com.bernardorufino.android.universitario.helpers.ViewHelper;
import br.com.bernardorufino.android.universitario.model.ModelManagers;
import br.com.bernardorufino.android.universitario.model.attendance.Attendance;
import br.com.bernardorufino.android.universitario.model.attendance.AttendanceManager;
import br.com.bernardorufino.android.universitario.model.attendance.AttendanceProvider;
import br.com.bernardorufino.android.universitario.model.course.Course;
import br.com.bernardorufino.android.universitario.view.fragments.CourseEditFragment;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.*;

public class AttendanceFragment extends RoboFragment implements LoaderManager.LoaderCallbacks<List<Attendance>> {

    private static final int ATTENDANCES_LOADER = 0;

    @InjectView(R.id.cards_list) private ListView mCardsList;

    private AttendanceCardAdapter mAdapter;
    private AttendanceProvider mAttendanceProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.actions_fragment_attendance, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.attendance_action_new:
                CourseEditFragment.show(new Course(), getFragmentManager());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_attendance, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkNotNull(mCardsList, "Robo not injecting, bad robot");
        /* TODO: Analyse life cycle to prevent leaks from spurious attendance providers created here */
        AttendanceManager attendanceManager = ModelManagers.get(getActivity(), AttendanceManager.class);
        mAttendanceProvider = attendanceManager.getAttendanceProvider();
        mAdapter = new AttendanceCardAdapter(getActivity());
        mCardsList.setAdapter(mAdapter);
        registerForContextMenu(mCardsList);
        getLoaderManager().initLoader(ATTENDANCES_LOADER, null, this);
    }

    private TextView mContextHeader;

    private TextView getContextHeader(String title) {
        if (mContextHeader == null || !ViewHelper.makeOrphan(mContextHeader)) {
            Helper.log("Inflating view for context menu of attendances");
            mContextHeader = (TextView) LayoutInflater.from(getActivity())
                    .inflate(R.layout.component_context_menu_header_attendance, null);
        }
        mContextHeader.setText(title);
        return mContextHeader;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Attendance attendance = mAdapter.getItem(info.position);
        Course course = attendance.getCourse();
        View header = getContextHeader(course.getTitle());
        /* TODO: Get rid of the blue line below the header */
        menu.setHeaderView(header);
        getActivity().getMenuInflater().inflate(R.menu.context_component_attendance_card, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Attendance attendance = mAdapter.getItem(info.position);
        Course course = attendance.getCourse();
        switch (item.getItemId()) {
            case R.id.ctx_menu_comp_att_card_edit:
                CourseEditFragment.show(course, getFragmentManager());
                return true;
            case R.id.ctx_menu_comp_att_card_delete:
                new DeleteAttendanceTask().execute(attendance);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public Loader<List<Attendance>> onCreateLoader(int loader, Bundle bundle) {
        Helper.log("Loader created, mAdapter = " + mAdapter);
        switch (loader) {
            case ATTENDANCES_LOADER:
                return new AttendanceCardLoader(getActivity(), checkNotNull(mAttendanceProvider));
        }
        throw new IllegalStateException("Trying to create an unknown loader.");
    }

    @Override
    public void onLoadFinished(Loader<List<Attendance>> loader, List<Attendance> attendances) {
        Helper.log("Loader finished loading with " + attendances);
        mAdapter.update(attendances);
    }

    @Override
    public void onLoaderReset(Loader<List<Attendance>> loader) {
        Helper.log("Loader reset");
        mAdapter.update(Collections.<Attendance>emptyList());
    }

    private class DeleteAttendanceTask extends AsyncTask<Attendance, Void, Exception> {

        private Attendance mAttendance;

        @Override
        protected Exception doInBackground(Attendance... attendances) {
            checkArgument(attendances.length == 1);
            mAttendance = checkNotNull(attendances[0]);
            AttendanceManager attendanceManager = ModelManagers.get(getActivity(), AttendanceManager.class);
            try {
                attendanceManager.deleteWithDependencies(mAttendance);
                return null;
            } catch (Exception e) {
                return e;
            }
        }

        @Override
        protected void onPostExecute(Exception e) {
            if (e == null) {
                /* TODO: Animate view */
                String course = mAttendance.getCourse().getTitle();
                ViewHelper.flash(getActivity(), course + " removido");
            } else {
                Helper.log("Error deleting attendance: " + e.getMessage());
                ViewHelper.flash(getActivity(), "Ocorreu um erro =(");
            }
        }
    }
}
