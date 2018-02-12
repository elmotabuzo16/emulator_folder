package com.vitalityactive.va.settings;

import com.vitalityactive.va.Presenter;

public interface SettingChangePasswordPresenter<UserInterface extends SettingChangePasswordPresenter.UserInterface> extends Presenter<UserInterface> {

    void attemptChangePassword(String currentPassword, String newPassword, String confirmPassword);


    interface UserInterface {
        void setUpView();
        void submitChangedPassword();
        void resetStateNewPassword();
        void resetStateConfirm();
        void addFocusListener();
        boolean validate(final String password);
        void showLoadingIndicator();
        void hideLoadingIndicator();
        void showChangePasswordConfirmation();
        void showIncorrectCurrentPasswordError();
        void showIncorrectNewPasswordValidation();
        void showIncorrectConfirmValidation();
        void showSpecialCharacterValidation();
        void hideNotMatchWarning();
        void showPasswordNotEqual();
        void savePassword(String password);
    }
}
