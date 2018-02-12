package com.vitalityactive.va.questionnaire.types;

import android.support.annotation.Nullable;
import android.text.InputType;

import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.constants.QuestionDecorator;
import com.vitalityactive.va.constants.QuestionType;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.questionnaire.QuestionnaireUnitOfMeasureContent;
import com.vitalityactive.va.questionnaire.dependencies.DependencyRuleFactory;
import com.vitalityactive.va.shared.questionnaire.repository.model.QuestionAssociationModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.QuestionModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.UnitOfMeasureModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.ValidValueModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

public class QuestionFactory
        implements DataStore.ModelListMapper<QuestionModel, Question>,
        DataStore.ModelMapper<QuestionModel, Question> {

    private static final String YES_NAME = "PositiveResponse";
    private static final String NO_NAME = "NegativeResponse";
    private final QuestionnaireUnitOfMeasureContent content;
    private final List<Integer> childQuestionHolders = new ArrayList<>();

    public QuestionFactory(QuestionnaireUnitOfMeasureContent content) {
        this.content = content;
    }

    @Nullable
    Question createQuestion(QuestionModel questionModel) {
        final int questionTypeKey = (int) questionModel.questionTypeKey;
        final int decoratorTypeKey = (int) questionModel.getQuestionDecorator().typeKey;

        Question question;

        switch (questionTypeKey) {
            case QuestionType._SINGLESELECT:
                question = singleSelectQuestion(decoratorTypeKey, questionModel);
                break;
            case QuestionType._DATE:
                question = dateQuestion(decoratorTypeKey, questionModel);
                break;
            case QuestionType._MULTISELECT:
                question = multiSelectQuestion(decoratorTypeKey, questionModel);
                break;
            case QuestionType._LABEL:
                question = labelQuestion(questionModel);
                break;
            case QuestionType._NUMBERRANGE:
            case QuestionType._DECIMALRANGE:
            case QuestionType._CHILDNUMBERRANGE:
                question = numberRangeQuestion(decoratorTypeKey, questionModel);
                break;
            case QuestionType._FREETEXT:
                question = freeTextQuestion(questionModel);
                break;
            default:
                return null;
        }

        setChildAssociationStatus(question);
        createDependencyRule(question, questionModel);
        createAssociationRule(question, questionModel);

        return question;
    }

    private Question labelQuestion(QuestionModel questionModel) {
        if (isQuestionAssociationsAvailable(questionModel)) {
            return labelWithAssociation(questionModel);
        } else {
            return defaultLabelQuestion(questionModel);
        }
    }

    private Question freeTextQuestion(QuestionModel questionModel) {
        return QuestionFreeTextDto.textInput(questionModel.typeKey,
                questionModel.questionTypeKey,
                questionModel.questionText,
                questionModel.questionDescription,
                questionModel.questionNote,
                null,
                questionModel.sortOrderIndex,
                questionModel.length == null ? 0 : questionModel.length);
    }

    private void createDependencyRule(Question question, QuestionModel questionModel) {
        if (question != null && questionModel.visibilityTagName != null) {
            question.setDependencyRules(DependencyRuleFactory.buildSet(questionModel.visibilityTagName));
        }
    }

    private void createAssociationRule(Question question, QuestionModel questionModel) {
        if (question != null && isQuestionAssociationsAvailable(questionModel)) {
            List<Integer> associationKeys = new ArrayList<>();
            List<QuestionAssociationModel> associations = questionModel.questionAssociationModels;
            for (QuestionAssociationModel associationModel : associations) {
                int typeKey = associationModel.childQuestionKey;
                associationKeys.add(typeKey);
                childQuestionHolders.add(typeKey);
            }

            question.setAssociatedChildTypeKeys(associationKeys);
        }
    }

    private boolean isQuestionAssociationsAvailable(QuestionModel questionModel) {
        return (questionModel.questionAssociationModels != null
                && !questionModel.questionAssociationModels.isEmpty());
    }

    private void setChildAssociationStatus(Question question) {
        for (Integer childQuestionHolder : childQuestionHolders) {
            if (question.getIdentifier() == childQuestionHolder) {
                question.setChildQuestion(true);
                childQuestionHolders.remove(childQuestionHolder);

                break;
            }
        }
    }

    public void loadAnswers(List<Question> questions, Map<Long, Answer> answers) {
        if (questions == null || answers == null)
            return;

        for (Question question : questions) {
            long questionId = question.getIdentifier();
            if (answers.containsKey(questionId)) {
                Answer answer = answers.get(questionId);
                if (answer != null && answer.getValues().size() > 0) {
                    question.loadAnswer(answer);
                } else if (answer != null) {
                    question.hasAnAnswerSavedButNoValues();
                }

                if (answer != null && answer.isPrePopulatedValue()) {
                    question.setPrePopulatedAnswer(answer.getPrePopulationValueDate());
                    question.setPrePopulatedSource(answer.getPrePopulationValueSource());
                }
            }
        }
    }

    @Override
    public List<Question> mapModels(List<QuestionModel> models) {
        List<Question> questions = null;
        List<Question> questionsWithAssociations = null;
        if (models != null) {
            questions = new ArrayList<>();
            questionsWithAssociations = new ArrayList<>();
            for (QuestionModel model : models) {
                Question question = createQuestion(model);
                if (question != null) {
                    if (question instanceof LabelWithAssociationsQuestionDto) {
                        questionsWithAssociations.add(question);
                    }
                    questions.add(question);
                }
            }

            updateLabelQuestionsWithAssociations(questions, questionsWithAssociations);
        }

        return questions;
    }

    @Override
    public Question mapModel(QuestionModel model) {
        return model == null ? null : createQuestion(model);
    }

    private Question singleSelectQuestion(int decoratorTypeKey,
                                          QuestionModel questionModel) {
        switch (decoratorTypeKey) {
            case QuestionDecorator._RADIOBUTTON:
                return singleSelectRadioButtons(questionModel);
            case QuestionDecorator._YESNO:
                return singleSelectYesNo(questionModel);
            case QuestionDecorator._TOGGLE:
                return singleSelectCheckBox(questionModel);
            default:
                return null;
        }
    }

    private Question singleSelectCheckBox(QuestionModel questionModel) {
        return new SingleCheckboxQuestionDto(questionModel.typeKey,
                questionModel.questionTypeKey,
                questionModel.getSectionTypeKey(),
                questionModel.questionText,
                questionModel.questionDescription,
                questionModel.questionNote,
                questionModel.sortOrderIndex);
    }

    private Question singleSelectYesNo(QuestionModel questionModel) {
        return new YesNoQuestionDto(questionModel.typeKey,
                questionModel.questionTypeKey,
                questionModel.getSectionTypeKey(),
                questionModel.questionText,
                questionModel.questionDescription,
                questionModel.questionNote,
                questionModel.sortOrderIndex,
                YES_NAME,
                NO_NAME,
                getYesValue(questionModel),
                getNoValue(questionModel));
    }

    private String getYesValue(QuestionModel questionModel) {
        return getValueForValidValueWithName(questionModel.validValues, YES_NAME, "Yes");
    }

    private String getNoValue(QuestionModel questionModel) {
        return getValueForValidValueWithName(questionModel.validValues, NO_NAME, "No");
    }

    private String getValueForValidValueWithName(RealmList<ValidValueModel> validValues, String name, String defaultValue) {
        for (ValidValueModel model : validValues) {
            if (name.equals(model.name)) {
                return model.value;
            }
        }
        return defaultValue;
    }

    private Question singleSelectRadioButtons(QuestionModel questionModel) {
        final SingleSelectOptionQuestionDto singleSelectOptionQuestionDto = new SingleSelectOptionQuestionDto(questionModel.typeKey,
                questionModel.questionTypeKey,
                questionModel.getSectionTypeKey(),
                questionModel.questionText,
                questionModel.questionDescription,
                questionModel.questionNote,
                questionModel.sortOrderIndex);

        if (questionModel.validValues != null) {
            for (ValidValueModel validValue : questionModel.validValues) {
                singleSelectOptionQuestionDto.addItem(validValue.name, validValue.value, validValue.description, validValue.note);
            }
        }

        return singleSelectOptionQuestionDto;
    }

    private Question dateQuestion(int decoratorTypeKey, QuestionModel questionModel) {
        switch (decoratorTypeKey) {
            case QuestionDecorator._DATEPICKER:
                return datePickerQuestion(questionModel);
            default:
                return null;
        }
    }

    private Question datePickerQuestion(QuestionModel questionModel) {
        return new QuestionDateInputDto((int) questionModel.typeKey,
                questionModel.questionTypeKey,
                questionModel.getSectionTypeKey(),
                questionModel.questionText,
                questionModel.questionDescription,
                questionModel.questionNote,
                questionModel.sortOrderIndex);
    }

    private Question multiSelectQuestion(int decoratorTypeKey, QuestionModel questionModel) {
        switch (decoratorTypeKey) {
            case QuestionDecorator._CHECKBOX:
                return multiSelectCheckbox(questionModel);
            default:
                return null;
        }
    }

    private Question multiSelectCheckbox(QuestionModel questionModel) {
        final MultiOptionOptionQuestionDto multiOptionOptionQuestionDto = new MultiOptionOptionQuestionDto(questionModel.typeKey,
                questionModel.questionTypeKey,
                questionModel.getSectionTypeKey(),
                questionModel.questionText,
                questionModel.questionDescription,
                questionModel.questionNote,
                questionModel.sortOrderIndex);

        if (questionModel.validValues != null) {
            for (ValidValueModel validValue : questionModel.validValues) {
                multiOptionOptionQuestionDto.addItem(validValue.name, validValue.value, validValue.description, validValue.note);
            }
        }

        return multiOptionOptionQuestionDto;
    }

    private Question labelWithAssociation(QuestionModel questionModel) {
        return LabelWithAssociationsQuestionDto.numericInput(questionModel.typeKey,
                (int) questionModel.questionTypeKey,
                questionModel.getSectionTypeKey(),
                questionModel.questionText,
                questionModel.questionDescription,
                questionModel.questionNote,
                null,
                questionModel.sortOrderIndex);
    }

    private Question defaultLabelQuestion(QuestionModel questionModel) {
        return new LabelQuestionDto(questionModel.typeKey,
                questionModel.questionTypeKey,
                questionModel.getSectionTypeKey(),
                questionModel.questionText,
                questionModel.questionDescription,
                questionModel.questionNote,
                questionModel.sortOrderIndex);
    }

    private Question numberRangeQuestion(int decoratorTypeKey, QuestionModel questionModel) {
        switch (decoratorTypeKey) {
            case QuestionDecorator._TEXTBOX:
                return numberRangeTextBox(questionModel);
            default:
                return null;
        }
    }

    private Question numberRangeTextBox(QuestionModel questionModel) {
        final QuestionBasicInputValueDto questionBasicInputValueDto =
                QuestionBasicInputValueDto.numericInput(questionModel.typeKey,
                        (int) questionModel.questionTypeKey,
                        questionModel.getSectionTypeKey(),
                        questionModel.questionText,
                        questionModel.questionDescription,
                        questionModel.questionNote,
                        null,
                        questionModel.sortOrderIndex
                );

        if (questionModel.unitOfMeasures != null) {
            for (UnitOfMeasureModel unitOfMeasureModel : questionModel.unitOfMeasures) {
                final UnitsOfMeasure unitsOfMeasure = UnitsOfMeasure.fromValue(unitOfMeasureModel.value);
                questionBasicInputValueDto.addUnit(unitsOfMeasure,
                        content.getUnitOfMeasureSymbol(unitsOfMeasure),
                        content.getUnitOfMeasureDisplayName(unitsOfMeasure));
            }
        }

        return questionBasicInputValueDto;
    }

    public Question sectionDescription(String sectionDescription, Long sectionTypeKey) {
        return new SectionDescriptionQuestionDto(-1,
                InternalQuestionType.SECTION_DESCRIPTION.ordinal(),
                sectionTypeKey,
                sectionDescription,
                null,
                null,
                -1);
    }

    private void updateLabelQuestionsWithAssociations(List<Question> questions, List<Question> questionsWithAssociations) {
        int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;

        for (Question questionWithAssociation : questionsWithAssociations) {
            List<Integer> questionAssociationIds = questionWithAssociation.getAssociatedChildTypeKeys();
            for (Integer questionAssociationId : questionAssociationIds) {
                Question childQuestion = getQuestionByIdentifier(questions, questionAssociationId);
                if (childQuestion != null && questionWithAssociation instanceof LabelWithAssociationsQuestionDto) {
                    ((LabelWithAssociationsQuestionDto) questionWithAssociation).addItem(childQuestion.getTitle(), "", inputType, questionAssociationId);
                }
            }
        }
    }

    private Question getQuestionByIdentifier(List<Question> questions, int identifier) {
        for (Question question : questions) {
            if ((int) question.getIdentifier() == identifier) {
                return question;
            }
        }
        return null;
    }
}
