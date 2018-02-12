package com.vitalityactive.va.termsandconditions;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;

public class TermsAndConditionsPresenterImpl<UserInterface extends TermsAndConditionsPresenter.UserInterface>
        extends BasePresenter<UserInterface>
        implements TermsAndConditionsInteractor.Callback, TermsAndConditionsPresenter<UserInterface> {

    protected final Scheduler scheduler;
    protected TermsAndConditionsInteractor interactor;
    protected TermsAndConditionsConsenter termsAndConditionsConsenter;
    protected EventDispatcher eventDispatcher;
    protected boolean didShowRequestErrorMessage;
    @NonNull
    private RequestResult agreeRequestResult = RequestResult.NONE;
    @NonNull
    private RequestResult disagreeRequestResult = RequestResult.NONE;
    @NonNull
    private RequestResult contentRequestResult = RequestResult.NONE;
    private EventListener<TermsAndConditionsAgreeRequestCompletedEvent> agreeRequestCompletedEventListener = new EventListener<TermsAndConditionsAgreeRequestCompletedEvent>() {
        @Override
        public void onEvent(final TermsAndConditionsAgreeRequestCompletedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (event.getRequestResult() == RequestResult.SUCCESSFUL) {
                        onTermsAndConditionsAccepted();
                    } else if (isUserInterfaceVisible()) {
                        resetUserInterfaceAfterRequestFinished();
                    }
                    if (isUserInterfaceVisible()) {
                        switch (event.getRequestResult()) {
                            case GENERIC_ERROR:
                                didShowRequestErrorMessage = true;
                                userInterface.showGenericAgreeRequestErrorMessage();
                                break;
                            case CONNECTION_ERROR:
                                didShowRequestErrorMessage = true;
                                userInterface.showConnectionAgreeRequestErrorMessage();
                                break;
                        }
                    } else {
                        agreeRequestResult = event.getRequestResult();
                    }
                }
            });
        }
    };
    private EventListener<TermsAndConditionsDisagreeRequestCompletedEvent> disagreeRequestCompletedEventListener = new EventListener<TermsAndConditionsDisagreeRequestCompletedEvent>() {
        @Override
        public void onEvent(final TermsAndConditionsDisagreeRequestCompletedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (event.getRequestResult() == RequestResult.SUCCESSFUL) {
                        onTermsAndConditionsDeclined();
                    } else if (isUserInterfaceVisible()) {
                        resetUserInterfaceAfterRequestFinished();
                    }
                    if (isUserInterfaceVisible()) {
                        switch (event.getRequestResult()) {
                            case GENERIC_ERROR:
                                didShowRequestErrorMessage = true;
                                userInterface.showGenericDisagreeRequestErrorMessage();
                                break;
                            case CONNECTION_ERROR:
                                didShowRequestErrorMessage = true;
                                userInterface.showConnectionDisagreeRequestErrorMessage();
                                break;
                        }
                    } else {
                        disagreeRequestResult = event.getRequestResult();
                    }
                }
            });
        }
    };

    public TermsAndConditionsPresenterImpl(final Scheduler scheduler,
                                           TermsAndConditionsInteractor interactor,
                                           TermsAndConditionsConsenter termsAndConditionsConsenter,
                                           EventDispatcher eventDispatcher) {
        this.scheduler = scheduler;
        this.interactor = interactor;
        this.termsAndConditionsConsenter = termsAndConditionsConsenter;
        this.eventDispatcher = eventDispatcher;
        interactor.setCallback(this);
    }

    protected void resetUserInterfaceAfterRequestFinished() {
        userInterface.hideLoadingIndicator();
        userInterface.enableAgreeButton();
    }

    protected void onTermsAndConditionsAccepted() {
        navigateAfterTermsAndConditionsAccepted();
    }

    private void onTermsAndConditionsDeclined() {
        if (isUserInterfaceVisible()) {
            userInterface.navigateAfterTermsAndConditionsDeclined();
        }
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        if (isNewNavigation) {
            eventDispatcher.removeEventListener(TermsAndConditionsAgreeRequestCompletedEvent.class, agreeRequestCompletedEventListener);
            eventDispatcher.removeEventListener(TermsAndConditionsDisagreeRequestCompletedEvent.class, disagreeRequestCompletedEventListener);
            eventDispatcher.addEventListener(TermsAndConditionsAgreeRequestCompletedEvent.class, agreeRequestCompletedEventListener);
            eventDispatcher.addEventListener(TermsAndConditionsDisagreeRequestCompletedEvent.class, disagreeRequestCompletedEventListener);
            resetRequestResults();
            fetchTermsAndConditions();
        }
    }

    @Override
    @CallSuper
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        if (shouldNavigateAfterSuccessfulAgreeRequest()) {
            userInterface.navigateAfterTermsAndConditionsAccepted();
            return;
        }

        if (shouldNavigateAfterSuccessfulDisagreeRequest()) {
            userInterface.navigateAfterTermsAndConditionsDeclined();
            return;
        }

        addEventListeners();

        if (isRequestInProgress()) {
            configureUserInterfaceForRequestInProgress();
            return;
        }

        userInterface.hideLoadingIndicator();

        if (agreeRequestResult != RequestResult.SUCCESSFUL && agreeRequestResult != RequestResult.NONE) {
            didShowRequestErrorMessage = true;
            switch (agreeRequestResult) {
                case GENERIC_ERROR:
                    userInterface.showGenericAgreeRequestErrorMessage();
                    userInterface.disableAgreeButton();
                    break;
                case CONNECTION_ERROR:
                    userInterface.showConnectionAgreeRequestErrorMessage();
                    userInterface.disableAgreeButton();
                    break;
            }
            resetRequestResults();
        } else if (disagreeRequestResult != RequestResult.SUCCESSFUL && disagreeRequestResult != RequestResult.NONE) {
            switch (disagreeRequestResult) {
                case GENERIC_ERROR:
                    userInterface.showGenericDisagreeRequestErrorMessage();
                    userInterface.disableAgreeButton();
                    break;
                case CONNECTION_ERROR:
                    userInterface.showConnectionDisagreeRequestErrorMessage();
                    userInterface.disableAgreeButton();
                    break;
            }
            resetRequestResults();
        } else if (contentRequestResult != RequestResult.SUCCESSFUL && contentRequestResult != RequestResult.NONE) {
            switch (contentRequestResult) {
                case GENERIC_ERROR:
                    userInterface.showGenericContentRequestErrorMessage();
                    userInterface.disableAgreeButton();
                    break;
                case CONNECTION_ERROR:
                    userInterface.showConnectionContentRequestErrorMessage();
                    userInterface.disableAgreeButton();
                    break;
            }
            resetRequestResults();
        } else {
            userInterface.showTermsAndConditions(interactor.getTermsAndConditions());
        }
    }

    @CallSuper
    protected void resetRequestResults() {
        agreeRequestResult = RequestResult.NONE;
        disagreeRequestResult = RequestResult.NONE;
        contentRequestResult = RequestResult.NONE;
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        removeEventListeners();
        if (isFinishing) {
            eventDispatcher.removeEventListener(TermsAndConditionsAgreeRequestCompletedEvent.class, agreeRequestCompletedEventListener);
            eventDispatcher.removeEventListener(TermsAndConditionsDisagreeRequestCompletedEvent.class, disagreeRequestCompletedEventListener);
        }
    }

    private boolean shouldNavigateAfterSuccessfulDisagreeRequest() {
        return disagreeRequestResult == RequestResult.SUCCESSFUL;
    }

    protected boolean shouldNavigateAfterSuccessfulAgreeRequest() {
        return agreeRequestResult == RequestResult.SUCCESSFUL;
    }

    @CallSuper
    protected boolean isRequestInProgress() {
        return interactor.isFetchingTermsAndConditions() || termsAndConditionsConsenter.isRequestInProgress();
    }

    @CallSuper
    protected void addEventListeners() {

    }

    protected void configureUserInterfaceForRequestInProgress() {
        userInterface.disableAgreeButton();
        userInterface.showLoadingIndicator();
    }

    @CallSuper
    protected void removeEventListeners() {

    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void onUserAgreesToTermsAndConditions() {
        configureUserInterfaceForRequestInProgress();
        didShowRequestErrorMessage = false;
        termsAndConditionsConsenter.agreeToTermsAndConditions();
    }

    @Override
    public void onBackPressed() {
        userInterface.showDisagreeAlert();
    }

    @Override
    public void onUserDisagreesToTermsAndConditions() {
        configureUserInterfaceForRequestInProgress();
        if (termsAndConditionsConsenter != null) {
            didShowRequestErrorMessage = false;
            termsAndConditionsConsenter.disagreeToTermsAndConditions();
        }
    }

    @Override
    public void fetchTermsAndConditions() {
        didShowRequestErrorMessage = false;
        userInterface.showLoadingIndicator();
        interactor.fetchTermsAndConditions();
    }

    public void setInteractor(TermsAndConditionsInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void onTermsAndConditionsRetrieved(final @NonNull String termsAndConditions) {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                if (isUserInterfaceVisible()) {
                    userInterface.hideLoadingIndicator();
                    userInterface.enableAgreeButton();
                    userInterface.showTermsAndConditions(termsAndConditions);
                }
            }
        });
    }

    @Override
    public void onGenericContentError() {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                if (isUserInterfaceVisible()) {
                    showGenericContentRequestErrorMessage();
                } else {
                    contentRequestResult = RequestResult.GENERIC_ERROR;
                }
            }
        });
    }

    private void showGenericContentRequestErrorMessage() {
        didShowRequestErrorMessage = true;
        userInterface.hideLoadingIndicator();
        userInterface.showGenericContentRequestErrorMessage();
    }

    @Override
    public void onConnectionContentError() {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                if (isUserInterfaceVisible()) {
                    showConnectionContentRequestErrorMessage();
                } else {
                    contentRequestResult = RequestResult.CONNECTION_ERROR;
                }
            }
        });
    }

    private void showConnectionContentRequestErrorMessage() {
        didShowRequestErrorMessage = true;
        userInterface.hideLoadingIndicator();
        userInterface.showConnectionContentRequestErrorMessage();
    }

    protected void navigateAfterTermsAndConditionsAccepted() {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                if (isUserInterfaceVisible()) {
                    userInterface.navigateAfterTermsAndConditionsAccepted();
                }
            }
        });
    }

}
