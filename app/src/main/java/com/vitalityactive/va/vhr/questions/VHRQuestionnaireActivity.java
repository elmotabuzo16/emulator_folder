package com.vitalityactive.va.vhr.questions;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.QuestionnaireSet;
import com.vitalityactive.va.shared.questionnaire.activity.QuestionnaireBaseActivity;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;

import javax.inject.Inject;

public class VHRQuestionnaireActivity extends QuestionnaireBaseActivity {
    @Inject
    QuestionnaireCapturePresenter presenter;

    @Override
    protected void injectDependenciesUsingCaptureScopeInjector() {
        navigationCoordinator.getVHRCaptureDependencyInjector().inject(this);
    }

    @Override
    protected QuestionnaireCapturePresenter getQuestionnaireCapturePresenter() {
        return presenter;
    }

    @Override
    protected int getGenericErrorTitle() {
        return R.string.privacy_policy_unable_to_complete_alert_title_115;
    }

    @Override
    protected int getGenericErrorMessage() {
        return R.string.vhr_unable_to_submit_error_alert_message;
    }

    @Override
    protected int getQuestionnaireSetType() {
        return QuestionnaireSet._VHR;
    }

    @Override
    public void navigateAfterQuestionnaireCompleted() {
        navigationCoordinator.navigateAfterVHRQuestionnaireCompleted(this, questionnaireTypeKey);
    }
}
