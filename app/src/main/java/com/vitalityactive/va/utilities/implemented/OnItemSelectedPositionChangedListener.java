package com.vitalityactive.va.utilities.implemented;

import android.view.View;
import android.widget.AdapterView;

public abstract class OnItemSelectedPositionChangedListener implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        onSelectedItemChanged(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    protected abstract void onSelectedItemChanged(int position);
}
