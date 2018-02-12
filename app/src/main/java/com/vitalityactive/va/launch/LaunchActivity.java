package com.vitalityactive.va.launch;

import android.os.Bundle;

import com.vitalityactive.va.BaseActivity;

public class LaunchActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationCoordinator.navigateOnLaunch(this);
    }
}
