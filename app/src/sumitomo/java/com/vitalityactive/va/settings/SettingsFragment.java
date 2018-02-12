package com.vitalityactive.va.settings;

import android.view.View;
import android.widget.LinearLayout;

import com.vitalityactive.va.R;

public class SettingsFragment extends BaseSettingsFragment {

    protected LinearLayout container;

    @Override
    protected void marketUiUpdate(){
        container = parentView.findViewById(R.id.unit_preferences);
        container.setVisibility(View.GONE);
    }
}