package com.vitalityactive.va.questionnaire.validations;

import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.questionnaire.types.LabelOptionDto;
import com.vitalityactive.va.questionnaire.types.LabelWithAssociationsQuestionDto;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.SectionDescriptionQuestionDto;

import java.util.List;

public class Validator {
    private final QuestionnaireSetRepository repository;

    public Validator(QuestionnaireSetRepository repository) {
        this.repository = repository;
    }

    public ValidationResult validate(Question question) {

        if (question.getIdentifier() == -1 && question instanceof SectionDescriptionQuestionDto) {
            return new ValidationResult(true);
        }

        if (question instanceof LabelWithAssociationsQuestionDto && question.getAssociatedChildTypeKeys() != null) {
            return validateFromChildAssociations((LabelWithAssociationsQuestionDto) question);
        }

        return validateQuestion(question, question.getIdentifier());
    }

    private ValidationResult validateFromChildAssociations(LabelWithAssociationsQuestionDto question) {
        List<Integer> childTypeKeys = question.getAssociatedChildTypeKeys();

        for (int childTypeKey : childTypeKeys) {
            if (childTypeKey == question.getActiveChildTypeKeyForValidation()) {
                return validateQuestion(question, (long) childTypeKey);
            }
        }
        return null;
    }

    private ValidationResult validateQuestion(Question question, long questionIdentifier) {
        List<ValidationRule> rules = repository.getQuestionValidationRules(questionIdentifier);
        boolean isValid = question.isAnswered();
        Answer answer = question.getAnswer();
        String activeAnswerValue;

        if (question instanceof LabelWithAssociationsQuestionDto) {
            LabelWithAssociationsQuestionDto labelWithAssociationsQuestionDto = ((LabelWithAssociationsQuestionDto) question);
            List<LabelOptionDto.Item> items = labelWithAssociationsQuestionDto.getItems();
            for (LabelOptionDto.Item item : items) {
                if (item.typeKey == questionIdentifier) {
                    activeAnswerValue = item.textValue;
                    isValid = labelWithAssociationsQuestionDto.isAnsweredByTypeKey(item.typeKey);
                    answer.setActiveAnswerValue(activeAnswerValue);
                    break;
                }
            }
        }

        Float lowerLimit = null;
        Float upperLimit = null;
        for (ValidationRule rule : rules) {
            isValid = isValid && rule.isValid(answer);
            if (rule.getLowerLimit(answer) != null) {
                lowerLimit = rule.getLowerLimit(answer);
            }
            if (rule.getUpperLimit(answer) != null) {
                upperLimit = rule.getUpperLimit(answer);
            }
        }

        int maxLength = question.getMaxLength();
        if (maxLength != 0) {
            isValid = isValid && answer.getValue().length() <= maxLength;
        }

        answer.setActiveAnswerValue(null);

        return new ValidationResult(isValid, lowerLimit, upperLimit);
    }
}
