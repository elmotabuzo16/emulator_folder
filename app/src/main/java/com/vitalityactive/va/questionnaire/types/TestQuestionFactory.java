package com.vitalityactive.va.questionnaire.types;

import android.support.annotation.NonNull;
import android.text.InputType;

import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.constants.QuestionDecorator;
import com.vitalityactive.va.constants.QuestionType;
import com.vitalityactive.va.questionnaire.QuestionnaireSection;
import com.vitalityactive.va.questionnaire.dependencies.DependencyRuleFactory;
import com.vitalityactive.va.questionnaire.dependencies.DependencyRuleSet;
import com.vitalityactive.va.shared.questionnaire.repository.model.QuestionDecoratorModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.QuestionModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.UnitOfMeasureModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.ValidValueModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class TestQuestionFactory {
    public static Question careForBackPain(int id) {
        return new YesNoQuestionDto(id,
                0,
                0, null,
                "Are you currently under medical care for back pain?",
                "An allergy is an abnormal immune reaction against a normally harmless substance. Learn More",
                15,
                String.valueOf(true), String.valueOf(false), "Yes", "No");
    }

    public static Question regularPrescriptionDrugs(int id) {
        return new YesNoQuestionDto(id, 0, 0, null, "Has your physician recommended regular prescription drug use for allergies?", null, 14, String.valueOf(true), String.valueOf(false), "Yes", "No");
    }

    public static YesNoQuestionDto doYouKnowWaist(int id) {
        return new YesNoQuestionDto(id, 0, 0, "Do you know your waist measurement?", null, null, 13, String.valueOf(true), String.valueOf(false), "Yes", "No");
    }

    @NonNull
    public static QuestionBasicInputValueDto servingsOfVegetablesPerDay(int id) {
        final QuestionBasicInputValueDto questionBasicInputValueDto = QuestionBasicInputValueDto.numericInput(id,
                0, "How many servings of vegetables do you eat?",
                "One serving size is equal to 100g of raw or cooked vegetables (e.g. spinach, artichoke, beans, cabbage)",
                null,
                "Servings",
                12);

        questionBasicInputValueDto.addUnit(UnitsOfMeasure.UNKNOWN, "per day", "per day");
        return questionBasicInputValueDto;
    }

    @NonNull
    public static QuestionBasicInputValueDto fruitPerDay(int id) {
        final QuestionBasicInputValueDto questionBasicInputValueDto = QuestionBasicInputValueDto.numericInput(id, 0, "How many servings of fruit do you eat?",
                "One serving size is equal to blah the bleh bloh",
                "Fruit loops does not count",
                "Fruit",
                11);
        questionBasicInputValueDto.addUnit(UnitsOfMeasure.UNKNOWN, "Per hour", "per hour");

        return questionBasicInputValueDto;
    }

    public static QuestionFreeTextDto whatIsYourName(int id) {
        return new QuestionFreeTextDto(id, 0, "Name", "", "", "", 1, 5);
    }

    public static SingleSelectOptionQuestionDto higherFatCookingMethods(int id) {
        SingleSelectOptionQuestionDto questionDto =
                new SingleSelectOptionQuestionDto(id,
                        0,
                        0, "How often do you prepare meals using higher fat cooking methods such as deep frying, pan frying /stir frying with excessive oil.",
                        null,
                        null,
                        10);
        questionDto.addItem("Never", null);
        questionDto.addItem("Rarely", "I usually prepare meals using other methods");
        questionDto.addItem("Occasionally", "I split my cooking between higher-fat and lower-fat methods");
        questionDto.addItem("Usually", "More often than not I prepare meals using these methods)");
        questionDto.addItem("Frequently", null);
        return questionDto;
    }

    public static SingleSelectOptionQuestionDto feelingsAboutDiet(int id) {
        SingleSelectOptionQuestionDto questionDto =
                new SingleSelectOptionQuestionDto(id,
                        0,
                        0, "Choose the option that best describes how you feel about your diet",
                        null,
                        null,
                        9);
        questionDto.addItem("I know my diet needs improvement but I don’t really want to change it", null);
        questionDto.addItem("I am happy with my diet", null);
        questionDto.addItem("I want to change my diet", null)
                .note = "Consider completing Vitality Health Check to get started on a healthier, fitter lifestyle.";

        return questionDto;
    }

    public static MultiOptionOptionQuestionDto medicalConditions(int id) {
        MultiOptionOptionQuestionDto questionDto =
                new MultiOptionOptionQuestionDto(id,
                        0,
                        0, "Select the medical conditions that your family has been diagnosed or prescribed medication for by a doctor.",
                        "Family is biological mother, father, brother or sister.",
                        null,
                        8);
        questionDto.addItem("Asthma", null);
        questionDto.addItem("Cancer", null);
        questionDto.addItem("1", "Chronic obstructive pulmonary disease")
                .note = "Consider completing VHC";
        return questionDto;
    }

    public static LabelWithAssociationsQuestionDto smokingQuestions(int id) {
        LabelWithAssociationsQuestionDto questionDto =
                LabelWithAssociationsQuestionDto.numericInput(id, 0, "How often do you smoke?",
                        "Smoking is bad for your health",
                        "null",
                        "Smoking",
                        5);
        return questionDto;
    }

    public static LabelQuestionDto headerLabel(int id) {
        return new LabelQuestionDto(id,
                0,
                0, "Section name",
                "Select the medical conditions that your family has been diagnosed or prescribed medication for by a doctor.",
                "Family is biological mother, father, brother or sister.",
                7);
    }

    public static QuestionBasicInputValueDto waistMeasurement(int id) {
        QuestionBasicInputValueDto questionDto = new QuestionBasicInputValueDto(id,
                0,
                0, null,
                null,
                "Waist measurement is a horizontal measure at the narrowest part of the torso above the bellybutton and below the lowest portion of the chest bone. Learn More",
                "Waist",
                InputType.TYPE_CLASS_NUMBER,
                6);
        questionDto.addUnit(UnitsOfMeasure.CENTIMETER, "cm", "centimeter");
        questionDto.addUnit(UnitsOfMeasure.INCH, "in", "inch");
        return questionDto;
    }

    public static SingleCheckboxQuestionDto allergies() {
        return new SingleCheckboxQuestionDto(11, 0, 0, "Allergies", null, null, 5);
    }

    public static Question doYouUseTobaccoProducts() {
        SingleSelectOptionQuestionDto questions = new SingleSelectOptionQuestionDto(12,
                0,
                0, "Do you use tobacco products",
                "etc cigarettes, cigars",
                null,
                4);
        questions.addItem("No, never", null);
        questions.addItem("No, have in the past", null);
        questions.addItem("Yes", null);
        return questions;
    }

    public static Question drinkAlcohol() {
        return new YesNoQuestionDto(13, 0, 0, "Do you drink Alcohol", null, null, 3, "True", "False", "Yes", "No");
    }

    public static Question cutDownOnDrinkAlcohol() {
        return new YesNoQuestionDto(14, 0, 0, "Have you ever felt you should cut down on your drinking", null, null, 4, String.valueOf(true), String.valueOf(false), "Yes", "No")
                .setDependencyRules(DependencyRuleFactory.buildSingleRuleSet(
                        DependencyRuleFactory.buildBooleanDependencyRule(13, true))
                );
    }

    private static Question smokingDates(int id) {
        return new QuestionDateInputDto(id,
                0,
                0, "When did you stop smoking?",
                "Date",
                null,
                1);
    }

    public static List<Question> basicList(int idsBaseValue) {
        List<Question> questions = new ArrayList<>();

        questions.add(headerLabel(idsBaseValue + 9));
        questions.add(servingsOfVegetablesPerDay(idsBaseValue + 4));
        questions.add(doYouKnowWaist(idsBaseValue + 3));
        questions.add(waistMeasurement(idsBaseValue + 10));
        questions.add(higherFatCookingMethods(idsBaseValue + 6));
        questions.add(feelingsAboutDiet(idsBaseValue + 7));
        questions.add(fruitPerDay(idsBaseValue + 5));
        questions.add(medicalConditions(idsBaseValue + 8));
        questions.add(regularPrescriptionDrugs(idsBaseValue + 2));
        questions.add(careForBackPain(idsBaseValue + 1));
        questions.add(smokingDates(idsBaseValue + 15));

        return questions;
    }

    public static Question labelQuestion(QuestionFactory questionFactory) {
        final QuestionModel dummyQuestionModel = createDummyQuestionModel(501,
                1,
                "Section name",
                com.vitalityactive.va.constants.QuestionType._LABEL,
                QuestionDecorator._LABEL,
                0);
        return questionFactory.createQuestion(dummyQuestionModel);
    }

    public static Question numberRangeTextBoxQuestion(QuestionFactory questionFactory) {
        final QuestionModel dummyQuestionModel = createDummyQuestionModel(502,
                1,
                "How many servings of vegetables do you eat?",
                QuestionType._DECIMALRANGE,
                QuestionDecorator._TEXTBOX,
                0);

        dummyQuestionModel.unitOfMeasures = new RealmList<>();

        UnitOfMeasureModel unitOfMeasure = new UnitOfMeasureModel();
        unitOfMeasure.value = "100115";
        dummyQuestionModel.unitOfMeasures.add(unitOfMeasure);

        unitOfMeasure = new UnitOfMeasureModel();
        unitOfMeasure.value = "100109";
        dummyQuestionModel.unitOfMeasures.add(unitOfMeasure);

        return questionFactory.createQuestion(dummyQuestionModel);
    }

    public static Question dependantNumberRangeQuestion(QuestionFactory questionFactory) {
        final QuestionModel dummyQuestionModel = createDummyQuestionModel(504,
                4,
                null,
                QuestionType._NUMBERRANGE,
                QuestionDecorator._TEXTBOX,
                0);

        final Question question = questionFactory.createQuestion(dummyQuestionModel);
        if (question != null) {
            question.setDependencyRules(DependencyRuleFactory.buildSingleRuleSet(
                    DependencyRuleFactory.buildBooleanDependencyRule(3, true))
            );
        }
        return question;
    }

    private static QuestionModel createDummyQuestionModel(long typeKey,
                                                          int sortOrderIndex,
                                                          String questionText,
                                                          long questionTypeKey,
                                                          long decoratorTypeKey,
                                                          Integer questionLength) {
        final QuestionModel model = new QuestionModel();

        model.questionDecorator = new QuestionDecoratorModel();

        model.typeKey = typeKey;
        model.sortOrderIndex = sortOrderIndex;
        model.questionText = questionText;
        model.questionTypeKey = questionTypeKey;
        model.questionDecorator.typeKey = decoratorTypeKey;
        model.length = questionLength;

        return model;
    }

    private static QuestionModel createDummyQuestionModel(long typeKey,
                                                          int sortOrderIndex,
                                                          String questionText,
                                                          long questionTypeKey,
                                                          long decoratorTypeKey,
                                                          RealmList<ValidValueModel> validValues) {
        QuestionModel model = createDummyQuestionModel(typeKey, sortOrderIndex, questionText, questionTypeKey, decoratorTypeKey, 0);

        model.validValues = validValues;

        return model;
    }

    public static Question multiSelectCheckboxQuestion(QuestionFactory questionFactory) {
        RealmList<ValidValueModel> validValues = new RealmList<>();

        ValidValueModel validValue = new ValidValueModel();
        validValue.value = "Poor";
        validValues.add(validValue);

        validValue = new ValidValueModel();
        validValue.value = "Fair";
        validValues.add(validValue);

        validValue = new ValidValueModel();
        validValue.value = "Good";
        validValues.add(validValue);

        final QuestionModel dummyQuestionModel = createDummyQuestionModel(507,
                5,
                "Select the medical conditions that your family has been diagnosed or prescribed medication for by a doctor.",
                QuestionType._MULTISELECT,
                QuestionDecorator._CHECKBOX,
                validValues);

        return questionFactory.createQuestion(dummyQuestionModel);
    }

    public static Question singleCheckBoxQuestion(QuestionFactory questionFactory) {
        final QuestionModel dummyQuestionModel = createDummyQuestionModel(506,
                10,
                "Do you want to check this checkbox?",
                QuestionType._SINGLESELECT,
                QuestionDecorator._CHECKBOX,
                0);

        return questionFactory.createQuestion(dummyQuestionModel);
    }

    public static Question datePickerQuestion(QuestionFactory questionFactory) {
        final QuestionModel dummyQuestionModel = createDummyQuestionModel(510,
                10,
                "When did you stop smoking?",
                QuestionType._DATE,
                QuestionDecorator._DATEPICKER,
                0);

        return questionFactory.createQuestion(dummyQuestionModel);
    }

    public static Question singleSelectOptionQuestion(QuestionFactory questionFactory) {
        RealmList<ValidValueModel> validValues = new RealmList<>();

        ValidValueModel validValue = new ValidValueModel();
        validValue.value = "Never";
        validValue.name = "Never";
        validValues.add(validValue);

        validValue = new ValidValueModel();
        validValue.value = "Rarely";
        validValue.name = "Rarely";
        validValues.add(validValue);

        validValue = new ValidValueModel();
        validValue.value = "Occasionally";
        validValue.name = "Occasionally";
        validValues.add(validValue);

        validValue = new ValidValueModel();
        validValue.value = "Usually";
        validValue.name = "Usually";
        validValues.add(validValue);

        validValue = new ValidValueModel();
        validValue.value = "Frequently";
        validValue.name = "Frequently";
        validValues.add(validValue);

        final QuestionModel dummyQuestionModel = createDummyQuestionModel(505,
                5,
                "How often do you prepare meals using higher fat cooking methods such as deep frying, pan frying /stir frying with excessive oil.",
                QuestionType._SINGLESELECT,
                QuestionDecorator._RADIOBUTTON,
                validValues);

        return questionFactory.createQuestion(dummyQuestionModel);
    }

    public static Question singleSelectYesNoQuestion(QuestionFactory questionFactory) {
        final QuestionModel dummyQuestionModel = createDummyQuestionModel(503,
                3,
                "Do you know your waist measurement?",
                QuestionType._SINGLESELECT,
                QuestionDecorator._YESNO,
                0);

        return questionFactory.createQuestion(dummyQuestionModel);
    }

    public static Question freeTextTextBoxQuestion(QuestionFactory questionFactory) {
        final QuestionModel dummyQuestionModel = createDummyQuestionModel(511,
                1,
                "Does this question need text?",
                QuestionType._FREETEXT,
                QuestionDecorator._TEXTBOX,
                5);

        return questionFactory.createQuestion(dummyQuestionModel);
    }

    public static Question fullYesNo() {
        return new YesNoQuestionDto(10001, 0, 0, "Do you want to answer some questions?", "Questions will not be visible if you say no", "Say yes", 0, String.valueOf(true), String.valueOf(false), "Yes", "No");
    }

    public static Question fullTypeOfUi() {
        SingleSelectOptionQuestionDto question = new SingleSelectOptionQuestionDto(10002,
                0,
                0, "what type of question?",
                "select the type",
                "select the type of question",
                1);
        question.addItem("Values", "Text");
        question.addItem("Layout tests", "Show options for different combinations of layout items that are visible");
        question.setDependencyRules(DependencyRuleFactory.buildSingleRuleSet(10001, "==", "True", "Boolean"));
        return question;
    }

    public static Question fullTextWithNoDependencies(int id) {
        return QuestionBasicInputValueDto.numericInput(id, 0, "number of items per day", "any text", "text", "Per day", 2);
    }

    public static Question fullText() {
        return QuestionBasicInputValueDto.numericInput(10003, 0, "number of items per day", "any text", "text", "Per day", 2)
                .setDependencyRules(DependencyRuleFactory.buildSingleRuleSet(10002, "==", "Values", "SelectedOption"));
    }

    public static Question fullText2(int idAdjustment) {
        return QuestionBasicInputValueDto.numericInput(10009 + idAdjustment, 0, "more number items here", "any number", "number", "Per hour", 3)
                .setDependencyRules(DependencyRuleFactory.buildSingleRuleSet(10003, "==", "100", "Value"));
    }

    public static Question fullText3() {
        return QuestionBasicInputValueDto.numericInput(10009, 0, "even more number items here", "any number", "number", "Per minute", 4);
    }

    public static Question layoutWithAllItems() {
        return new YesNoQuestionDto(10005, 0, 0, "title", "detail", "footer", 4, String.valueOf(true), String.valueOf(false), "Yes", "No")
                .setDependencyRules(DependencyRuleFactory.buildSingleRuleSet(10004, "==", "layoutWithAllItems", "SelectedOption"));
    }

    public static Question layoutWithNoTitle() {
        return new YesNoQuestionDto(10006, 0, 0, null, "detail", "footer", 4, String.valueOf(true), String.valueOf(false), "Yes", "No")
                .setDependencyRules(DependencyRuleFactory.buildSingleRuleSet(10004, "==", "layoutWithNoTitle", "SelectedOption"));
    }

    public static Question layoutWithNoDetail() {
        return new YesNoQuestionDto(10007, 0, 0, "title", null, "footer", 4, String.valueOf(true), String.valueOf(false), "Yes", "No")
                .setDependencyRules(DependencyRuleFactory.buildSingleRuleSet(10004, "==", "layoutWithNoDetail", "SelectedOption"));
    }

    public static Question layoutWithNoFooter() {
        return new YesNoQuestionDto(10008, 0, 0, "title", "detail", null, 4, String.valueOf(true), String.valueOf(false), "Yes", "No")
                .setDependencyRules(DependencyRuleFactory.buildSingleRuleSet(10004, "==", "layoutWithNoFooter", "SelectedOption"));
    }

    public static Question layoutTypes() {
        SingleSelectOptionQuestionDto question = new SingleSelectOptionQuestionDto(10004, 0, 0, "What items should be included?",
                null, null, 3);
        question.addItem("layoutWithAllItems", null);
        question.addItem("layoutWithNoTitle", null);
        question.addItem("layoutWithNoDetail", null);
        question.addItem("layoutWithNoFooter", null);
        question.setDependencyRules(DependencyRuleFactory.buildSingleRuleSet(10002, "==", "Layout tests", "SelectedOption"));
        return question;
    }

    public static Question questionWith2Rules(int id, boolean and, String rule1, String rule2) {
        DependencyRuleSet rules = and ?
                DependencyRuleFactory.buildAndDependencyRuleSet(new String[]{rule1, rule2}) :
                DependencyRuleFactory.buildOrDependencyRuleSet(new String[]{rule1, rule2});
        return new YesNoQuestionDto(id, 0, 0, "title", null, "footer", 4, String.valueOf(true), String.valueOf(false), "Yes", "No")
                .setDependencyRules(rules);
    }

    public static Question cutDownOnDrinkAlcoholHowMuch() {
        return QuestionBasicInputValueDto.numericInput(16, 0, "How much less?", null, null, "drinks per week", 5)
                .setDependencyRules(DependencyRuleFactory.buildSingleRuleSet(14, "==", "True", "Boolean"));
    }

    public static QuestionnaireSection basicSection(String visibilityTagName) {
        return new QuestionnaireSection(123, null, visibilityTagName, false);
    }
}
