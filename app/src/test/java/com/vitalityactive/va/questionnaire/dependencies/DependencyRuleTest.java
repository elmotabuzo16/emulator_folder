package com.vitalityactive.va.questionnaire.dependencies;

import android.support.annotation.NonNull;

import com.vitalityactive.va.BaseTest;

import org.junit.Assert;
import org.junit.Test;

public class DependencyRuleTest extends BaseTest {
    private static final String THE_DATE = "2017-03-12";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initialiseAndroidThreeTen();
    }

    @Test
    public void boolean_true() {
        DependencyRule rule = new BooleanDependencyRule(1L, true);

        Assert.assertTrue(rule.isMet("true"));
        Assert.assertFalse(rule.isMet("false"));
        Assert.assertFalse(rule.isMet("apple"));
    }

    @Test
    public void boolean_false() {
        DependencyRule rule = new BooleanDependencyRule(1L, false);

        Assert.assertFalse(rule.isMet("true"));
        Assert.assertTrue(rule.isMet("false"));
        Assert.assertTrue(rule.isMet("apple"));
    }

    @Test
    public void boolean_not_equal_to() {
        DependencyRule rule = new BooleanDependencyRule(1, Operator.NOT_EQUAL_TO, false);

        Assert.assertFalse(rule.isMet("false"));
        Assert.assertTrue(rule.isMet("true"));
        Assert.assertFalse(rule.isMet("apple"));
    }

    @Test
    public void number_equals() {
        DependencyRule rule = new NumberDependencyRule(1, Operator.EQUALS, 10.0f);

        Assert.assertFalse(rule.isMet("11"));
        Assert.assertTrue(rule.isMet("10"));
        Assert.assertFalse(rule.isMet("9"));
        Assert.assertFalse(rule.isMet("10.4"));
        Assert.assertFalse(rule.isMet("9.999999"));
    }

    @Test
    public void number_greater_than() {
        DependencyRule rule = new NumberDependencyRule(1, Operator.GREATER_THAN, 8.12f);

        Assert.assertTrue(rule.isMet("11"));
        Assert.assertTrue(rule.isMet("8.120001"));
        Assert.assertFalse(rule.isMet("8.12"));
        Assert.assertFalse(rule.isMet("7"));
        Assert.assertFalse(rule.isMet("8.119"));
    }

    @Test
    public void number_greater_than_or_equals() {
        DependencyRule rule = new NumberDependencyRule(1, Operator.GREATER_THAN_OR_EQUALS, 6.45f);

        Assert.assertTrue(rule.isMet("7"));
        Assert.assertTrue(rule.isMet("6.45"));
        Assert.assertFalse(rule.isMet("5"));
    }

    @Test
    public void number_less_than() {
        DependencyRule rule = new NumberDependencyRule(1, Operator.LESS_THAN, 56.67f);

        Assert.assertFalse(rule.isMet("57"));
        Assert.assertFalse(rule.isMet("56.67"));
        Assert.assertTrue(rule.isMet("9"));
    }

    @Test
    public void number_less_than_or_equals() {
        DependencyRule rule = new NumberDependencyRule(1, Operator.LESS_THAN_OR_EQUALS, 23.123f);

        Assert.assertFalse(rule.isMet("24"));
        Assert.assertTrue(rule.isMet("23.123"));
        Assert.assertTrue(rule.isMet("22"));
    }

    @Test
    public void number_not_equal_to() {
        DependencyRule rule = new NumberDependencyRule(1, Operator.NOT_EQUAL_TO, 10);

        Assert.assertFalse(rule.isMet("10"));
        Assert.assertTrue(rule.isMet("245"));
        Assert.assertTrue(rule.isMet("1"));
        Assert.assertTrue(rule.isMet("10.000001"));
        Assert.assertTrue(rule.isMet("9.999999"));
    }

    @Test
    public void string_equals() {
        DependencyRule rule = new StringDependencyRule(1, "Female");

        Assert.assertFalse(rule.isMet(""));
        Assert.assertFalse(rule.isMet(null));
        Assert.assertFalse(rule.isMet("Male"));
        Assert.assertFalse(rule.isMet("FEMALE"));
        Assert.assertTrue(rule.isMet("Female"));
    }

    @Test
    public void string_not_equal_to() {
        DependencyRule rule = new StringDependencyRule(1, Operator.NOT_EQUAL_TO, "Dog");

        Assert.assertTrue(rule.isMet(""));
        Assert.assertTrue(rule.isMet(null));
        Assert.assertTrue(rule.isMet("DOG"));
        Assert.assertTrue(rule.isMet("dog"));
        Assert.assertFalse(rule.isMet("Dog"));

    }

    @Test
    public void date_equals() {
        DependencyRule rule = getDateDependencyRule(Operator.EQUALS);

        Assert.assertTrue(rule.isMet(THE_DATE));
        Assert.assertFalse(rule.isMet("2018-03-12"));
        Assert.assertFalse(rule.isMet("2017-04-12"));
        Assert.assertFalse(rule.isMet("2017-03-13"));
        Assert.assertFalse(rule.isMet(null));
        Assert.assertFalse(rule.isMet(""));
    }

    @Test
    public void date_not_equal_to() {
        String theDate = org.threeten.bp.LocalDate.now().toString();
        DependencyRule rule = new DateDependencyRule(1, Operator.NOT_EQUAL_TO, theDate);

        Assert.assertTrue(rule.isMet("9999-04-12"));
        Assert.assertFalse(rule.isMet(theDate));
        Assert.assertTrue(rule.isMet(null));
        Assert.assertTrue(rule.isMet(""));
    }

    @NonNull
    private DependencyRule getDateDependencyRule(Operator operator) {
        return new DateDependencyRule(1, operator, THE_DATE);
    }

    @Test
    public void date_greater_than() {
        DependencyRule rule = getDateDependencyRule(Operator.GREATER_THAN);

        Assert.assertTrue(rule.isMet("1900-01-01"));
        Assert.assertFalse(rule.isMet(THE_DATE));
        Assert.assertFalse(rule.isMet("2017-03-13"));
    }

    @Test
    public void date_greater_than_or_equals() {
        DependencyRule rule = getDateDependencyRule(Operator.GREATER_THAN_OR_EQUALS);

        Assert.assertTrue(rule.isMet("1900-01-01"));
        Assert.assertTrue(rule.isMet(THE_DATE));
        Assert.assertFalse(rule.isMet("9999-12-31"));
    }

    @Test
    public void date_less_than() {
        DependencyRule rule = getDateDependencyRule(Operator.LESS_THAN);

        Assert.assertFalse(rule.isMet("1900-01-01"));
        Assert.assertFalse(rule.isMet(THE_DATE));
        Assert.assertTrue(rule.isMet("2017-03-13"));
    }

    @Test
    public void date_less_than_or_equals() {
        DependencyRule rule = getDateDependencyRule(Operator.LESS_THAN_OR_EQUALS);

        Assert.assertFalse(rule.isMet("1900-01-01"));
        Assert.assertTrue(rule.isMet(THE_DATE));
        Assert.assertTrue(rule.isMet("2017-03-13"));
    }
}
