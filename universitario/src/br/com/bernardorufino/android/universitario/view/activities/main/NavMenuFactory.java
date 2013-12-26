package br.com.bernardorufino.android.universitario.view.activities.main;

import android.support.v4.app.FragmentActivity;
import br.com.bernardorufino.android.universitario.R;
import br.com.bernardorufino.android.universitario.view.fragments.NavMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class NavMenuFactory {

    public static SlidingMenu getInstance(FragmentActivity activity) {
        SlidingMenu menu = configure(new SlidingMenu(activity));
        menu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.nav_menu_frame);
        NavMenuFragment fragment = new NavMenuFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_menu_frame, fragment)
                .commit();
        return menu;
    }

    private static SlidingMenu configure(SlidingMenu menu) {
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowDrawable(R.drawable.menu_shadow);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeEnabled(true);
        menu.setFadeDegree(0.35f);
        return menu;
    }

    // Prevents instantiation
    private NavMenuFactory() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
