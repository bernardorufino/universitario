package br.com.bernardorufino.android.universitario.activities.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.google.common.base.Throwables;

import static br.com.bernardorufino.android.universitario.activities.MainActivity.TAB_FRAGMENTS;

public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        try {
            return TAB_FRAGMENTS.get(i).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public int getCount() {
        return TAB_FRAGMENTS.size();
    }
}
