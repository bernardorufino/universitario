package br.com.bernardorufino.android.universitario.libs.tabbed;


import android.app.ActionBar;
import android.support.v4.view.ViewPager;

public class SynchronizedViewChangeListenerAdapter implements ViewPager.OnPageChangeListener {

    private ActionBar mActionBar;

    // In order not to have to rewrite constructor when subclassing, it's preferable to chain a call to setActionBar()
    public SynchronizedViewChangeListenerAdapter setActionBar(ActionBar actionBar) {
        mActionBar = actionBar;
        return this;
    }

    @Override
    public void onPageSelected(int i) {
        mActionBar.setSelectedNavigationItem(i);
    }

    @Override
    public void onPageScrolled(int i, float v, int j) { /* Override */ }

    @Override
    public void onPageScrollStateChanged(int i) { /* Override */ }
}
