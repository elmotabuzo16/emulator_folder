package com.vitalityactive.va.vna;

import com.vitalityactive.va.constants.QuestionnaireSet;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.shared.questionnaire.activity.BaseQuestionnaireCompletedActivity;
import com.vitalityactive.va.shared.questionnaire.AssessmentCompletedPresenter;

import javax.inject.Inject;
import javax.inject.Named;

public class VNAQuestionnaireCompletedActivity extends BaseQuestionnaireCompletedActivity {
    @Inject
    @Named(DependencyNames.VNA)
    AssessmentCompletedPresenter presenter;

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected AssessmentCompletedPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected int getQuestionnaireSetType() {
        return QuestionnaireSet._VNA;
    }
}
