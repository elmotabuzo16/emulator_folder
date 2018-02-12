package com.vitalityactive.va.vhc;

import android.os.Bundle;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;

public class VHCHelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vhc_help);

        setUpActionBarWithTitle(R.string.help_button_141)
                .setDisplayHomeAsUpEnabled(true);
    }
}
