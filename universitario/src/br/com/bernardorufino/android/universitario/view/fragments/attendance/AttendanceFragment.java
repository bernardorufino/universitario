package br.com.bernardorufino.android.universitario.view.fragments.attendance;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.*;
import android.widget.ListView;
import br.com.bernardorufino.android.universitario.R;
import br.com.bernardorufino.android.universitario.helpers.Helper;
import br.com.bernardorufino.android.universitario.model.ModelManagerFactory;
import br.com.bernardorufino.android.universitario.model.attendance.Attendance;
import br.com.bernardorufino.android.universitario.model.attendance.AttendanceManager;
import br.com.bernardorufino.android.universitario.model.attendance.AttendanceProvider;
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
        inflater.inflate(R.menu.fragment_attendance, menu);
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
        AttendanceManager attendanceManager = ModelManagerFactory.getManager(getActivity(), AttendanceManager.class);
        mAttendanceProvider = attendanceManager.getAttendanceProvider();
        mAdapter = new AttendanceCardAdapter(getActivity());
        mCardsList.setAdapter(mAdapter);
        getLoaderManager().initLoader(ATTENDANCES_LOADER, null, this);
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
}
