package com.vitalityactive.va.questionnaire;

import com.vitalityactive.va.questionnaire.dependencies.DependencyRuleSet;

public interface VisibilityDependsOnQuestionAnswer {
    boolean getCanBeAnswered();

    void setCanBeAnswered(boolean canBeAnswered);

    DependencyRuleSet getDependencyRules();
}
