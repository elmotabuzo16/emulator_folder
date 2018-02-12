package com.vitalityactive.va.vna.onboarding;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.shared.activities.BaseOnboardingActivity;
import com.vitalityactive.va.shared.content.OnboardingContent;
import com.vitalityactive.va.vna.content.VNAContent;

import javax.inject.Inject;

public class VNAOnboardingActivity extends BaseOnboardingActivity {
    @Inject
    VNAContent content;

    @Override
    protected void setupDependencyInjection() {
        getDependencyInjector().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vhr_onboarding;
    }

    @NonNull
    @Override
    protected OnboardingContent getContent() {
        return content;
    }

    @Override
    protected void navigateAfterGotItTapped() {
        navigationCoordinator.navigateAfterVNAOnboardingGotItTapped(this);
    }

    @Override
    protected void setupSecondaryButton(Button button) {
        button.setText(R.string.learn_more_button_title_104);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationCoordinator.navigateToVNAMenu(VNAOnboardingActivity.this, MenuItemType.LearnMore);
            }
        });
    }

    @Override
    protected void setupSection1Icon(ImageView image) {
        image.setImageResource(R.drawable.onboarding_earn_points);
    }

    @Override
    protected void setupSection2Icon(ImageView image) {
        image.setImageResource(R.drawable.understand_nutrition);
    }

    @Override
    protected void setupSection3Icon(ImageView image) {
        image.setImageResource(R.drawable.complete_sections);
    }

    @Override
    public void setupContent(OnboardingContent content) {
        setCamelCasedTextView(R.id.onboarding_title, content.getOnboardingTitle());


        setupIcon(3, R.id.onboarding_section1_icon);
        setTextView(R.id.onboarding_section1_title, content.getOnboardingSection1Title());
        setTextView(R.id.onboarding_section1_content, content.getOnboardingSection1Content());

        setupIcon(1, R.id.onboarding_section2_icon);
        setTextView(R.id.onboarding_section2_title, content.getOnboardingSection2Title());
        setTextView(R.id.onboarding_section2_content, content.getOnboardingSection2Content());

        setupIcon(2, R.id.onboarding_section3_icon);
        setTextView(R.id.onboarding_section3_title, content.getOnboardingSection3Title());
        setTextView(R.id.onboarding_section3_content, content.getOnboardingSection3Content());

        setupSecondaryButton();
    }
}
