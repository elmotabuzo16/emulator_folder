package com.vitalityactive.va.utilities.implemented;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class TextWatcherWithDefaultBeforeAndAfter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
