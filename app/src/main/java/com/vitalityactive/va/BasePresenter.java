package com.vitalityactive.va;

import android.support.annotation.CallSuper;

public class BasePresenter<UserInterface> implements Presenter<UserInterface> {
    protected UserInterface userInterface;
    private boolean isUserInterfaceVisible;

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {

    }

    @CallSuper
    @Override
    public void onUserInterfaceAppeared() {
        isUserInterfaceVisible = true;
    }

    @CallSuper
    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        isUserInterfaceVisible = false;
    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    protected final boolean isUserInterfaceVisible() {
        return isUserInterfaceVisible;
    }
}
