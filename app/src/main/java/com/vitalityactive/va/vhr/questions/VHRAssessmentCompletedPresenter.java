package com.vitalityactive.va.vhr.questions;

import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.shared.questionnaire.BaseAssessmentCompletedPresenter;

public class VHRAssessmentCompletedPresenter extends BaseAssessmentCompletedPresenter {
    public VHRAssessmentCompletedPresenter(QuestionnaireSetRepository questionnaireSetRepository) {
        super(questionnaireSetRepository);
    }
}
