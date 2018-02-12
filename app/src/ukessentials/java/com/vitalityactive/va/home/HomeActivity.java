package com.vitalityactive.va.home;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.R;

/**
 * Created by peter.ian.t.betos on 05/01/2018.
 */

public class HomeActivity extends BaseHomeActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hiding help on UKE build
        navigationView.getMenu().findItem(R.id.navigation_item_help).setVisible(BuildConfig.SHOW_HELP);
    }
}
