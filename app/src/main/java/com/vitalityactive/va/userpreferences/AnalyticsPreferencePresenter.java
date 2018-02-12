package com.vitalityactive.va.userpreferences;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;

public class AnalyticsPreferencePresenter extends DefaultUserPreferencePresenter {

    private final DeviceSpecificPreferences deviceSpecificPreferences;
    private UserInterface userInterface;

    public AnalyticsPreferencePresenter(int title, int description, boolean optedIn, int iconID,
                                        Type type, boolean hasToggleSwitch, boolean hasSettingsButton,
                                        DeviceSpecificPreferences deviceSpecificPreferences) {
        super(title, description, optedIn, iconID, type, hasToggleSwitch, hasSettingsButton, null);
        this.deviceSpecificPreferences = deviceSpecificPreferences;
    }

    @Override
    public void onUserInterfaceAppeared() {
        if (userInterface != null) {
            userInterface.synchroniseOptedInState();
        }
    }

    @Override
    public void onToggle(boolean checked) {
        deviceSpecificPreferences.setAnalytics(checked);
    }

    @Override
    public boolean isOptedIn() {
        return deviceSpecificPreferences.isAnalyticsEnabled();
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

}
