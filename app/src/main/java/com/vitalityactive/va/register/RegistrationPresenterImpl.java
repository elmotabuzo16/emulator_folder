package com.vitalityactive.va.register;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.register.interactor.RegistrationInteractor;
import com.vitalityactive.va.register.view.RegistrationUserInterface;

public class RegistrationPresenterImpl implements RegistrationPresenter {
    private final EventDispatcher eventDispatcher;
    private final RegistrationInteractor interactor;
    private RegistrationUserInterface userInterface;
    private EventListener<RegistrationInteractor.RegistrationSucceededEvent> registrationSucceededEventListener;
    private EventListener<RegistrationInteractor.RegistrationFailedEvent> registrationFailedEventListener;
    private final Scheduler scheduler;
    private boolean didShowRegistrationErrorMessage;

    public RegistrationPresenterImpl(EventDispatcher eventDispatcher, RegistrationInteractor interactor, final Scheduler scheduler) {
        this.eventDispatcher = eventDispatcher;
        this.interactor = interactor;

        registrationSucceededEventListener = new EventListener<RegistrationInteractor.RegistrationSucceededEvent>() {
            @Override
            public void onEvent(RegistrationInteractor.RegistrationSucceededEvent event) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        userInterface.navigateAfterSuccessfulRegistration();
                    }
                });
            }
        };
        registrationFailedEventListener = new EventListener<RegistrationInteractor.RegistrationFailedEvent>() {
            @Override
            public void onEvent(final RegistrationInteractor.RegistrationFailedEvent event) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        showRegistrationErrorMessage(event.getRegistrationRequestResult());
                        userInterface.hideLoadingIndicator();
                    }
                });
            }
        };
        this.scheduler = scheduler;
    }

    private void showRegistrationErrorMessage(RegistrationInteractor.RegistrationRequestResult requestResult) {
        didShowRegistrationErrorMessage = true;

        switch (requestResult) {
            case CONNECTION_ERROR:
                userInterface.showConnectionErrorMessage();
                break;
            case GENERIC_ERROR:
                userInterface.showGenericErrorMessage();
                break;
            case INVALID_EMAIL_INSURER_CODE_ERROR:
                userInterface.showInvalidEmailOrInsurerCodeErrorMessage();
                break;
            case ALREADY_REGISTERED_ERROR:
                userInterface.showAlreadyRegisteredErrorMessage();
                break;
        }
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {

    }

    private boolean shouldShowRegistrationRequestErrorMessageWhenUserInterfaceAppears() {
        return didLastRegistrationRequestFail() && !didShowRegistrationErrorMessage;
    }

    private boolean didLastRegistrationRequestFail() {
        return interactor.getRegistrationRequestResult() != RegistrationInteractor.RegistrationRequestResult.NONE;
    }

    @Override
    public void onUserInterfaceAppeared() {
        eventDispatcher.addEventListener(RegistrationInteractor.RegistrationSucceededEvent.class, registrationSucceededEventListener);
        eventDispatcher.addEventListener(RegistrationInteractor.RegistrationFailedEvent.class, registrationFailedEventListener);

        userInterface.hideLoadingIndicator();

        if (interactor.isRegistering()) {
            userInterface.showLoadingIndicator();
        } else if (shouldShowRegistrationRequestErrorMessageWhenUserInterfaceAppears()) {
            showRegistrationErrorMessage(interactor.getRegistrationRequestResult());
        }

        if (interactor.getRegistrationRequestResult() == RegistrationInteractor.RegistrationRequestResult.SUCCESSFUL) {
            userInterface.navigateAfterSuccessfulRegistration();
        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        eventDispatcher.removeEventListener(RegistrationInteractor.RegistrationSucceededEvent.class, registrationSucceededEventListener);
        eventDispatcher.removeEventListener(RegistrationInteractor.RegistrationFailedEvent.class, registrationFailedEventListener);
        scheduler.cancel();
    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void setUserInterface(RegistrationUserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void onUserTriesToRegister() {
        if (interactor.canRegister() && !interactor.isRegistering()) {
            didShowRegistrationErrorMessage = false;
            userInterface.showLoadingIndicator();
            interactor.register();
        }
    }

    @Override
    public void onUserDismissesErrorMessage() {
        userInterface.hideLoadingIndicator();
    }
}
