package com.vitalityactive.va.mwb.questions;

import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.shared.questionnaire.BaseAssessmentCompletedPresenter;

/**
 * Created by christian.j.p.capin on 1/30/2018.
 */

public class MWBAssessmentCompletedPresenter extends BaseAssessmentCompletedPresenter {
    public MWBAssessmentCompletedPresenter(QuestionnaireSetRepository questionnaireSetRepository) {
        super(questionnaireSetRepository);
    }
}