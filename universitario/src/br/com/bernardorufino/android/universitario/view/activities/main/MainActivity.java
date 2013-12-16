package br.com.bernardorufino.android.universitario.view.activities.main;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import br.com.bernardorufino.android.universitario.R;
import br.com.bernardorufino.android.universitario.libs.tabbed.SynchronizedTabListenerAdapter;
import br.com.bernardorufino.android.universitario.libs.tabbed.SynchronizedViewChangeListenerAdapter;
import br.com.bernardorufino.android.universitario.view.activities.common.NavMenuFactory;
import br.com.bernardorufino.android.universitario.view.fragments.AttendanceFragment;
import br.com.bernardorufino.android.universitario.view.fragments.CalendarFragment;
import br.com.bernardorufino.android.universitario.view.fragments.ReportFragment;
import com.google.common.collect.ImmutableList;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
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
    private SlidingMenu mNavMenu;

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
        ViewPager.OnPageChangeListener pageChangeListener = new MainViewChangeListener().setActionBar(actionBar);
        mViewPager.setOnPageChangeListener(pageChangeListener);
    }

    private void initializeSlidingMenu(Bundle savedInstanceState) {
        mNavMenu = NavMenuFactory.getInstance(this);
        mViewPager.setCurrentItem(0); // iff below
        mNavMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeView(savedInstanceState);
        initializeSlidingMenu(savedInstanceState);
        initializeTabs();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mNavMenu.toggle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
        * Listener intended to avoid conflicts between ViewPager and SlidingMenu when a swipe gesture is performed
        * */
    private class MainViewChangeListener extends SynchronizedViewChangeListenerAdapter {
        @Override
        public void onPageSelected(int i) {
            super.onPageSelected(i);
            mNavMenu.setTouchModeAbove(
                    (i == 0)
                    ? SlidingMenu.TOUCHMODE_FULLSCREEN
                    : SlidingMenu.TOUCHMODE_NONE
            );
        }
    }
}
