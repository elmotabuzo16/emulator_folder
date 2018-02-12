package com.vitalityactive.va.onboarding;

import com.vitalityactive.va.Presenter;

public interface OnboardingPresenter extends Presenter<OnboardingPresenter.UserInterface> {
    OnboardingViewModel getViewModel();

    void onSkipOnboarding();

    void setUserInterface(UserInterface userInterface);

    interface UserInterface {
        void skipOnboarding();
    }
}
