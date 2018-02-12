package com.vitalityactive.va.userpreferences.entities;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.userpreferences.UserPreferencePresenter;

public class DefaultUserPreferencePresenter implements UserPreferencePresenter {
    private boolean optedIn;
    private final int title;
    private final int description;
    private final int iconID;
    private final Type type;
    private final boolean hasToggleSwitch;
    private final boolean hasSettingsButton;

    protected PartyInformationRepository partyRepo;

    private UserPreferencePresenter.SettingsToggleChangeListener mSettingsToggleChangeListener;

    private UserInterface userInterfacePreference;

    public DefaultUserPreferencePresenter(int title, int description, boolean optedIn,
                                          int iconID, Type type, boolean hasToggleSwitch,
                                          boolean hasSettingsButton,
                                          UserPreferencePresenter.SettingsToggleChangeListener mSettingsToggleChangeListener) {

        this.title = title;
        this.description = description;
        this.optedIn = optedIn;
        this.iconID = iconID;
        this.type = type;
        this.hasToggleSwitch = hasToggleSwitch;
        this.hasSettingsButton = hasSettingsButton;
        this.mSettingsToggleChangeListener = mSettingsToggleChangeListener;
    }

    public DefaultUserPreferencePresenter(int title, int description, boolean optedIn,
                                          int iconID, Type type, boolean hasToggleSwitch,
                                          boolean hasSettingsButton) {
        this(title, description, optedIn, iconID, type, hasToggleSwitch, hasSettingsButton, null);
    }

    @Override
    public void onToggle(boolean checked) {
        optedIn = checked;
        if(null != mSettingsToggleChangeListener) {
            mSettingsToggleChangeListener.onToggle(checked);
        }
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
    }

    @Override
    public void onUserInterfaceAppeared() {
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
    }

    @Override
    public void onUserInterfaceDestroyed() {
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterfacePreference = userInterface;
    }

    @Override
    public boolean hasToggleSwitch() {
        return hasToggleSwitch;
    }

    @Override
    public boolean hasSettingsButton() {
        return hasSettingsButton;
    }

    @Override
    public int getTitle() {
        return title;
    }

    @Override
    public int getDescription() {
        return description;
    }

    protected void setRepo(PartyInformationRepository partyInfo){
        this.partyRepo = partyInfo;
    }

    @Override
    public String getEmail(){
        setRepo(partyRepo);
        return partyRepo.getEmailFromDevicePreference();
    }

    @Override
    public boolean isOptedIn() {
        return optedIn;
    }

    @Override
    public int getIconID() {
        return iconID;
    }

    @Override
    public Type getType() {
        return type;
    }

    public void enableSwitch(){
        this.userInterfacePreference.enableSwitch();
    }

    public void disableSwitch() {
        this.userInterfacePreference.disableSwitch();
    }

}
