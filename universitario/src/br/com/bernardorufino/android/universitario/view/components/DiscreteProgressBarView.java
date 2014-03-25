package br.com.bernardorufino.android.universitario.view.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import br.com.bernardorufino.android.universitario.R;
import br.com.bernardorufino.android.universitario.helpers.CustomHelper;

import static com.google.common.base.Preconditions.*;

public class DiscreteProgressBarView extends LinearLayout {

    /* FIXME: If one of the attrs is -1, it silently fails */
    private static final int INT_UNDEFINED = -1;
    private static final float FLOAT_UNDEFINED = -1;
    private static final int DEFAULT_SPACE = 2; // in dp
    private static final int DEFAULT_OFF_COLOR = 0xFFFF_FFFF;
    private static final int DEFAULT_ON_COLOR = 0xFF00_FF00;

    private int mTotal = INT_UNDEFINED;
    private float mCurrent = FLOAT_UNDEFINED;
    private int mSpace; // Initialized in initializeView() because getContext() is needed
    private int mOnColor = DEFAULT_ON_COLOR;
    private int mOffColor = DEFAULT_OFF_COLOR;

    public DiscreteProgressBarView(Context context) {
        super(context);
        initializeView();
    }

    public DiscreteProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
        initializeAttributes(attrs);
    }

    private void initializeView() {
        setOrientation(HORIZONTAL);
        mSpace = getDefaultSpaceInPx(getContext());
    }

    private void initializeAttributes(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.DiscreteProgressBarView);
        try {
            mSpace = array.getDimensionPixelSize(R.styleable.DiscreteProgressBarView_space, mSpace);
            int total = array.getInt(R.styleable.DiscreteProgressBarView_total, INT_UNDEFINED);
            if (total != INT_UNDEFINED) setTotal(total);
            float current = array.getFloat(R.styleable.DiscreteProgressBarView_current, FLOAT_UNDEFINED);
            if (current != FLOAT_UNDEFINED) setCurrent(current);
        } finally {
            array.recycle();
        }
        draw(); // it takes care of needed attributes
    }

    public void draw() {
        checkState(mTotal != INT_UNDEFINED, "attribute total must be provided.");
        checkState(mCurrent != FLOAT_UNDEFINED, "attribute current must be provided.");

        int partialUnitIndex = (int) mCurrent;
        float partialUnitValue = mCurrent - ((int) mCurrent);
        if (getChildCount() != mTotal) createUnits();
        for (int i = 0; i < mTotal; i++) {
            ProgressUnitView unit = (ProgressUnitView) getChildAt(i);
            unit.setOnColor(mOnColor)
                .setOffColor(mOffColor)
                .setProgress((i < partialUnitIndex) ? 1 : (i == partialUnitIndex) ? partialUnitValue : 0)
                .draw();
        }
    }

    private void createUnits() {
        CustomHelper.log("Creating views");
        removeAllViews();
        for (int i = 0; i < mTotal; i++) {
            ProgressUnitView unit = new ProgressUnitView(getContext(), mOffColor);
            LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
            params.setMargins((i == 0) ? 0 : mSpace, 0, 0, 0);
            unit.setLayoutParams(params);
            addView(unit);
        }
    }

    public DiscreteProgressBarView setProgress(float current, int total) {
        setTotal(total);
        setCurrent(current);
        return this;
    }

    public void setTotal(int total) {
        checkArgument(total > 0, "total attribute must be positive.");
        mTotal = total;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setCurrent(float current) {
        checkArgument(current >= 0, "current attribute must be greater than or equal to 0.");
        checkArgument(current <= mTotal, "current attribute, which is " + current + ", " +
                "must be smaller than the total of " + mTotal + ".");
        mCurrent = current;
    }

    public float getCurrent() {
        return mCurrent;
    }

    public void setSpace(int space) {
        mSpace = space;
    }

    public int getSpace() {
        return mSpace;
    }

    public DiscreteProgressBarView setColors(int onColor, int offColor) {
        setOnColor(onColor);
        setOffColor(offColor);
        return this;
    }

    public int getOnColor() {
        return mOnColor;
    }

    public void setOnColor(int onColor) {
        mOnColor = onColor;
    }

    public int getOffColor() {
        return mOffColor;
    }

    public void setOffColor(int offColor) {
        mOffColor = offColor;
    }

    private static int getDefaultSpaceInPx(Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_SPACE,
                context.getResources().getDisplayMetrics()
        );
    }
}
