package com.vitalityactive.va.shared.questionnaire.repository.model;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;
import com.vitalityactive.va.utilities.InvalidModelLogger;

import java.util.Objects;

import io.realm.RealmList;
import io.realm.RealmObject;

public class QuestionModel extends RealmObject implements Model {
    public String format;
    public QuestionDecoratorModel questionDecorator;
    public RealmList<PopulationValueModel> populationValues;
    public long typeKey;
    public String typeName;
    public String visibilityTagName;
    public Integer length;
    public String questionText;
    public String questionNote;
    public String questionDescription;
    public String typeCode;
    public String questionTypeName;
    public String questionTypeCode;
    public long questionTypeKey;
    public float sortOrderIndex;
    public RealmList<UnitOfMeasureModel> unitOfMeasures;
    public RealmList<ValidValueModel> validValues;
    private QuestionnaireSectionModel section;
    public RealmList<QuestionAssociationModel> questionAssociationModels;

    public QuestionModel() {
    }

    public static QuestionModel create(QuestionnaireSetResponse.Questions responseQuestion, QuestionnaireSectionModel section) {
        if (responseQuestion.typeKey == null
                || responseQuestion.questionTypeKey == null
                || responseQuestion.sortOrderIndex == null)
        {
            InvalidModelLogger.warn(responseQuestion, responseQuestion.typeKey, "required fields are null");
            return null;
        }

        QuestionModel question = new QuestionModel();
        question.section = section;

        question.questionDecorator = QuestionDecoratorModel.create(responseQuestion.questionDecorator);
        question.typeKey = responseQuestion.typeKey;
        question.typeName = responseQuestion.typeName;
        question.visibilityTagName = responseQuestion.visibilityTagName;
        question.format = responseQuestion.format;
        question.length = responseQuestion.length;
        question.questionText = responseQuestion.text;
        question.questionNote = responseQuestion.textNote;
        question.questionDescription = responseQuestion.textDescription;
        question.typeCode = responseQuestion.typeCode;
        question.questionTypeName = responseQuestion.questionTypeName;
        question.questionTypeCode = responseQuestion.questionTypeCode;
        question.questionTypeKey = responseQuestion.questionTypeKey;
        question.sortOrderIndex = responseQuestion.sortOrderIndex;

        if (responseQuestion.unitOfMeasures != null) {
            question.unitOfMeasures = new RealmList<>();
            for (QuestionnaireSetResponse.UnitOfMeasure responseUnitOfMeasure : responseQuestion.unitOfMeasures) {
                UnitOfMeasureModel unitOfMeasureModel = UnitOfMeasureModel.create(responseUnitOfMeasure, responseQuestion.typeKey);
                if (unitOfMeasureModel != null) {
                    question.unitOfMeasures.add(unitOfMeasureModel);
                }
            }
        }

        if (responseQuestion.populationValues != null) {
            question.populationValues = new RealmList<>();
            for (QuestionnaireSetResponse.PopulationValue responseValueModel : responseQuestion.populationValues) {
                PopulationValueModel populationValueModel = PopulationValueModel.create(responseValueModel, responseQuestion.typeKey);

                if (!question.isValidUnitOfMeasure(populationValueModel)) {
                    InvalidModelLogger.warn(QuestionnaireSetResponse.PopulationValue.class,
                            responseQuestion.populationValues, responseQuestion.typeKey,
                            "populationValues' unit of measures is invalid for this question");
                } else if (populationValueModel != null) {
                    question.populationValues.add(populationValueModel);
                }
            }
        }

        if (responseQuestion.validValues != null) {
            question.validValues = new RealmList<>();
            for (QuestionnaireSetResponse.ValidValues responseValidValue : responseQuestion.validValues) {
                ValidValueModel validValueModel = ValidValueModel.create(responseValidValue, responseQuestion.typeKey, responseQuestion.questionTypeKey);
                if (validValueModel != null) {
                    question.validValues.add(validValueModel);
                }
            }
        }

        if(responseQuestion.questionAssociations != null){
            question.questionAssociationModels = new RealmList<>();
            for(QuestionnaireSetResponse.QuestionAssociations questionAssociation : responseQuestion.questionAssociations){
                QuestionAssociationModel questionAssociationModel = QuestionAssociationModel.create(questionAssociation);
                if(questionAssociationModel != null){
                    question.questionAssociationModels.add(questionAssociationModel);
                }
            }
        }

        return question;
    }

    public QuestionDecoratorModel getQuestionDecorator() {
        return questionDecorator;
    }

    private boolean isValidUnitOfMeasure(PopulationValueModel populationValueModel) {
        if (unitOfMeasures == null || unitOfMeasures.size() == 0) {
            return true;
        }
        if (populationValueModel.unitOfMeasure == null) {
            return true;
        }

        for (UnitOfMeasureModel unitOfMeasure : unitOfMeasures) {
            if (Objects.equals(populationValueModel.unitOfMeasure, unitOfMeasure.value)) {
                return true;
            }
        }

        // nothing valid found, so invalid
        return false;
    }

    public long getSectionTypeKey() {
        if (section == null) {
            return -1;
        }
        return section.typeKey;
    }
}
