package com.vitalityactive.va.snv.confirmandsubmit.presenter;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.snv.confirmandsubmit.service.SNVSubmissionRequestCompletedEvent;
import com.vitalityactive.va.snv.confirmandsubmit.service.SNVSubmitter;
import com.vitalityactive.va.snv.confirmandsubmit.view.SNVPrivacyPolicyUserInterface;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenterImpl;

public class SNVPrivacyPolicyPresenterImpl extends TermsAndConditionsPresenterImpl<SNVPrivacyPolicyUserInterface> implements SNVPrivacyPolicyPresenter {
    private SNVSubmitter snvSubmitter;
    private EventListener<SNVSubmissionRequestCompletedEvent> submissionRequestCompletedEventListener = new EventListener<SNVSubmissionRequestCompletedEvent>() {
        @Override
        public void onEvent(SNVSubmissionRequestCompletedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (didSubmissionRequestFail()) {
                        resetUserInterfaceAfterRequestFinished();
                        showRequestErrorMessage();
                    } else {
                        userInterface.navigateAfterTermsAndConditionsAccepted();
                    }
                }
            });
        }
    };

    public SNVPrivacyPolicyPresenterImpl(Scheduler scheduler, TermsAndConditionsInteractor interactor, TermsAndConditionsConsenter consenter, EventDispatcher eventDispatcher, SNVSubmitter vhcSubmitter) {
        super(scheduler, interactor, consenter, eventDispatcher);
        this.snvSubmitter = vhcSubmitter;
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        if (didSubmissionRequestFail() && !didShowRequestErrorMessage) {
            showRequestErrorMessage();
        }
    }

    @Override
    protected void onTermsAndConditionsAccepted() {
        snvSubmitter.submit();
    }

    @Override
    protected boolean shouldNavigateAfterSuccessfulAgreeRequest() {
        return super.shouldNavigateAfterSuccessfulAgreeRequest() && snvSubmitter.getSubmissionRequestResult() == RequestResult.SUCCESSFUL;
    }

    @Override
    protected boolean isRequestInProgress() {
        return super.isRequestInProgress() || snvSubmitter.isSubmitting();
    }

    @Override
    protected void addEventListeners() {
        super.addEventListeners();
        eventDispatcher.addEventListener(SNVSubmissionRequestCompletedEvent.class, submissionRequestCompletedEventListener);
    }

    @Override
    protected void removeEventListeners() {
        super.removeEventListeners();
        eventDispatcher.removeEventListener(SNVSubmissionRequestCompletedEvent.class, submissionRequestCompletedEventListener);
    }

    private boolean didSubmissionRequestFail() {
        return snvSubmitter.getSubmissionRequestResult() != RequestResult.SUCCESSFUL &&
                snvSubmitter.getSubmissionRequestResult() != RequestResult.NONE;
    }

    private void showRequestErrorMessage() {
        didShowRequestErrorMessage = true;
        if (snvSubmitter.getSubmissionRequestResult() == RequestResult.CONNECTION_ERROR) {
            showConnectionSubmissionRequestErrorMessage();
        } else if (snvSubmitter.getSubmissionRequestResult() == RequestResult.GENERIC_ERROR) {
            showGenericSubmissionRequestErrorMessage();
        }
    }

    private void showGenericSubmissionRequestErrorMessage() {
        userInterface.showGenericSubmissionRequestErrorMessage();
    }

    private void showConnectionSubmissionRequestErrorMessage() {
        userInterface.showConnectionSubmissionRequestErrorMessage();
    }

    @Override
    public void onUserTriesToSubmitAgain() {
        didShowRequestErrorMessage = false;
        configureUserInterfaceForRequestInProgress();
        snvSubmitter.submit();
    }
}
