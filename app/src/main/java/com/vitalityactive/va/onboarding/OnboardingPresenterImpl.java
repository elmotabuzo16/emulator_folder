package com.vitalityactive.va.onboarding;

import com.vitalityactive.va.DeviceSpecificPreferences;

public class OnboardingPresenterImpl implements OnboardingPresenter, OnboardingViewModel {
    private DeviceSpecificPreferences preferences;
    private UserInterface userInterface;

    public OnboardingPresenterImpl(DeviceSpecificPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public OnboardingViewModel getViewModel() {
        return this;
    }

    @Override
    public void onSkipOnboarding() {
        userInterface.skipOnboarding();
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {

    }

    @Override
    public void onUserInterfaceAppeared() {
        if (!shouldShowOnboarding()) {
            onSkipOnboarding();
        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {

    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public boolean shouldShowOnboarding() {
        return !preferences.hasCurrentUserSeenOnboarding();
    }

    @Override
    public int getNumberOfPages() {
        return 4;
    }

    @Override
    public boolean isOnboardingPageAtPosition(int position) {
        return position >= 0 && position < 3;
    }
}
