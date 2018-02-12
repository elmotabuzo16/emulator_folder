package com.vitalityactive.va.mwb.questions;

import com.vitalityactive.va.constants.QuestionnaireSet;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.shared.questionnaire.AssessmentCompletedPresenter;
import com.vitalityactive.va.shared.questionnaire.activity.BaseQuestionnaireCompletedActivity;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by christian.j.p.capin on 1/30/2018.
 */

public class MWBQuestionnaireCompletedActivity extends BaseQuestionnaireCompletedActivity {
    @Inject
    @Named(DependencyNames.MWB)
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
        return QuestionnaireSet._MWB;
    }
}