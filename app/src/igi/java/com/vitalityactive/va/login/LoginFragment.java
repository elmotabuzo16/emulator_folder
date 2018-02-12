package com.vitalityactive.va.login;

public class LoginFragment extends LoginFragmentBase {

    @Override
    protected void processLoginClick() {
        getPresenter().onUserTriesToLogIn();
    }

    @Override
    protected void checkForFingerPrint() {

    }
}
