package com.vitalityactive.va.onboarding;

public interface OnboardingViewModel {
    boolean shouldShowOnboarding();

    int getNumberOfPages();

    boolean isOnboardingPageAtPosition(int position);
}
