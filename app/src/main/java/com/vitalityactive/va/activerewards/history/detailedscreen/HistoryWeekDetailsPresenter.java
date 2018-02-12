package com.vitalityactive.va.activerewards.history.detailedscreen;

import com.vitalityactive.va.Presenter;

public class HistoryWeekDetailsPresenter implements Presenter<HistoryWeekDetailsPresenter.UserInterface> {
    private UserInterface userInterface;

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {

    }

    @Override
    public void onUserInterfaceAppeared() {

    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {

    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    interface UserInterface {
    }
}
