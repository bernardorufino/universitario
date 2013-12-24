package br.com.bernardorufino.android.universitario.view.components;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import br.com.bernardorufino.android.universitario.R;
import br.com.bernardorufino.android.universitario.helpers.ColorHelper;

import static com.google.common.base.Preconditions.*;

public class ProgressUnitView extends View {

    private static final double DEFAULT_PROGRESS = 0;

    private int mOnColor;
    private int mOffColor;
    private final Drawable mBackground;
    private double mProgress;

    public ProgressUnitView(Context context, int offColor) {
        super(context);
        mOffColor = offColor;
        mOnColor = mOffColor; // It simply doesn't "work" until the user set the color
        mBackground = getContext().getResources().getDrawable(R.drawable.progress_unit_on);
        setBackgroundDrawable(mBackground);
        checkState(mBackground == getBackground());
        setProgress(DEFAULT_PROGRESS);
        draw();
    }

    public ProgressUnitView setProgress(double progress) {
        checkArgument(0 <= progress && progress <= 1, "progress is " + progress + ", " +
                "but it should be between 0 and 1 (inclusive)");
        mProgress = progress;
        return this;
    }

    public double getProgress() {
        return mProgress;
    }

    public int getOffColor() {
        return mOffColor;
    }

    public int getOnColor() {
        return mOnColor;
    }

    public ProgressUnitView setOnColor(int onColor) {
        mOnColor = onColor;
        return this;
    }

    public void draw() {
        int color = ColorHelper.interpolate(mOffColor, mOnColor, mProgress);
        mBackground.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }
}
