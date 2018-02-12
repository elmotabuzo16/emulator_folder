package com.vitalityactive.va.profile;

import android.support.annotation.NonNull;

import com.vitalityactive.va.Presenter;

public interface ChangeEmailPresenter extends Presenter<ChangeEmailPresenter.UI> {

    void onUserTriesToChangeEmail(String newEmailAddress);

    void onUserConfirmsChangeEmail(String newEmailAddress, String password);

    void onEmailTextChanged(@NonNull CharSequence usernameText);

    void onNewEmailEntered();

    boolean isDoneEnabled();

    CharSequence getNewEmailText();

    void showPasswordConfirmation();

    long getTenantId();

    String getEncryptedPassword();

    interface UI {
        void showChangeEmailInfo(String newEmailAddress);

        void showChangeEmailExistingError();

        void showChangeEmailFailedError();

        void showChangeEmailConfirmation();

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showInvalidEmailAddressMessage();

        void showInvalidLengthEmailAddressMessage();

        void hideInvalidEmailAddressMessage();

        void updateDoneEnabled(boolean isEnabled);

        void showPasswordConfirmation();

        void showIncorrectPasswordError();

        void updateNewEmailView();
    }
}
