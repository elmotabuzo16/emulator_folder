package com.vitalityactive.va.questionnaire.dependencies;

import android.support.annotation.NonNull;

import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.questionnaire.QuestionnaireSection;
import com.vitalityactive.va.questionnaire.VisibilityDependsOnQuestionAnswer;
import com.vitalityactive.va.questionnaire.types.Question;

import java.util.ArrayList;
import java.util.List;

public class DependencyChecker {
    private final List<Question> questions;
    private final List<VisibilityDependsOnQuestionAnswer> items;

    public DependencyChecker() {
        questions = new ArrayList<>();
        items = new ArrayList<>();
    }

    public DependencyChecker addSections(List<QuestionnaireSection> sections) {
        this.items.addAll(sections);
        return this;
    }

    public DependencyChecker addQuestions(List<Question> questions) {
        this.questions.addAll(questions);
        this.items.addAll(questions);
        return this;
    }

    public DependencyChecker addQuestion(Question question) {
        questions.add(question);
        items.add(question);
        return this;
    }

    public void addReferenceQuestions(List<Question> previousQuestions) {
        this.questions.addAll(previousQuestions);
    }

    public void update() {
        for (int i = 0; i < items.size(); i++) {
            VisibilityDependsOnQuestionAnswer question = items.get(i);
            updateCanBeAnswered(question);
        }
    }

    private void updateCanBeAnswered(VisibilityDependsOnQuestionAnswer item) {
        DependencyRuleSet ruleSet = item.getDependencyRules();
        if (ruleSet == null || ruleSet.rules.size() == 0) {
            item.setCanBeAnswered(true);
            return;
        }

        boolean canBeAnswered = ruleSet.allMustBeTrue ? checkAllRulesPass(item, ruleSet) : checkAnyRulePass(item, ruleSet);
        item.setCanBeAnswered(canBeAnswered);
    }

    private boolean checkAllRulesPass(VisibilityDependsOnQuestionAnswer item, DependencyRuleSet ruleSet) {
        boolean canBeAnswered = true;
        for (DependencyRule rule : ruleSet.rules) {
            canBeAnswered = canBeAnswered && calculateCanBeAnswered(item, rule);
        }
        return canBeAnswered;
    }

    private boolean checkAnyRulePass(VisibilityDependsOnQuestionAnswer item, DependencyRuleSet ruleSet) {
        boolean canBeAnswered = false;
        for (DependencyRule rule : ruleSet.rules) {
            canBeAnswered = canBeAnswered || calculateCanBeAnswered(item, rule);
        }
        return canBeAnswered;
    }

    @NonNull
    private Boolean calculateCanBeAnswered(VisibilityDependsOnQuestionAnswer item, DependencyRule dependencyRule) {
        if (dependencyRule == null) {
            return true;
        }

        long parentQuestionId = dependencyRule.getParentQuestionId();
        Question parentQuestion = getQuestionById(parentQuestionId);
        if (parentQuestion == null && item instanceof Question) {
            // todo: parent question does not exist, should this rather fail?
            return false;
        } else if (parentQuestion == null) {
            // parent question is not visible in this section, so it was already set
            return item.getCanBeAnswered();
        }

        updateCanBeAnswered(parentQuestion);
        if (!parentQuestion.getCanBeAnswered()) {
            // parent cannot be answered, so ignore its value and hide question
            return false;
        }

        Answer parentQuestionAnswer = parentQuestion.getAnswer();
        return parentQuestionAnswer.hasAnswer() && dependencyRule.isMet(parentQuestionAnswer.getValue());
    }

    private Question getQuestionById(long questionId) {
        for (Question question : questions) {
            if (questionId == question.getIdentifier())
                return question;
        }
        return null;
    }
}
