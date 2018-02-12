package com.vitalityactive.va.vhc;

import android.os.Bundle;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;

public class VHCHistoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vhc_history);

        setUpActionBarWithTitle(R.string.AR_rewards_history_segment_title_670)
                .setDisplayHomeAsUpEnabled(true);
    }
}
