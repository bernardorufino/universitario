package br.com.bernardorufino.android.universitario.view.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import br.com.bernardorufino.android.universitario.ext.summarizer.Summarizable;
import br.com.bernardorufino.android.universitario.helpers.ViewHelper;

import static com.google.common.base.Preconditions.*;

/* TODO: Add dependents support (see EditTextPreference.setText() source code) */
/* TODO: Add save state (for orientation changes, etc.)  */
public class EditDecimalPreference extends DialogPreference implements Summarizable {

    private final float DEFAULT_VALUE = 0;

    private EditText mEditText;
    private float mValue;

    public EditDecimalPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);
    }

    public EditDecimalPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        mEditText = new EditText(context, attrs);
        /* FIXME: NOT WORKING! */
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEditText.setEnabled(true);
    }

    public float getValue() {
        return mValue;
    }

    public void setValue(float value) {
        mValue = value;
        persistFloat(value);
    }

    private String getText() {
        return Float.toString(getValue());
    }

    @Override
    protected Float onGetDefaultValue(TypedArray a, int index) {
        return a.getFloat(index, DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mValue = getPersistedFloat(Float.NaN);
            checkState(mValue != Float.NaN, "restorePersistedValue is true but there is no persisted value");
        } else {
            checkNotNull(defaultValue, "Asked to set to default value, but it's null.");
            checkState(defaultValue instanceof Float, "Asked to set to default value, but it's not a Float.");
            mValue = (float) defaultValue;
            persistFloat(mValue); // Already checks shouldPersist()
        }
    }

    @Override
    protected View onCreateDialogView() {
        RelativeLayout layout = new RelativeLayout(getContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mEditText.setLayoutParams(params);
        ViewHelper.tryMakeOrphan(mEditText);
        layout.addView(mEditText);
        return layout;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mEditText.setText(getText());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            float value = Float.parseFloat(mEditText.getText().toString());
            if (callChangeListener(value)) {
                mValue = value;
                persistFloat(mValue);
            }
        }
    }

    @Override
    public String getSummaryValue() {
        return FORMATTER.format(mValue);
    }
}
