package com.vitalityactive.va.vna;

import com.vitalityactive.va.constants.QuestionnaireSet;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.questionnaire.QuestionnaireStateManager;
import com.vitalityactive.va.shared.questionnaire.service.BaseQuestionnaireSubmitter;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmissionServiceClient;

public class VNASubmitterImpl extends BaseQuestionnaireSubmitter {
    public VNASubmitterImpl(QuestionnaireStateManager questionnaireStateManager,
                            QuestionnaireSubmissionServiceClient questionnaireSubmissionServiceClient,
                            EventDispatcher eventDispatcher) {
        super(questionnaireStateManager, eventDispatcher, questionnaireSubmissionServiceClient);
    }

    @Override
    protected int getQuestionnaireSetType() {
        return QuestionnaireSet._VNA;
    }
}
