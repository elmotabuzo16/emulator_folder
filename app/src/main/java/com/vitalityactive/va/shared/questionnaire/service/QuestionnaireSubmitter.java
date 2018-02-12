package com.vitalityactive.va.shared.questionnaire.service;

import com.vitalityactive.va.networking.RequestResult;

public interface QuestionnaireSubmitter {
    void submit(long questionnaireTypeKey);

    RequestResult getSubmissionRequestResult();
}
