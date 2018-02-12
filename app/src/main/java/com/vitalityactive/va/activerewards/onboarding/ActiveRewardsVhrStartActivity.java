package com.vitalityactive.va.activerewards.onboarding;

import android.os.Bundle;
import android.view.View;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;

public class ActiveRewardsVhrStartActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_rewards_start_vhr);

        setUpActionBarWithTitle(R.string.vhr_get_started)
                .setDisplayHomeAsUpEnabled(true);
    }

    public void onStartVhrClicked(View view){
        navigationCoordinator.navigateAfterStartVhrButtonClicked(ActiveRewardsVhrStartActivity.this);
    }
}
