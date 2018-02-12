package com.vitalityactive.va.wellnessdevices.onboarding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.wellnessdevices.Constants;

public class WellnessDevicesOnboardingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wd_onboarding);

        if(isInsideActiveRewardsFlow()){
            ((TextView)findViewById(R.id.btn_wd_got_it)).setText(R.string.link_now_button_text_434);
            TextView secondaryOptionView = (TextView)findViewById(R.id.btn_wd_onboarding_secondary_option);
            secondaryOptionView.setText(R.string.WDA_onboarding_link_later_title_778);
            secondaryOptionView.setVisibility(View.VISIBLE);
        }
    }

    public void onNextClicked(View view) {
        navigationCoordinator.navigateToWellnessDeviceLandingAfterOnboarding(this, isInsideActiveRewardsFlow());
    }

    public void onSecondaryOptionClicked(View view) {
        if(isInsideActiveRewardsFlow()){
            navigationCoordinator.navigateAfterDoneLinkingDevicesFromOnboarding(this);
        } else {
            // TODO Future release requirement
        }
    }

    private boolean isInsideActiveRewardsFlow(){
        return getIntent().getBooleanExtra(Constants.IS_INSIDE_ACTIVE_REWARDS_FLOW, false);
    }
}
