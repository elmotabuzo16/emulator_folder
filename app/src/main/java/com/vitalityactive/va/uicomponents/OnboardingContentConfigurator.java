package com.vitalityactive.va.uicomponents;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vitalityactive.va.R;

public class OnboardingContentConfigurator {
    private final Button button;
    private final TextView title;
    private final TextView subtitle;

    public OnboardingContentConfigurator(ViewGroup contentView) {
        title = (TextView) contentView.findViewById(R.id.onboarding_title);
        subtitle = (TextView) contentView.findViewById(R.id.onboarding_description);
        button = (Button) contentView.findViewById(R.id.onboarding_got_it_button);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        button.setOnClickListener(onClickListener);
    }

    public OnboardingContentConfigurator setTitleText(int titleResourceId) {
        title.setText(titleResourceId);
        return this;
    }

    public OnboardingContentConfigurator setSubtitleText(int subtitleResourceId) {
        subtitle.setText(subtitleResourceId);
        return this;
    }

    public OnboardingContentConfigurator setTitleText(String titleText) {
        title.setText(titleText);
        return this;
    }

    public OnboardingContentConfigurator setSubtitleText(String subtitleText) {
        subtitle.setText(subtitleText);
        return this;
    }

    public OnboardingContentConfigurator hideButton() {
        button.setVisibility(View.GONE);
        return this;
    }
}
