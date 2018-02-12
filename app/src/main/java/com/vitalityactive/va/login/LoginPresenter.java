package com.vitalityactive.va.login;

import android.support.annotation.NonNull;

import com.vitalityactive.va.Presenter;

public interface LoginPresenter extends Presenter<LoginPresenter.UserInterface> {
    LoginViewModel getViewModel();

    void onUsernameChanged(@NonNull CharSequence emailAddress);

    void onPasswordChanged(@NonNull CharSequence password);

    void onUsernameEntered();

    void setUsernameFromIntent(String username);

    void onUserTriesToLogIn();

    interface UserInterface {
        void updateLoginEnabled(boolean isLoginEnabled);

        void showInvalidEmailAddressMessage();

        void hideInvalidUsernameMessage();

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showInvalidCredentialsLoginErrorMessage();

        void showGenericLoginErrorMessage();

        void showGenericFingerPrintErrorMessage();

        void navigateAfterSuccessfulLogin();

        void showConnectionErrorMessage();

        void showInvalidCredentialsLoginErrorMessageWithForgotPassword();

        void showLockedAccountLoginErrorMessage();

        void navigatetoscreening();

        void showEnrollFingerPrintDialog();

        void deleteProfilePhoto();
    }
}
