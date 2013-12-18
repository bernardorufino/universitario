package br.com.bernardorufino.android.universitario.view.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.bernardorufino.android.universitario.R;

import static com.google.common.base.Preconditions.*;

/*
* Class to be extended and given a layout in the constructor, as the abstraction of a menu item, composed
* of image and text. The layout has to provide a ImageView with id comp_menu_item_icon, a TextView with
* id comp_menu_item_text and two dividers (can be of any View class) with corresponding ids of
* comp_menu_item_above_divider and comp_menu_item_below_divider.
* */
public abstract class MenuItemView extends FrameLayout {

    public static class Divider {
        public static int NONE = 0;
        public static int BELOW = 1;
        public static int ABOVE = 2;
        public static int BOTH = 3;
    }

    private static int DEFAULT_DIVIDER = Divider.NONE;

    private View mAboveDivider;
    private View mBelowDivider;
    private ImageView mIconView;
    private TextView mTextView;

    public MenuItemView(Context context, AttributeSet attrs, int layoutId) {
        super(context, attrs);
        inflate(context, layoutId, this);
        initializeView();
        initializeAttributes(attrs);
    }

    private void initializeView() {
        if (isInEditMode()) return;
        mIconView = (ImageView) checkNotNull(findViewById(R.id.comp_menu_item_icon));
        mTextView = (TextView) checkNotNull(findViewById(R.id.comp_menu_item_text));
        mAboveDivider = checkNotNull(findViewById(R.id.comp_menu_item_above_divider));
        mBelowDivider = checkNotNull(findViewById(R.id.comp_menu_item_below_divider));
        checkState(getChildCount() == 1, "There should be only one child, the container layout.");
    }

    private void initializeAttributes(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MenuItemView);
        try {
            setIcon(array.getDrawable(R.styleable.MenuItemView_icon));
            setText(array.getString(R.styleable.MenuItemView_text));
            setDivider(array.getInt(R.styleable.MenuItemView_divider, DEFAULT_DIVIDER));
        } finally {
            array.recycle();
        }
    }

    private void setDivider(int divider) {
        mAboveDivider.setVisibility((divider == Divider.ABOVE || divider == Divider.BOTH) ? View.VISIBLE : View.GONE);
        mBelowDivider.setVisibility((divider == Divider.BELOW || divider == Divider.BOTH) ? View.VISIBLE : View.GONE);
    }

    public Drawable getIcon() {
        return mIconView.getDrawable();
    }

    public void setIcon(Drawable drawable) {
        mIconView.setImageDrawable(drawable);
    }

    public void setIcon(int resourceId) {
        mIconView.setImageResource(resourceId);
    }

    public CharSequence getText() {
        return mTextView.getText();
    }

    public void setText(String text) {
        mTextView.setText(text);
    }
}
