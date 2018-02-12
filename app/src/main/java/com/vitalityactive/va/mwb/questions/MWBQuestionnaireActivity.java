package com.vitalityactive.va.mwb.questions;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.QuestionnaireSet;
import com.vitalityactive.va.shared.questionnaire.activity.QuestionnaireBaseActivity;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;

import javax.inject.Inject;

/**
 * Created by christian.j.p.capin on 1/30/2018.
 */

public class MWBQuestionnaireActivity extends QuestionnaireBaseActivity {
    @Inject
    QuestionnaireCapturePresenter presenter;

    @Override
    protected void injectDependenciesUsingCaptureScopeInjector() {
        navigationCoordinator.getMWBCaptureDependencyInjector().inject(this);
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
        return QuestionnaireSet._MWB;
    }

    @Override
    public void navigateAfterQuestionnaireCompleted() {
        navigationCoordinator.navigateAfterVHRQuestionnaireCompleted(this, questionnaireTypeKey);
    }
}
