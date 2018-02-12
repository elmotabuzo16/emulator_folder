package com.vitalityactive.va.mwb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.mwb.content.MWBHealthAttributeContent;
import com.vitalityactive.va.snv.content.SNVHealthAttributeContent;

import javax.inject.Inject;

public class MWBOnBoardingActivity extends BaseActivity {

    @Inject
    MWBHealthAttributeContent content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mwb_onboarding);
        getDependencyInjector().inject(this);
        setUpActionBarWithTitle("").setDisplayHomeAsUpEnabled(true);
        setUpContent();
    }

    private void setUpContent() {
        setTextView(R.id.onboarding_title, content.getOnboardingTitle());
        setTextView(R.id.onboarding_section1_title, content.getOnboardingSection1Title());
        setTextView(R.id.onboarding_section1_content, content.getOnboardingSection1Content());
        setTextView(R.id.onboarding_section2_title, content.getOnboardingSection2Title());
        setTextView(R.id.onboarding_section2_content, content.getOnboardingSection2Content());
        setTextView(R.id.onboarding_section3_title, content.getOnboardingSection3Title());
        setTextView(R.id.onboarding_section3_content, content.getOnboardingSection3Content());
    }

    private void setTextView(int viewId, String text) {
        ((TextView) findViewById(viewId)).setText(text);
    }

    public void onGetStartedTapped(View view) {
        navigationCoordinator.navigateToScreeningsAndVaccination(this);
    }

    public void onLearnMoreTapped(View view) {
        navigationCoordinator.navigateAfterSNVLearnMoreTapped(this);
    }

}
