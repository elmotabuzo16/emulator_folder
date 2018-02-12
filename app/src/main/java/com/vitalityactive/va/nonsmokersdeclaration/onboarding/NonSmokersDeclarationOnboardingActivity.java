package com.vitalityactive.va.nonsmokersdeclaration.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersDeclarationContent;

import javax.inject.Inject;

public class NonSmokersDeclarationOnboardingActivity extends BaseActivity {

    @Inject
    NonSmokersDeclarationContent content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_smokers_declaration_onboarding);
        getDependencyInjector().inject(this);
        setUpActionBarWithTitle("").setDisplayHomeAsUpEnabled(true);
        setUpContent();
    }

    private void setUpContent() {
        setTextView(R.id.title, content.getOnboardingTitle());
        setTextView(R.id.non_smoker_earn_points_title, content.getOnboardingSection1Title());
        setTextView(R.id.non_smoker_earn_points_text, content.getOnboardingSection1Content());
        setTextView(R.id.non_smoker_smoking_status_title, content.getOnboardingSection2Title());
        setTextView(R.id.non_smoker_smoking_status_text, content.getOnboardingSection2Content());
    }

    private void setTextView(int viewId, String text) {
        ((TextView) findViewById(viewId)).setText(text);
    }

    public void onGetStartedTapped(View view) {
        navigationCoordinator.navigateOnGetStartedFromNonSmokersDeclarationOnboarding(this);
    }

    public void onLearnMoreTapped(View view) {
        navigationCoordinator.navigateOnLearnMoreFromNonSmokersDeclarationOnboarding(this);
    }
}
