package com.vitalityactive.va.questionnaire;

import com.vitalityactive.va.questionnaire.dependencies.DependencyRuleFactory;
import com.vitalityactive.va.questionnaire.dependencies.DependencyRuleSet;

import java.util.ArrayList;
import java.util.List;

public class QuestionnaireSection implements VisibilityDependsOnQuestionAnswer {
    public final long typeKey;
    public final String title;
    public final String visibilityTagName;
    public List<Long> questionTypeKeys;
    public boolean isVisible;

    public QuestionnaireSection(long typeKey, String title, String visibilityTagName, boolean isVisible) {
        this.typeKey = typeKey;
        this.title = title;
        this.visibilityTagName = visibilityTagName;
        questionTypeKeys = new ArrayList<>();
        this.isVisible = isVisible;
    }

    @Override
    public boolean getCanBeAnswered() {
        return isVisible;
    }

    @Override
    public void setCanBeAnswered(boolean canBeAnswered) {
        isVisible = canBeAnswered;
    }

    @Override
    public DependencyRuleSet getDependencyRules() {
        return DependencyRuleFactory.buildSet(visibilityTagName);
    }
}
