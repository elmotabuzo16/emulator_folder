package com.vitalityactive.va.questionnaire.dependencies;

import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.QuestionBasicInputValueDto;
import com.vitalityactive.va.questionnaire.types.TestQuestionFactory;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class DependencyCheckerWithMultipleRulesTest {
    private Question and;
    private Question or;
    private QuestionBasicInputValueDto question;
    private QuestionBasicInputValueDto question1;
    private QuestionBasicInputValueDto question2;
    private DependencyChecker dependencyChecker;

    @Before
    public void setUp() {
        buildQuestions();
        dependencyChecker = new DependencyChecker()
                .addQuestion(question)
                .addQuestion(question1)
                .addQuestion(question2)
                .addQuestion(and)
                .addQuestion(or);
    }

    private void buildQuestions() {
        question = (QuestionBasicInputValueDto) TestQuestionFactory.fullTextWithNoDependencies(10003);
        question.setValue("100");

        question1 = (QuestionBasicInputValueDto) TestQuestionFactory.fullText2(1);
        question2 = (QuestionBasicInputValueDto) TestQuestionFactory.fullText2(2);
        and = TestQuestionFactory.questionWith2Rules(0, true, "10010;==;1;Number", "10011;==;2;Number");
        or = TestQuestionFactory.questionWith2Rules(1, false, "10010;==;1;Number", "10011;==;2;Number");
    }

    @Test
    public void and_all_must_be_valid() {
        setBothValid();
        dependencyChecker.update();
        Assert.assertTrue(and.getCanBeAnswered());

        set1Invalid2Valid();
        dependencyChecker.update();
        Assert.assertFalse(and.getCanBeAnswered());

        set1Valid2Invalid();
        dependencyChecker.update();
        Assert.assertFalse(and.getCanBeAnswered());

        setBothInvalid();
        dependencyChecker.update();
        Assert.assertFalse(and.getCanBeAnswered());
    }

    @Test
    public void or_all_must_be_valid() {
        setBothValid();
        dependencyChecker.update();
        Assert.assertTrue(or.getCanBeAnswered());

        set1Invalid2Valid();
        dependencyChecker.update();
        Assert.assertTrue(or.getCanBeAnswered());

        set1Valid2Invalid();
        dependencyChecker.update();
        Assert.assertTrue(or.getCanBeAnswered());

        setBothInvalid();
        dependencyChecker.update();
        Assert.assertFalse(or.getCanBeAnswered());
    }

    private void set1Invalid2Valid() {
        setQuestion1Invalid();
        setQuestion2Valid();
    }

    private void set1Valid2Invalid() {
        setQuestion1Valid();
        setQuestion2Invalid();
    }

    private void setBothInvalid() {
        setQuestion1Invalid();
        setQuestion2Invalid();
    }

    private void setBothValid() {
        setQuestion1Valid();
        setQuestion2Valid();
    }

    private void setQuestion1Invalid() {
        question1.setValue("0");
    }

    private void setQuestion1Valid() {
        question1.setValue("1");
    }

    private void setQuestion2Invalid() {
        question2.setValue("0");
    }

    private void setQuestion2Valid() {
        question2.setValue("2");
    }
}
