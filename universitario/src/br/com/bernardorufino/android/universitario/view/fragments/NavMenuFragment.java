package br.com.bernardorufino.android.universitario.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import br.com.bernardorufino.android.universitario.R;
import br.com.bernardorufino.android.universitario.helpers.CustomViewHelper;
import br.com.bernardorufino.android.universitario.view.components.MenuItemView;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class NavMenuFragment extends RoboFragment {

    @InjectView(R.id.menu_list) private LinearLayout mMenuList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_menu, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeItems();
    }

    private View.OnClickListener mOnItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MenuItemView item = (MenuItemView) view;
            onItemClick(item.getId());
        }
    };

    private void onItemClick(int itemId) {
        switch (itemId) {
            case R.id.menu_item_username:
                CustomViewHelper.flash(this, "Username");
                break;
            case R.id.menu_item_info:
                CustomViewHelper.flash(this, "Info");
                break;
        }
    }

    private void initializeItems() {
        for (int i = 0, n = mMenuList.getChildCount(); i < n; i++) {
            MenuItemView item = (MenuItemView) mMenuList.getChildAt(i);
            item.setOnClickListener(mOnItemClickListener);
            if (item.getId() == R.id.menu_item_username) {
                // TODO: Pick user name
                item.setText("Bernardo Rufino");
            }
        }
    }


}
