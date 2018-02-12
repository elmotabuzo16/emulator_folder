package com.vitalityactive.va.vitalitystatus;

import android.os.Bundle;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;

public class PointsEarningMetricDetail extends BaseActivity {

    public static final String TYPE = "TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_earning_metric_detail);

        setActionBarTitleAndDisplayHomeAsUp(getIntent().getStringExtra(TYPE));
    }
}
