package com.vitalityactive.va.activerewards.termsandconditions;

import android.support.annotation.NonNull;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.activerewards.ActivationErrorType;
import com.vitalityactive.va.activerewards.ActiveRewardsActivator;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenterImpl;

import javax.inject.Named;

public class ActiveRewardsTermsAndConditionsPresenterImpl extends TermsAndConditionsPresenterImpl<ActiveRewardsTermsAndConditionsUserInterface> {
    private ActiveRewardsActivator activeRewardsActivator;
    @NonNull
    private RequestResult activationRequestResult = RequestResult.NONE;
    private EventListener<ActiveRewardsActivator.ActivationSucceededEvent> activationSucceededEventListener = new EventListener<ActiveRewardsActivator.ActivationSucceededEvent>() {
        @Override
        public void onEvent(ActiveRewardsActivator.ActivationSucceededEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (isUserInterfaceVisible()) {
                        navigateAfterTermsAndConditionsAccepted();
                    } else {
                        activationRequestResult = RequestResult.SUCCESSFUL;
                    }
                }
            });
        }
    };
    private EventListener<ActiveRewardsActivator.ActivationFailedEvent> activationFailedEventListener = new EventListener<ActiveRewardsActivator.ActivationFailedEvent>() {
        @Override
        public void onEvent(final ActiveRewardsActivator.ActivationFailedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (isUserInterfaceVisible()) {
                        resetUserInterfaceAfterRequestFinished();
                        if (event.getActivationErrorType() == ActivationErrorType.GENERIC) {
                            userInterface.showGenericActivationErrorMessage();
                        } else {
                            userInterface.showConnectionActivationErrorMessage();
                        }
                    } else {
                        activationRequestResult = getActivationRequestResult(event.getActivationErrorType());
                    }
                }

                private RequestResult getActivationRequestResult(ActivationErrorType activationErrorType) {
                    switch (activationErrorType) {
                        case GENERIC:
                            return RequestResult.GENERIC_ERROR;
                        case CONNECTION:
                            return RequestResult.CONNECTION_ERROR;
                    }
                    return RequestResult.NONE;
                }
            });
        }
    };

    public ActiveRewardsTermsAndConditionsPresenterImpl(final Scheduler scheduler, ActiveRewardsActivator activeRewardsActivator, @Named(DependencyNames.ACTIVE_REWARDS_TERMS_AND_CONDITIONS) TermsAndConditionsInteractor interactor, EventDispatcher eventDispatcher, TermsAndConditionsConsenter consenter) {
        super(scheduler, interactor, consenter, eventDispatcher);
        this.activeRewardsActivator = activeRewardsActivator;
        eventDispatcher.addEventListener(ActiveRewardsActivator.ActivationSucceededEvent.class, activationSucceededEventListener);
        eventDispatcher.addEventListener(ActiveRewardsActivator.ActivationFailedEvent.class, activationFailedEventListener);
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();

        if (activationRequestResult == RequestResult.GENERIC_ERROR) {
            userInterface.showGenericActivationErrorMessage();
            resetRequestResults();
        } else if (activationRequestResult == RequestResult.CONNECTION_ERROR) {
            userInterface.showConnectionActivationErrorMessage();
            resetRequestResults();
        }
    }

    @Override
    public void onTermsAndConditionsAccepted() {
        activeRewardsActivator.activate();
    }

    @Override
    public void onBackPressed() {
        configureUserInterfaceForRequestInProgress();
        termsAndConditionsConsenter.disagreeToTermsAndConditions();
    }

    @Override
    protected boolean shouldNavigateAfterSuccessfulAgreeRequest() {
        return activationRequestResult == RequestResult.SUCCESSFUL;
    }

    @Override
    protected boolean isRequestInProgress() {
        return super.isRequestInProgress() || activeRewardsActivator.isActivateRequestInProgress();
    }

    @Override
    protected void resetRequestResults() {
        super.resetRequestResults();
        activationRequestResult = RequestResult.NONE;
    }
}
