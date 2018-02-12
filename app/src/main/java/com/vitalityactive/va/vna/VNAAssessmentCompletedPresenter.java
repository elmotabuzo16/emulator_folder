package com.vitalityactive.va.vna;

import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.shared.questionnaire.BaseAssessmentCompletedPresenter;

public class VNAAssessmentCompletedPresenter extends BaseAssessmentCompletedPresenter {
    public VNAAssessmentCompletedPresenter(QuestionnaireSetRepository questionnaireSetRepository) {
        super(questionnaireSetRepository);
    }
}
