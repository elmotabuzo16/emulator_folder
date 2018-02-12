package com.vitalityactive.va.userpreferences;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;

public class RememberMePreferencePresenter extends DefaultUserPreferencePresenter {

    private final PartyInformationRepository partyInformationRepository;
    private final DeviceSpecificPreferences deviceSpecificPreferences;
    private UserInterface userInterface;

    public RememberMePreferencePresenter(int title, int description, boolean optedIn, int iconID,
                                         Type type, boolean hasToggleSwitch, boolean hasSettingsButton,
                                         PartyInformationRepository partyInformationRepository,
                                         DeviceSpecificPreferences deviceSpecificPreferences) {
        super(title, description, optedIn, iconID, type, hasToggleSwitch, hasSettingsButton, null);
        this.partyInformationRepository = partyInformationRepository;
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
        deviceSpecificPreferences.setRememberMe(checked);
//        if (checked) {
//            deviceSpecificPreferences.setRememberedUsername(partyInformationRepository.getUsername());
//        }
//        else {
//            deviceSpecificPreferences.clearRememberedUsername();
//        }
    }

    @Override
    public boolean isOptedIn() {
        return deviceSpecificPreferences.isRememberMeOn();
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }


    public void enableToggle(boolean enable){
        userInterface.enableSwitch();

        if(!enable) {
            userInterface.disableSwitch();
        }
    }

    public boolean isFingerprintOn(){
        return deviceSpecificPreferences.isFingerprint();
    }

}
