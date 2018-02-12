package com.vitalityactive.va.shared.questionnaire.service;

import android.support.annotation.CallSuper;

import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.questionnaire.QuestionnaireStateManager;
import com.vitalityactive.va.questionnaire.types.Question;

import java.util.List;

public abstract class BaseQuestionnaireSubmitter implements QuestionnaireSubmitter, WebServiceResponseParser<QuestionnaireSubmitResponse> {
    private final QuestionnaireStateManager questionnaireStateManager;
    private final QuestionnaireSubmissionServiceClient questionnaireSubmissionServiceClient;
    private final EventDispatcher eventDispatcher;
    private RequestResult submissionRequestResult;

    public BaseQuestionnaireSubmitter(QuestionnaireStateManager questionnaireStateManager,
                                      EventDispatcher eventDispatcher,
                                      QuestionnaireSubmissionServiceClient questionnaireSubmissionServiceClient) {
        this.questionnaireStateManager = questionnaireStateManager;
        this.eventDispatcher = eventDispatcher;
        this.questionnaireSubmissionServiceClient = questionnaireSubmissionServiceClient;
    }

    @Override
    public synchronized RequestResult getSubmissionRequestResult() {
        return submissionRequestResult;
    }

    private synchronized void setSubmissionRequestResult(RequestResult submissionRequestResult) {
        this.submissionRequestResult = submissionRequestResult;
    }

    private void onRequestCompleted(RequestResult requestResult) {
        setSubmissionRequestResult(requestResult);
        eventDispatcher.dispatchEvent(new QuestionnaireSubmissionRequestCompletedEvent());
    }

    @Override
    public void submit(long questionnaireTypeKey) {
        List<Question> allValidQuestionsInCurrentSection = questionnaireStateManager.getAllValidAnsweredQuestionsForQuestionnaire(questionnaireTypeKey);
        questionnaireSubmissionServiceClient.submitQuestionnaire(getQuestionnaireSetType(),
                allValidQuestionsInCurrentSection, questionnaireTypeKey, this);
    }

    protected abstract int getQuestionnaireSetType();

    @Override
    @CallSuper
    public void parseResponse(QuestionnaireSubmitResponse questionnaireSubmitResponse) {
        questionnaireStateManager.clearQuestionnaireProgress();
        onRequestCompleted(RequestResult.SUCCESSFUL);
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        onRequestCompleted(RequestResult.GENERIC_ERROR);
    }

    @Override
    public void handleGenericError(Exception exception) {
        onRequestCompleted(RequestResult.GENERIC_ERROR);
    }

    @Override
    public void handleConnectionError() {
        onRequestCompleted(RequestResult.CONNECTION_ERROR);
    }
}
