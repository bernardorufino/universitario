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

    public ProgressUnitView(Context context, int onColor, int offColor) {
        super(context);
        mOnColor = onColor;
        mOffColor = offColor;
        mBackground = getContext().getResources().getDrawable(R.drawable.progress_unit_on);
        setBackgroundDrawable(mBackground);
        checkState(mBackground == getBackground());
        setProgress(DEFAULT_PROGRESS);
    }

    public void setProgress(double progress) {
        checkArgument(0 <= progress && progress <= 1, "progress is " + progress + ", " +
                "but it should be between 0 and 1 (inclusive)");
        mProgress = progress;
        int color = ColorHelper.interpolate(mOffColor, mOnColor, progress);
        mBackground.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public double getProgress() {
        return mProgress;
    }
}
