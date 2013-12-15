package br.com.bernardorufino.android.universitario.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import br.com.bernardorufino.android.universitario.R;
import br.com.bernardorufino.android.universitario.activities.adapters.MainPagerAdapter;
import br.com.bernardorufino.android.universitario.fragments.AttendanceFragment;
import br.com.bernardorufino.android.universitario.fragments.CalendarFragment;
import br.com.bernardorufino.android.universitario.fragments.ReportFragment;
import br.com.bernardorufino.android.universitario.libs.tabbed.SynchronizedTabListenerAdapter;
import br.com.bernardorufino.android.universitario.libs.tabbed.SynchronizedViewChangeListenerAdapter;
import com.google.common.collect.ImmutableList;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

import java.util.List;

public class MainActivity extends RoboFragmentActivity {

    // Both lists must have corresponding elements in same positions
    public static List<String> TAB_LABELS = ImmutableList.of("Faltas", "Trabalhos", "Notas");
    public static List<? extends Class<? extends Fragment>> TAB_FRAGMENTS = ImmutableList.of(
            AttendanceFragment.class,
            CalendarFragment.class,
            ReportFragment.class
    );

    @InjectView(R.id.pager) private ViewPager mViewPager;

    private MainPagerAdapter mPagerAdapter;

    private void initializeView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    private void initializeTabs() {
        // Sets the adapter, who will provide the fragments, to the ViewPager (in layout)
        mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        // Sets tabs, its labels, and bind the listener to make the views change accordingly
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new SynchronizedTabListenerAdapter().setViewPager(mViewPager);
        for (String label : TAB_LABELS) {
            actionBar.addTab(actionBar.newTab().setText(label).setTabListener(tabListener));
        }
        // Sets view listener to activate corresponding tab when swipping
        ViewPager.OnPageChangeListener pageChangeListener =
                new SynchronizedViewChangeListenerAdapter().setActionBar(actionBar);
        mViewPager.setOnPageChangeListener(pageChangeListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeView(savedInstanceState);
        initializeTabs();
    }
}
