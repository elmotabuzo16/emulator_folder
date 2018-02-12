package com.vitalityactive.va.profile;

import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;

public class ProfilePresenterImpl implements ProfilePresenter, EventListener<ProfileImageAvailableEvent> {

    private final ProfileInteractor interactor;
    private final EventDispatcher eventDispatcher;

    private UI userInterface;

    public ProfilePresenterImpl(ProfileInteractor interactor, EventDispatcher eventDispatcher) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
    }

    @Override
    public void onUserInterfaceAppeared() {
        eventDispatcher.addEventListener(ProfileImageAvailableEvent.class, this);
        showProfileInfo();
    }

    private void showProfileInfo() {
        userInterface.showProfileName(interactor.getUserGivenName(), interactor.getUserFamilyName());
        if (interactor.isProfileImageAvailable()) {
            userInterface.showProfileImage(interactor.getProfileImagePath());
        } else {
            interactor.fetchProfileImage();
            userInterface.showProfileInitials(interactor.getUserInitials());
        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        eventDispatcher.removeEventListener(ProfileImageAvailableEvent.class, this);
    }

    @Override
    public void onUserInterfaceDestroyed() {
    }

    @Override
    public void setUserInterface(UI userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void onEvent(ProfileImageAvailableEvent event) {
        if (interactor.isProfileImageAvailable()) {
            userInterface.showProfileImage(interactor.getProfileImagePath());
        }
    }
}
