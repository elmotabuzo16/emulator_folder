package com.vitalityactive.va.register;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.register.view.RegistrationUserInterface;

public interface RegistrationPresenter extends Presenter<RegistrationUserInterface> {

    void onUserTriesToRegister();

    void onUserDismissesErrorMessage();
}
