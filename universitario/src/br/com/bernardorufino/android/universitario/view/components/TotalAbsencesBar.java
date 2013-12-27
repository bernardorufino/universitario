package br.com.bernardorufino.android.universitario.view.components;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.bernardorufino.android.universitario.R;

import static com.google.common.base.Preconditions.*;

public class TotalAbsencesBar extends FrameLayout {

    private static final int DECIMAL_PRECISION = 2;
    private static final int PRECISION_FACTOR = (int) Math.pow(10, DECIMAL_PRECISION);

    private ProgressBar mBar;
    private TextView mText;
    private double mCurrent;
    private Drawable mBarDrawable;
    private int mTotal;

    public TotalAbsencesBar(Context context) {
        super(context);
        initialize();
    }

    public TotalAbsencesBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TotalAbsencesBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        inflate(getContext(), R.layout.component_total_absences_bar, this);
        mBar = (ProgressBar) findViewById(R.id.comp_total_abscs_bar_bar);
        mText =  (TextView) findViewById(R.id.comp_total_abscs_bar_text);
        mBar.setIndeterminate(false);
        LayerDrawable layers = (LayerDrawable) getContext().getResources()
                .getDrawable(R.drawable.frag_att_total_abscs_bar);
        mBar.setProgressDrawable(layers);
        mBarDrawable = layers.findDrawableByLayerId(android.R.id.progress);
    }

    public synchronized int getTotal() {
        return mTotal;
    }

    public synchronized TotalAbsencesBar setTotal(int total) {
        checkArgument(total >= 0, "total must be >= 0");
        mTotal = total;
        return this;
    }

    public synchronized TotalAbsencesBar setCurrent(double current) {
        checkArgument(current >= 0, "current must be >= 0");
        mCurrent = current;
        return this;
    }

    public synchronized double getCurrent() {
        return mCurrent;
    }

    public void draw() {
        int color = AbsencesBarHelper.getColor(getContext(), mCurrent, mTotal);
        mBarDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        int max = mTotal * PRECISION_FACTOR;
        int progress = (int) (mCurrent * PRECISION_FACTOR);
        mBar.setMax(max);
        mBar.setProgress(Math.min(max, progress));
        mText.setText(AbsencesBarHelper.getStatusText(mCurrent, mTotal));
    }
}
