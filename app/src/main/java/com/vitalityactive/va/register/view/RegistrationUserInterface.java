package com.vitalityactive.va.register.view;

public interface RegistrationUserInterface {
    void allowRegistration();

    void disallowRegistration();

    void showLoadingIndicator();

    void hideLoadingIndicator();

    void navigateAfterSuccessfulRegistration();

    void showConnectionErrorMessage();

    void showGenericErrorMessage();

    void showInvalidEmailOrInsurerCodeErrorMessage();

    void showAlreadyRegisteredErrorMessage();

    void setUsername(CharSequence username);
}
