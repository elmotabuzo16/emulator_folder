package com.vitalityactive.va.vhr.questions;

import com.vitalityactive.va.constants.QuestionnaireSet;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.shared.questionnaire.AssessmentCompletedPresenter;
import com.vitalityactive.va.shared.questionnaire.activity.BaseQuestionnaireCompletedActivity;

import javax.inject.Inject;
import javax.inject.Named;

public class VHRQuestionnaireCompletedActivity extends BaseQuestionnaireCompletedActivity {
    @Inject
    @Named(DependencyNames.VHR)
    AssessmentCompletedPresenter presenter;

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        getDependencyInjector().inject(this);
    }

    @Override
    protected AssessmentCompletedPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected int getQuestionnaireSetType() {
        return QuestionnaireSet._VHR;
    }
}
