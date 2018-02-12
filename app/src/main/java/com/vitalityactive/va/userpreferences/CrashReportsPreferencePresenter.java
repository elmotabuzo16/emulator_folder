package com.vitalityactive.va.userpreferences;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;

public class CrashReportsPreferencePresenter extends DefaultUserPreferencePresenter {

    private final DeviceSpecificPreferences deviceSpecificPreferences;
    private UserInterface userInterface;

    public CrashReportsPreferencePresenter(int title, int description, boolean optedIn, int iconID,
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
        deviceSpecificPreferences.setEnableCrashReports(checked);
    }

    @Override
    public boolean isOptedIn() {
        return deviceSpecificPreferences.isCrashReportsEnabled();
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

}
