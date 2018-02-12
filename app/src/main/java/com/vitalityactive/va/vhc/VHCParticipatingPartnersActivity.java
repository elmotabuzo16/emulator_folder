package com.vitalityactive.va.vhc;

import android.os.Bundle;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;

public class VHCParticipatingPartnersActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vhc_participating_partners);

        setUpActionBarWithTitle(R.string.participating_partners_title_262)
                .setDisplayHomeAsUpEnabled(true);
    }
}
