package com.vitalityactive.va.userpreferences;

import com.vitalityactive.va.Presenter;

public interface UserPreferencePresenter extends Presenter<UserPreferencePresenter.UserInterface> {
    void onToggle(boolean enabled);

    boolean hasToggleSwitch();

    boolean hasSettingsButton();

    int getTitle();

    int getDescription();

    String getEmail();

    boolean isOptedIn();

    int getIconID();

    enum Type {Email, Notifications, Analytics, CrashReports, Fingerprint, RememberMe, ShareVitalityStatus}

    Type getType();

    interface UserInterface {
        void showErrorMessage();
        void disableSwitch();
        void enableSwitch();
        void synchroniseOptedInState();
    }

    public interface SettingsToggleChangeListener{
        void onToggle(boolean enabled);
    }
}
