package com.vitalityactive.va.forgotpassword;

import com.vitalityactive.va.Presenter;

public interface ForgotPasswordPresenter extends Presenter<ForgotPasswordPresenter.Ui> {
    void onForgotPasswordTapped(String username);

    interface Ui {
        void showInvalidEmailAddressMessage();

        void hideInvalidEmailAddressMessage();

        void onForgotPasswordRequestSuccessful();

        void onForgotPasswordRequestFailed();

        void onRequestBusy();

        void onRequestCompleted();

        void onEmailNotRegistered();
    }
}
