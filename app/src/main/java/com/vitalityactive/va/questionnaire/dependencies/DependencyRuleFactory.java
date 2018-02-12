package com.vitalityactive.va.questionnaire.dependencies;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vitalityactive.va.utilities.TextUtilities;

public class DependencyRuleFactory {
    @Nullable
    public static DependencyRuleSet buildSet(String visibilityTag) {
        if (visibilityTag == null) {
            return null;
        } else if (visibilityTag.indexOf("&&") > 0) {
            String[] splits = visibilityTag.split("&&");
            return buildAndDependencyRuleSet(splits);
        } else if (visibilityTag.indexOf("||") > 0) {
            String[] splits = visibilityTag.split("\\|\\|");
            return buildOrDependencyRuleSet(splits);
        } else {
            return buildSingleRuleSet(build(visibilityTag));
        }
    }

    @Nullable
    public static DependencyRuleSet buildAndDependencyRuleSet(String[] visibilityTags) {
        return buildDependencyRuleSet(DependencyRuleSet.and(), visibilityTags);
    }

    @Nullable
    public static DependencyRuleSet buildOrDependencyRuleSet(String[] visibilityTags) {
        return buildDependencyRuleSet(DependencyRuleSet.or(), visibilityTags);
    }

    @Nullable
    private static DependencyRuleSet buildDependencyRuleSet(DependencyRuleSet ruleSet, String[] visibilityTags) {
        for (String visibilityTag : visibilityTags) {
            DependencyRule rule = build(visibilityTag);
            if (rule == null) {
                return null;
            }
            ruleSet.rules.add(rule);
        }
        return ruleSet;
    }

    @Nullable
    public static DependencyRuleSet buildSingleRuleSet(DependencyRule rule) {
        if (rule == null)
            return null;
        return new DependencyRuleSet(rule);
    }

    @NonNull
    public static DependencyRuleSet buildSingleRuleSet(long parentQuestionId, String operator, String value, String type) {
        return buildSingleRuleSet(build(parentQuestionId, operator, value, type));
    }

    @Nullable
    public static DependencyRule build(String visibilityTag) {
        if (TextUtilities.isNullOrWhitespace(visibilityTag)) {
            return null;
        }

        String[] split = visibilityTag.split(";");
        if (split.length != 4) {
            return null;
        }
        try {
            return build(Long.parseLong(split[0]), split[1], split[2], split[3]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static DependencyRule build(long parentQuestionId, String operator, String value, String type) {
        switch (type) {
            case "Number":
                return new NumberDependencyRule(parentQuestionId, getOperator(operator), Long.parseLong(value));
            case "Boolean":
                return buildBooleanDependencyRule(parentQuestionId, Boolean.parseBoolean(value));
            case "Date":
                return new DateDependencyRule(parentQuestionId, getOperator(operator), value);
            default:
                return new StringDependencyRule(parentQuestionId, getOperator(operator), value);
        }
    }

    @NonNull
    public static DependencyRule buildBooleanDependencyRule(long parentQuestionId, boolean value) {
        return new BooleanDependencyRule(parentQuestionId, value);
    }

    @NonNull
    private static Operator getOperator(String operator) {
        return Operator.fromString(operator);
    }
}
