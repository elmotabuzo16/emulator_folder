package com.vitalityactive.va.vhr.questions;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.questionnaire.QuestionnaireStateManager;
import com.vitalityactive.va.shared.questionnaire.capture.BaseQuestionnaireCapturePresenter;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmitter;

public class VHRQuestionnairePresenterImpl extends BaseQuestionnaireCapturePresenter {
    public VHRQuestionnairePresenterImpl(QuestionnaireStateManager questionnaireStateManager,
                                         InsurerConfigurationRepository insurerConfigurationRepository,
                                         QuestionnaireSubmitter questionnaireSubmitter,
                                         EventDispatcher eventDispatcher,
                                         Scheduler scheduler)
    {
        super(questionnaireStateManager, insurerConfigurationRepository, questionnaireSubmitter, eventDispatcher, scheduler);
    }

    @Override
    protected boolean shouldShowPrivacyPolicy() {
        return insurerConfigurationRepository.shouldShowVHRPrivacyPolicy();
    }
}
