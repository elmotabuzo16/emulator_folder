package com.vitalityactive.va.shared.activities;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.shared.content.OnboardingContent;
import com.vitalityactive.va.utilities.TextUtilities;

public abstract class BaseOnboardingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setupDependencyInjection();
        setupContent();
    }

    protected abstract void setupDependencyInjection();

    protected abstract @LayoutRes int getLayoutId();

    @NonNull
    protected abstract OnboardingContent getContent();

    protected abstract void navigateAfterGotItTapped();

    public void onGetStartedTapped(View view) {
        navigateAfterGotItTapped();
    }

    protected void setupContent() {
        setupContent(getContent());
    }

    public void setupContent(OnboardingContent content) {
        setCamelCasedTextView(R.id.onboarding_title, content.getOnboardingTitle());

        setupIcon(1, R.id.onboarding_section1_icon);
        setTextView(R.id.onboarding_section1_title, content.getOnboardingSection1Title());
        setTextView(R.id.onboarding_section1_content, content.getOnboardingSection1Content());
        setupIcon(2, R.id.onboarding_section2_icon);
        setTextView(R.id.onboarding_section2_title, content.getOnboardingSection2Title());
        setTextView(R.id.onboarding_section2_content, content.getOnboardingSection2Content());
        setupIcon(3, R.id.onboarding_section3_icon);
        setTextView(R.id.onboarding_section3_title, content.getOnboardingSection3Title());
        setTextView(R.id.onboarding_section3_content, content.getOnboardingSection3Content());

        setupSecondaryButton();
    }

    public void setupSecondaryButton() {
        View disclaimer = findViewById(R.id.vhr_disclaimer_button);
        View learnMore = findViewById(R.id.learn_more_button);
        if (disclaimer != null) {
            setupSecondaryButton((Button) disclaimer);
        } else if (learnMore != null) {
            setupSecondaryButton((Button) learnMore);
        }
    }

    protected void setupSecondaryButton(Button button) {
        // default no-op
    }

    public void setupIcon(int number, @IdRes int iconId) {
        ImageView image = (ImageView) findViewById(iconId);
        switch (number) {
            case 1:
                setupSection1Icon(image);
                break;
            case 2:
                setupSection2Icon(image);
                break;
            case 3:
                setupSection3Icon(image);
                break;
        }
    }

    @SuppressWarnings("UnusedParameters")
    protected void setupSection1Icon(ImageView image) {
        // no-op by default
    }

    @SuppressWarnings("UnusedParameters")
    protected void setupSection2Icon(ImageView image) {
        // no-op by default
    }

    @SuppressWarnings("UnusedParameters")
    protected void setupSection3Icon(ImageView image) {
        // no-op by default
    }

    protected void setCamelCasedTextView(@IdRes int viewId, String text) {
        setTextView(viewId, TextUtilities.toCamelCase(text));
    }

    protected void setTextView(@IdRes int viewId, String text) {
        ((TextView) findViewById(viewId)).setText(text);
    }
}
