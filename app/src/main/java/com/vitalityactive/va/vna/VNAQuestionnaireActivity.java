package com.vitalityactive.va.vna;

import com.vitalityactive.va.constants.QuestionnaireSet;
import com.vitalityactive.va.shared.questionnaire.activity.QuestionnaireBaseActivity;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;

import javax.inject.Inject;

public class VNAQuestionnaireActivity extends QuestionnaireBaseActivity {
    @Inject
    QuestionnaireCapturePresenter presenter;

    @Override
    protected void injectDependenciesUsingCaptureScopeInjector() {
        navigationCoordinator.getVNACaptureDependencyInjector().inject(this);
    }

    @Override
    protected QuestionnaireCapturePresenter getQuestionnaireCapturePresenter() {
        return presenter;
    }

    @Override
    protected int getQuestionnaireSetType() {
        return QuestionnaireSet._VNA;
    }

    @Override
    public void navigateAfterQuestionnaireCompleted() {
        navigationCoordinator.navigateAfterVNAQuestionnaireCompleted(this, questionnaireTypeKey);
    }
}
