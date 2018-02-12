package com.vitalityactive.va.settings;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.register.entity.EmailAddress;
import com.vitalityactive.va.register.entity.Password;

public class SettingChangePasswordPresenterImpl<UserInterface extends SettingChangePasswordPresenter.UserInterface> extends BasePresenter<UserInterface> implements SettingChangePasswordPresenter<UserInterface>, EventListener<ChangePasswordEvent> {
    private final SettingChangePasswordInteractor passwordInteractor;
    private final PartyInformationRepository partyInfoRepository;
    public final EventDispatcher eventDispatcher;
    private final AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    private Password existingPassword;
    private Password newPassword;
    private Password confirmPass;
    public final EmailAddress userName = new EmailAddress("");
    private boolean hasSpecial;



    public SettingChangePasswordPresenterImpl( SettingChangePasswordInteractor passwordInteractor, PartyInformationRepository partyInfoRepository, EventDispatcher eventDispatcher,  AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.passwordInteractor = passwordInteractor;
        this.partyInfoRepository = partyInfoRepository;
        this.eventDispatcher = eventDispatcher;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        super.onUserInterfaceCreated(isNewNavigation);
    }

    @Override
    public void attemptChangePassword(String currentPassword, String newPass, String confirmPassword) {

        existingPassword = new Password("");
        newPassword = new Password("");
        confirmPass = new Password("");
        hasSpecial   = !confirmPassword.matches("[A-Za-z0-9]*");

        existingPassword.setText(currentPassword);
        newPassword.setText(newPass);
        confirmPass.setText(confirmPassword);

        if(!newPassword.isValid()) {
            userInterface.showIncorrectNewPasswordValidation();
            if(!confirmPass.isValid()){
                userInterface.showIncorrectConfirmValidation();
                return;
            }
            return;
        } else if (hasSpecial){
                userInterface.showSpecialCharacterValidation();
                return;
        }else{
            if(!confirmPass.isValid()){
                userInterface.showIncorrectConfirmValidation();
                return;
            }
        }
        //Comment for now. API needs some confirmation
        //userName.setText(partyInfoRepository.getEmailAddress());
        if(newPassword.getText().equals(confirmPass.getText())) {
            userInterface.showLoadingIndicator();
            userInterface.hideNotMatchWarning();
            userName.setText(partyInfoRepository.getEmailFromDevicePreference());
            passwordInteractor.changePassword(existingPassword, newPassword, userName,accessTokenAuthorizationProvider);
        } else {
            userInterface.showPasswordNotEqual();
        }
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        eventDispatcher.addEventListener(ChangePasswordEvent.class, this);
        userInterface.hideLoadingIndicator();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        eventDispatcher.removeEventListener(ChangePasswordEvent.class, this);
    }

    @Override
    public void onUserInterfaceDestroyed() {
        super.onUserInterfaceDestroyed();
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void onEvent(ChangePasswordEvent event) {
        userInterface.hideLoadingIndicator();
        if(event.getEventType() == ChangePasswordEvent.EventType.CHANGE_PASSWORD_SUCCESS){
            userInterface.savePassword(newPassword.toString());
            userInterface.showChangePasswordConfirmation();
        } else if(event.getEventType() == ChangePasswordEvent.EventType.CHANGE_PASSWORD_AUTH_ERROR){
            userInterface.showIncorrectCurrentPasswordError();
        } else if(event.getEventType() == ChangePasswordEvent.EventType.CHANGE_PASSWORD_FAILED) {
            userInterface.showIncorrectCurrentPasswordError();
        } else {
            userInterface.hideLoadingIndicator();
        }
    }
}
