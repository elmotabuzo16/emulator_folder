package com.vitalityactive.va.vna;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmissionRequestCompletedEvent;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmitter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenterImpl;

public class VNAPrivacyPolicyPresenterImpl extends TermsAndConditionsPresenterImpl<VNAPrivacyPolicyUserInterface> implements VNAPrivacyPolicyPresenter {
    private final QuestionnaireSubmitter questionnaireSubmitter;
    private long questionnaireTypeKey;
    private EventListener<QuestionnaireSubmissionRequestCompletedEvent> submissionRequestCompletedEventListener = new EventListener<QuestionnaireSubmissionRequestCompletedEvent>() {
        @Override
        public void onEvent(QuestionnaireSubmissionRequestCompletedEvent event) {
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

    private void showGenericSubmissionRequestErrorMessage() {
        userInterface.showGenericSubmissionRequestErrorMessage();
    }

    private void showConnectionSubmissionRequestErrorMessage() {
        userInterface.showConnectionSubmissionRequestErrorMessage();
    }

    private void showRequestErrorMessage() {
        didShowRequestErrorMessage = true;
        if (questionnaireSubmitter.getSubmissionRequestResult() == RequestResult.CONNECTION_ERROR) {
            showConnectionSubmissionRequestErrorMessage();
        } else if (questionnaireSubmitter.getSubmissionRequestResult() == RequestResult.GENERIC_ERROR) {
            showGenericSubmissionRequestErrorMessage();
        }
    }

    private boolean didSubmissionRequestFail() {
        return questionnaireSubmitter.getSubmissionRequestResult() != RequestResult.SUCCESSFUL &&
                questionnaireSubmitter.getSubmissionRequestResult() != RequestResult.NONE;
    }

    public VNAPrivacyPolicyPresenterImpl(Scheduler scheduler,
                                         TermsAndConditionsInteractor interactor,
                                         TermsAndConditionsConsenter termsAndConditionsConsenter,
                                         EventDispatcher eventDispatcher,
                                         QuestionnaireSubmitter questionnaireSubmitter) {
        super(scheduler, interactor, termsAndConditionsConsenter, eventDispatcher);
        this.questionnaireSubmitter = questionnaireSubmitter;
    }

    @Override
    public void onTermsAndConditionsAccepted() {
        configureUserInterfaceForRequestInProgress();
        questionnaireSubmitter.submit(questionnaireTypeKey);
    }

    @Override
    public void setQuestionnaireTypeKey(long questionnaireTypeKey) {
        this.questionnaireTypeKey = questionnaireTypeKey;
    }

    protected void configureUserInterfaceForRequestInProgress() {
        userInterface.disableAgreeButton();
        userInterface.showLoadingIndicator();
    }

    @Override
    protected void addEventListeners() {
        super.addEventListeners();
        eventDispatcher.addEventListener(QuestionnaireSubmissionRequestCompletedEvent.class, submissionRequestCompletedEventListener);
    }

    @Override
    protected void removeEventListeners() {
        super.removeEventListeners();
        eventDispatcher.removeEventListener(QuestionnaireSubmissionRequestCompletedEvent.class, submissionRequestCompletedEventListener);
    }

    @Override
    public void onUserTriesToSubmitAgain() {
        didShowRequestErrorMessage = false;
        configureUserInterfaceForRequestInProgress();
        questionnaireSubmitter.submit(questionnaireTypeKey);
    }
}
