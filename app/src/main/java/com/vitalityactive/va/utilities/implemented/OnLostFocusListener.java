package com.vitalityactive.va.utilities.implemented;

import android.view.View;

public abstract class OnLostFocusListener implements View.OnFocusChangeListener {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            onLostFocus(v);
        }
    }

    protected abstract void onLostFocus(View v);
}
