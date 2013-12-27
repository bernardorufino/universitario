package br.com.bernardorufino.android.universitario.view.activities.main;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import br.com.bernardorufino.android.universitario.application.Facets;
import com.google.common.base.Throwables;

public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        try {
            return Facets.FRAGMENTS.get(i).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public int getCount() {
        return Facets.FRAGMENTS.size();
    }
}
