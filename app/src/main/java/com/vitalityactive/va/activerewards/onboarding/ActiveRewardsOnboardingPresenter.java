package com.vitalityactive.va.activerewards.onboarding;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.activerewards.ActivationErrorType;
import com.vitalityactive.va.activerewards.ActiveRewardsActivator;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ActiveRewardsOnboardingPresenter extends BasePresenter<ActiveRewardsOnboardingPresenter.UserInterface> {
    private MainThreadScheduler scheduler;
    private final EventDispatcher eventDispatcher;
    private ActiveRewardsActivator activator;
    private RequestResult requestResult = RequestResult.NONE;
    private InsurerConfigurationRepository insurerConfigurationRepository;
    private final EventListener<ActiveRewardsActivator.ActivationSucceededEvent> activationSucceededEventListener = new EventListener<ActiveRewardsActivator.ActivationSucceededEvent>() {
        @Override
        public void onEvent(ActiveRewardsActivator.ActivationSucceededEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (isUserInterfaceVisible()) {
                        userInterface.navigateAway();
                    } else {
                        setRequestResult(RequestResult.SUCCESSFUL);
                    }
                }
            });
        }
    };
    private final EventListener<ActiveRewardsActivator.ActivationFailedEvent> activationFailedEventListener = new EventListener<ActiveRewardsActivator.ActivationFailedEvent>() {
        @Override
        public void onEvent(final ActiveRewardsActivator.ActivationFailedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (isUserInterfaceVisible()) {
                        userInterface.hideLoadingIndicator();
                        if (event.getActivationErrorType() == ActivationErrorType.GENERIC) {
                            userInterface.showGenericErrorMessage();
                        } else if (event.getActivationErrorType() == ActivationErrorType.CONNECTION) {
                            userInterface.showConnectionErrorMessage();
                        }
                    } else {
                        setRequestResult(getRequestResult(event.getActivationErrorType()));
                    }
                }

                private RequestResult getRequestResult(ActivationErrorType activationErrorType) {
                    switch (activationErrorType) {
                        case GENERIC:
                            return RequestResult.GENERIC_ERROR;
                        case CONNECTION:
                            return RequestResult.CONNECTION_ERROR;
                        default:
                            return RequestResult.NONE;
                    }
                }
            });
        }
    };

    @Inject
    ActiveRewardsOnboardingPresenter(EventDispatcher eventDispatcher, ActiveRewardsActivator activator, final MainThreadScheduler scheduler, InsurerConfigurationRepository insurerConfigurationRepository) {
        this.eventDispatcher = eventDispatcher;
        this.activator = activator;
        this.scheduler = scheduler;
        this.insurerConfigurationRepository = insurerConfigurationRepository;
    }

    private synchronized void setRequestResult(RequestResult requestResult) {
        this.requestResult = requestResult;
    }

    private synchronized RequestResult getRequestResult() {
        return requestResult;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        if (isNewNavigation) {
            setRequestResult(RequestResult.NONE);
            removeEventListeners();
            addEventListeners();
        }
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(ActiveRewardsActivator.ActivationSucceededEvent.class, activationSucceededEventListener);
        eventDispatcher.addEventListener(ActiveRewardsActivator.ActivationFailedEvent.class, activationFailedEventListener);
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(ActiveRewardsActivator.ActivationSucceededEvent.class, activationSucceededEventListener);
        eventDispatcher.removeEventListener(ActiveRewardsActivator.ActivationFailedEvent.class, activationFailedEventListener);
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        if (activator.isActivateRequestInProgress()) {
            userInterface.showLoadingIndicator();
        } else {
            userInterface.hideLoadingIndicator();
        }

        switch (getRequestResult()) {
            case SUCCESSFUL:
                userInterface.navigateAway();
                break;
            case GENERIC_ERROR:
                userInterface.showGenericErrorMessage();
                break;
            case CONNECTION_ERROR:
                userInterface.showConnectionErrorMessage();
                break;
        }

        setRequestResult(RequestResult.NONE);
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        if (isFinishing) {
            removeEventListeners();
        }
    }

    public void onGetStartedTapped() {
        if (insurerConfigurationRepository.shouldShowARMedicallyFitAgreement()) {
            userInterface.navigateAway();
        } else {
            userInterface.showLoadingIndicator();
            activator.activate();
        }
    }

    interface UserInterface {

        void navigateAway();

        void showGenericErrorMessage();

        void showConnectionErrorMessage();

        void showLoadingIndicator();
        void hideLoadingIndicator();

    }
}
