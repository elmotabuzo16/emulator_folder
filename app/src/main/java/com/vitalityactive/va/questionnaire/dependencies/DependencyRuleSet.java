package com.vitalityactive.va.questionnaire.dependencies;

import java.util.ArrayList;

public class DependencyRuleSet {
    final ArrayList<DependencyRule> rules;
    final boolean allMustBeTrue;

    DependencyRuleSet(boolean allMustBeTrue) {
        this.allMustBeTrue = allMustBeTrue;
        rules = new ArrayList<>();
    }

    DependencyRuleSet(DependencyRule singleRule) {
        this(true);
        rules.add(singleRule);
    }

    static DependencyRuleSet and() {
        return new DependencyRuleSet(true);
    }

    static DependencyRuleSet or() {
        return new DependencyRuleSet(false);
    }
}
