package com.vitalityactive.va.questionnaire.dependencies;

import android.annotation.SuppressLint;

import com.vitalityactive.va.questionnaire.QuestionnaireSection;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.QuestionBasicInputValueDto;
import com.vitalityactive.va.questionnaire.types.TestQuestionFactory;
import com.vitalityactive.va.questionnaire.types.YesNoQuestionDto;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class DependencyCheckerTest {
    private DependencyChecker dependencyChecker;
    private YesNoQuestionDto cutDownOnAlcohol;
    private YesNoQuestionDto drinkAlcohol;
    private QuestionBasicInputValueDto howMuchPerWeek;
    private QuestionnaireSection section;
    private QuestionnaireSection section2;
    private Question layoutTypes;

    @Before
    public void setUp() {
        buildQuestions();
        buildSections();
        dependencyChecker = new DependencyChecker()
                .addQuestion(cutDownOnAlcohol)
                .addQuestion(drinkAlcohol)
                .addQuestion(howMuchPerWeek)
                .addQuestion(layoutTypes)
                .addSections(Collections.singletonList(section))
                .addSections(Collections.singletonList(section2));
    }

    private void buildQuestions() {
        drinkAlcohol = (YesNoQuestionDto) TestQuestionFactory.drinkAlcohol();
        cutDownOnAlcohol = (YesNoQuestionDto) TestQuestionFactory.cutDownOnDrinkAlcohol();
        howMuchPerWeek = (QuestionBasicInputValueDto) TestQuestionFactory.cutDownOnDrinkAlcoholHowMuch();
        layoutTypes = TestQuestionFactory.layoutTypes();
    }

    @SuppressLint("DefaultLocale")
    private void buildSections() {
        section = TestQuestionFactory.basicSection(String.format("%d;>=;10;Number", howMuchPerWeek.getIdentifier()));
        section2 = TestQuestionFactory.basicSection(String.format("%d;>=;10;Number", 100000));
    }

    @Test
    public void set_canBeAnswered_on_dependent_questions() {
        Assert.assertFalse(cutDownOnAlcohol.getCanBeAnswered());

        drinkAlcohol.setValue(true);
        dependencyChecker.update();
        Assert.assertTrue(cutDownOnAlcohol.getCanBeAnswered());

        drinkAlcohol.setValue(false);
        dependencyChecker.update();
        Assert.assertFalse(cutDownOnAlcohol.getCanBeAnswered());
    }

    @Test
    public void no_dependencies_can_always_be_answered() {
        // false when loaded
        Assert.assertFalse(drinkAlcohol.getCanBeAnswered());

        dependencyChecker.update();
        Assert.assertTrue(drinkAlcohol.getCanBeAnswered());
    }

    @Test
    public void if_parent_not_answerable_any_more_then_children_also_not_answerable() {
        // grandparent answered and parent
        drinkAlcohol.setValue(true);
        cutDownOnAlcohol.setValue(true);

        // then visible
        dependencyChecker.update();
        Assert.assertTrue(howMuchPerWeek.getCanBeAnswered());

        // when grandparent value changed
        drinkAlcohol.setValue(false);
        dependencyChecker.update();

        // then parent hidden and self hidden
        Assert.assertFalse(cutDownOnAlcohol.getCanBeAnswered());
        Assert.assertFalse(howMuchPerWeek.getCanBeAnswered());
    }

    @Test
    public void set_sections_visibility() {
        drinkAlcohol.setValue(true);
        cutDownOnAlcohol.setValue(true);

        howMuchPerWeek.setValue("3");
        dependencyChecker.update();
        Assert.assertFalse(section.getCanBeAnswered());

        howMuchPerWeek.setValue("30");
        dependencyChecker.update();
        Assert.assertTrue(section.getCanBeAnswered());
    }

    @Test
    public void question_parent_not_visible_then_not_answerable() {
        // layoutTypes depends on a question id 10002, but it is not in this list
        // so it will not be answerable
        dependencyChecker.update();
        Assert.assertFalse(layoutTypes.getCanBeAnswered());
    }

    @Test
    public void section_parent_not_visible_then_do_not_change() {
        // section2 depends on a question id 100000, but it is not in this list
        // so it will not be updated
        section2.isVisible = false;
        dependencyChecker.update();
        Assert.assertFalse(section2.getCanBeAnswered());

        section2.isVisible = true;
        dependencyChecker.update();
        Assert.assertTrue(section2.getCanBeAnswered());
    }
}
