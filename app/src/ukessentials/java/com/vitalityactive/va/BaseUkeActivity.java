package com.vitalityactive.va;

import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

public class BaseUkeActivity extends BaseActivity {
    @Inject
    protected UkeNavigationCoordinator ukeNavigationCoordinator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDependencyInjector().inject(this);
    }
}
