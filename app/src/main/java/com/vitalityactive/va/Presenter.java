package com.vitalityactive.va;

public interface Presenter<UI> {
    void onUserInterfaceCreated(boolean isNewNavigation);

    void onUserInterfaceAppeared();

    void onUserInterfaceDisappeared(boolean isFinishing);

    void onUserInterfaceDestroyed();

    void setUserInterface(UI userInterface);

    interface UserInterface {
        void showLoadingIndicator();
        void hideLoadingIndicator();
    }
}
