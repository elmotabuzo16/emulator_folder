package com.vitalityactive.va.forgotpassword;

import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.forgotpassword.service.ForgotPasswordSuccessEvent;
import com.vitalityactive.va.networking.RequestFailedEvent;

public class ForgotPasswordPresenterImpl implements ForgotPasswordPresenter {
    private final ForgotPasswordInteractor interactor;
    private Ui userInterface;
    private boolean isUserInterfacePresent;
    private boolean isRequestActive = false;

    public ForgotPasswordPresenterImpl(ForgotPasswordInteractor interactor, EventDispatcher eventDispatcher) {
        this.interactor = interactor;
        buildEventListeners(eventDispatcher);
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
    }

    @Override
    public void onUserInterfaceAppeared() {
        isUserInterfacePresent = true;
        if (isRequestActive) {
            userInterface.onRequestBusy();
        } else {
            userInterface.onRequestCompleted();
        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        isUserInterfacePresent = false;
    }

    @Override
    public void onUserInterfaceDestroyed() {
    }

    @Override
    public void onForgotPasswordTapped(String username) {
        isRequestActive = true;
        userInterface.onRequestBusy();
        interactor.forgotPassword(username);
    }

    @Override
    public void setUserInterface(Ui userInterface) {
        this.userInterface = userInterface;
    }

    private void buildEventListeners(EventDispatcher eventDispatcher) {
        eventDispatcher.addEventListener(ForgotPasswordSuccessEvent.class, new EventListener<ForgotPasswordSuccessEvent>() {
            @Override
            public void onEvent(ForgotPasswordSuccessEvent event) {
                onSuccessfulForgotPasswordEvent();
            }
        });
        eventDispatcher.addEventListener(RequestFailedEvent.class, new EventListener<RequestFailedEvent>() {
            @Override
            public void onEvent(RequestFailedEvent event) {
                onFailedForgotPasswordEvent(event);
            }
        });
    }

    private void onFailedForgotPasswordEvent(RequestFailedEvent event) {
        isRequestActive = false;
        if (isUserInterfacePresent) {
            if (event.getType() == RequestFailedEvent.Type.INVALID_USERNAME) {
                userInterface.onEmailNotRegistered();
            } else {
                userInterface.onForgotPasswordRequestFailed();
            }
            userInterface.onRequestCompleted();
        }
    }

    private void onSuccessfulForgotPasswordEvent() {
        isRequestActive = false;
        if (isUserInterfacePresent) {
            userInterface.onForgotPasswordRequestSuccessful();
            userInterface.onRequestCompleted();
        }
    }
}
