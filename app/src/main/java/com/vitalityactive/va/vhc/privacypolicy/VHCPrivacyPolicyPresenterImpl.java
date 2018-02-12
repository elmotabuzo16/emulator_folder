package com.vitalityactive.va.vhc.privacypolicy;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenterImpl;
import com.vitalityactive.va.vhc.VHCPrivacyPolicyPresenter;
import com.vitalityactive.va.vhc.submission.VHCSubmissionRequestCompletedEvent;
import com.vitalityactive.va.vhc.submission.VHCSubmitter;

public class VHCPrivacyPolicyPresenterImpl extends TermsAndConditionsPresenterImpl<VHCPrivacyPolicyUserInterface> implements VHCPrivacyPolicyPresenter {
    private VHCSubmitter vhcSubmitter;
    private EventListener<VHCSubmissionRequestCompletedEvent> submissionRequestCompletedEventListener = new EventListener<VHCSubmissionRequestCompletedEvent>() {
        @Override
        public void onEvent(VHCSubmissionRequestCompletedEvent event) {
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

    public VHCPrivacyPolicyPresenterImpl(Scheduler scheduler, TermsAndConditionsInteractor interactor, TermsAndConditionsConsenter consenter, EventDispatcher eventDispatcher, VHCSubmitter vhcSubmitter) {
        super(scheduler, interactor, consenter, eventDispatcher);
        this.vhcSubmitter = vhcSubmitter;
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
        vhcSubmitter.submit();
    }

    @Override
    protected boolean shouldNavigateAfterSuccessfulAgreeRequest() {
        return super.shouldNavigateAfterSuccessfulAgreeRequest() && vhcSubmitter.getSubmissionRequestResult() == RequestResult.SUCCESSFUL;
    }

    @Override
    protected boolean isRequestInProgress() {
        return super.isRequestInProgress() || vhcSubmitter.isSubmitting();
    }

    @Override
    protected void addEventListeners() {
        super.addEventListeners();
        eventDispatcher.addEventListener(VHCSubmissionRequestCompletedEvent.class, submissionRequestCompletedEventListener);
    }

    @Override
    protected void removeEventListeners() {
        super.removeEventListeners();
        eventDispatcher.removeEventListener(VHCSubmissionRequestCompletedEvent.class, submissionRequestCompletedEventListener);
    }

    private boolean didSubmissionRequestFail() {
        return vhcSubmitter.getSubmissionRequestResult() != RequestResult.SUCCESSFUL &&
                vhcSubmitter.getSubmissionRequestResult() != RequestResult.NONE;
    }

    private void showRequestErrorMessage() {
        didShowRequestErrorMessage = true;
        if (vhcSubmitter.getSubmissionRequestResult() == RequestResult.CONNECTION_ERROR) {
            showConnectionSubmissionRequestErrorMessage();
        } else if (vhcSubmitter.getSubmissionRequestResult() == RequestResult.GENERIC_ERROR) {
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
        vhcSubmitter.submit();
    }
}
