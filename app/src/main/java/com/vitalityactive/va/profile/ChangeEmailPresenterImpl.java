package com.vitalityactive.va.profile;

import android.support.annotation.NonNull;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.register.entity.EmailAddress;
import com.vitalityactive.va.register.entity.Password;

public class ChangeEmailPresenterImpl implements ChangeEmailPresenter, EventListener<PersonalDetailsEvent> {

    private final PartyInformationRepository partyInfoRepo;
    private final EventDispatcher eventDispatcher;
    private final PersonalDetailsInteractor personalDetailsInteractor;
    private final DeviceSpecificPreferences deviceSpecificPreferences;

    private UI userInterface;
    private EmailAddress existingEmailAddress = new EmailAddress("");
    private EmailAddress newEmailAddress = new EmailAddress("");
    private Password authPassword = new Password("");

    public ChangeEmailPresenterImpl(EventDispatcher eventDispatcher, PartyInformationRepository partyInfoRepo,
                                    PersonalDetailsInteractor personalDetailsInteractor, DeviceSpecificPreferences deviceSpecificPreferences) {
        this.eventDispatcher = eventDispatcher;
        this.partyInfoRepo = partyInfoRepo;
        this.personalDetailsInteractor = personalDetailsInteractor;
        this.deviceSpecificPreferences = deviceSpecificPreferences;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
    }

    @Override
    public void onUserInterfaceAppeared() {
        eventDispatcher.addEventListener(PersonalDetailsEvent.class, this);
        userInterface.hideLoadingIndicator();
        showChangeEmailInfo();
    }

    @Override
    public boolean isDoneEnabled() {
        return newEmailAddress.isValid() && !(newEmailAddress.getText().length() > 100);
    }

    @Override
    public CharSequence getNewEmailText() {
        return newEmailAddress.getText();
    }

    @Override
    public void showPasswordConfirmation() {
        userInterface.showPasswordConfirmation();
    }

    private void showChangeEmailInfo() {
        String emailAddress = partyInfoRepo.getEmailFromDevicePreference();
        userInterface.showChangeEmailInfo(emailAddress);
    }

    @Override
    public void onUserTriesToChangeEmail(String newEmail) {
        userInterface.showLoadingIndicator();
        newEmailAddress.setText(newEmail);
        personalDetailsInteractor.verifyEmail(newEmailAddress);
    }

    @Override
    public void onUserConfirmsChangeEmail(String newEmail, String password) {
        userInterface.showLoadingIndicator();
        existingEmailAddress.setText(partyInfoRepo.getEmailFromDevicePreference());
        authPassword.setText(password);
        newEmailAddress.setText(newEmail);
        personalDetailsInteractor.changeEmail(existingEmailAddress, newEmailAddress, authPassword);
    }

    @Override
    public void onNewEmailEntered() {
        if (newEmailAddress.isValid() || newEmailAddress.getText().length() == 0) {
            userInterface.hideInvalidEmailAddressMessage();
        } else if (newEmailAddress.getText().length() > 100) {
            userInterface.showInvalidLengthEmailAddressMessage();
        } else {
            userInterface.showInvalidEmailAddressMessage();
        }
    }

    @Override
    public void onEmailTextChanged(@NonNull CharSequence usernameText) {
        newEmailAddress.setText(usernameText);
        userInterface.updateDoneEnabled(isDoneEnabled());
        if (newEmailAddress.isValid() || newEmailAddress.getText().length() == 0) {
            userInterface.hideInvalidEmailAddressMessage();
        } else if (newEmailAddress.getText().length() > 100) {
            userInterface.showInvalidLengthEmailAddressMessage();
        } else {
            userInterface.showInvalidEmailAddressMessage();
        }
    }

    public void setNewEmailAddress(EmailAddress emailAddress){
        this.newEmailAddress = emailAddress;
    }

    public void setAuthPassword(Password password){
        this.authPassword = password;
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        eventDispatcher.removeEventListener(PersonalDetailsEvent.class, this);
    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void setUserInterface(UI userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void onEvent(PersonalDetailsEvent event) {
        userInterface.hideLoadingIndicator();
        if (event.getEventType() == PersonalDetailsEvent.EventType.CHANGE_EMAIL_SUCCESS) {
            userInterface.updateNewEmailView();
        } else if (event.getEventType() == PersonalDetailsEvent.EventType.CHANGE_EMAIL_FAILED) {
            userInterface.showChangeEmailFailedError();
        } else if (event.getEventType() == PersonalDetailsEvent.EventType.CHANGE_EMAIL_AUTH_ERROR) {
            userInterface.showIncorrectPasswordError();
        } else if (event.getEventType() == PersonalDetailsEvent.EventType.VERIFY_EMAIL_EXISTING) {
            userInterface.showChangeEmailExistingError();
        } else if (event.getEventType() == PersonalDetailsEvent.EventType.VERIFY_EMAIL_FAILED) {
            userInterface.showChangeEmailFailedError();
        } else if (event.getEventType() == PersonalDetailsEvent.EventType.VERIFY_EMAIL_OK) {
            userInterface.showChangeEmailConfirmation();
        } else {
            userInterface.hideLoadingIndicator();
        }
    }

    @Override
    public long getTenantId() {
        return partyInfoRepo.getTenantId();
    }

    @Override
    public String getEncryptedPassword() {
        return deviceSpecificPreferences.getRememberedPassword();
    }
}
