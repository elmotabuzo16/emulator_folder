package com.vitalityactive.va.questionnaire.dependencies;

import com.vitalityactive.va.utilities.date.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class DependencyRuleFactoryTest {
    @Test
    public void create_number_rules() {
        DependencyRule rule = DependencyRuleFactory.build("111;>;5;Number");

        Assert.assertNotNull(rule);
        Assert.assertEquals(NumberDependencyRule.class.getName(), rule.getClass().getName());
        Assert.assertEquals(111, rule.getParentQuestionId());
        Assert.assertEquals(Operator.GREATER_THAN, rule.getOperator());

        NumberDependencyRule numberRule = (NumberDependencyRule) rule;
        Assert.assertEquals(5, numberRule.getValue(), 0.001f);
    }

    @Test
    public void create_boolean_rules() {
        DependencyRule rule = DependencyRuleFactory.build("7;==;True;Boolean");

        Assert.assertNotNull(rule);
        Assert.assertEquals(BooleanDependencyRule.class.getName(), rule.getClass().getName());
        Assert.assertEquals(7, rule.getParentQuestionId());
        Assert.assertEquals(Operator.EQUALS, rule.getOperator());

        BooleanDependencyRule boolRule = (BooleanDependencyRule) rule;
        Assert.assertEquals(true, boolRule.getValue());
    }

    @Test
    public void create_string_rules() {
        DependencyRule rule = DependencyRuleFactory.build("4;==;Female;Name");

        Assert.assertNotNull(rule);
        Assert.assertEquals(StringDependencyRule.class.getName(), rule.getClass().getName());
        Assert.assertEquals(4, rule.getParentQuestionId());
        Assert.assertEquals(Operator.EQUALS, rule.getOperator());

        StringDependencyRule stringRule = (StringDependencyRule) rule;
        Assert.assertEquals("Female", stringRule.getValue());

        rule = DependencyRuleFactory.build("4;!=;Female;Name");
        Assert.assertNotNull(rule);
        Assert.assertEquals(Operator.NOT_EQUAL_TO, rule.getOperator());
    }

    @Test
    public void create_date_rules() {
        DependencyRule rule = DependencyRuleFactory.build("73;==;2017-03-12;Date");

        Assert.assertNotNull(rule);
        Assert.assertEquals(DateDependencyRule.class.getName(), rule.getClass().getName());
        Assert.assertEquals(73, rule.getParentQuestionId());
        Assert.assertEquals(Operator.EQUALS, rule.getOperator());

        DateDependencyRule dateRule = (DateDependencyRule)rule;
        Assert.assertEquals(new LocalDate("2017-03-12"), dateRule.getValue());

        rule = DependencyRuleFactory.build("23;>=;2017-03-12;Date");
        Assert.assertNotNull(rule);
        Assert.assertEquals(Operator.GREATER_THAN_OR_EQUALS, rule.getOperator());
    }

    @Test
    public void build_set_of_one_rule() {
        DependencyRuleSet ruleSet = DependencyRuleFactory.buildSet("111;>;5;Number");

        Assert.assertNotNull(ruleSet);
        Assert.assertEquals(1, ruleSet.rules.size());
    }

    @Test
    public void build_set_of_rules_for_list_of_rules() {
        String tag = "111;>;45;Value&&111;<;80;Value";
        DependencyRuleSet ruleSet = DependencyRuleFactory.buildSet(tag);

        Assert.assertNotNull(ruleSet);
        Assert.assertEquals(2, ruleSet.rules.size());

        Assert.assertEquals(111, ruleSet.rules.get(0).getParentQuestionId());
        Assert.assertEquals(Operator.GREATER_THAN, ruleSet.rules.get(0).getOperator());

        Assert.assertEquals(111, ruleSet.rules.get(1).getParentQuestionId());
        Assert.assertEquals(Operator.LESS_THAN, ruleSet.rules.get(1).getOperator());
    }

    @Test
    public void build_and_set_of_rules() {
        String tag = "111;>;45;Value&&111;<;80;Value";
        DependencyRuleSet ruleSet = DependencyRuleFactory.buildSet(tag);

        Assert.assertNotNull(ruleSet);
        Assert.assertTrue(ruleSet.allMustBeTrue);
    }

    @Test
    public void build_or_set_of_rules() {
        String tag = "111;>;45;Value||111;<;80;Value";
        DependencyRuleSet ruleSet = DependencyRuleFactory.buildSet(tag);

        Assert.assertNotNull(ruleSet);
        Assert.assertFalse(ruleSet.allMustBeTrue);
    }

    @Test
    public void invalid_visibility_tag_name_results_in_null_dependency_rule() {
        assertNull(DependencyRuleFactory.build(""));
        assertNull(DependencyRuleFactory.build(null));
        assertNull(DependencyRuleFactory.build("asdf;123"));
        assertNull(DependencyRuleFactory.build("asdf"));
        assertNull(DependencyRuleFactory.build("123"));
        assertNull(DependencyRuleFactory.build("123;asdf"));
        assertNull(DependencyRuleFactory.build("123;operator;value;valueType;tooMany"));
    }

    @Test
    public void invalid_visibility_tag_name_results_in_null_dependency_rule_set() {
        assertNull(DependencyRuleFactory.buildSet(""));
        assertNull(DependencyRuleFactory.buildSet(null));
        assertNull(DependencyRuleFactory.buildSet("111;>;45;Value||111;<;80;Value;tooMany"));
        assertNull(DependencyRuleFactory.buildSet("111;>;45;Value||111;<;80;Value&&111;<;80;Value"));
        assertNull(DependencyRuleFactory.buildSet("123;operator;value;valueType;tooMany"));
    }
}
