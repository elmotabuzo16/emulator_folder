package com.vitalityactive.va.shared.questionnaire.repository.model;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.utilities.TimeUtilities;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AnswerModel extends RealmObject implements Model {
    @PrimaryKey
    public long questionId;
    public long answeredTimestamp;
    private RealmList<AnswerItemModel> values;
    private String unitOfMeasureKey;
    private long prepopulatedSourceDate;
    private boolean isPrePopulatedValue;
    private int prepopulatedSourceEvent;
    private RealmList<AnswerAssociatedIdentifier> associatedValuesIdentifier;
    private RealmList<AnswerAssociatedTitleModel> associatedValuesTitle;

    @SuppressWarnings("unused") // required by realm
    public AnswerModel() {
    }

    public AnswerModel(long questionId, Answer answer, long answeredTimestamp) {
        this.questionId = questionId;
        if (answer.getUnitOfMeasureKey() != null)
            this.unitOfMeasureKey = answer.getUnitOfMeasureKey();
        this.values = buildCheckedItems(answer);
        this.answeredTimestamp = answeredTimestamp;
        this.associatedValuesIdentifier = buildAssociatedIdentifierList(answer);
        this.associatedValuesTitle = buildAssociatedTitleList(answer);
    }

    public AnswerModel(List<PopulationValueModel> values) {
        if (values == null || values.size() == 0)
            return;
        this.questionId = values.get(0).questionTypeKey;
        this.unitOfMeasureKey = values.get(0).unitOfMeasure;
        this.prepopulatedSourceDate = TimeUtilities.getDateFromFormatterString(values.get(0).eventDate);
        this.prepopulatedSourceEvent = values.get(0).eventKey;
        this.values = buildCheckedItems(values);
        this.answeredTimestamp = TimeUtilities.getCurrentTimestamp();
        this.isPrePopulatedValue = true;
    }

    public Answer getAnswer() {
        Answer answer = new Answer("", unitOfMeasureKey);
        answer.setPrePopulationValueDate(prepopulatedSourceDate);
        answer.setPrePopulatedValue(isPrePopulatedValue);
        answer.setPrePopulationValueSource(prepopulatedSourceEvent);

        if (values != null) {
            answer.getValues().clear();
            for (AnswerItemModel checkedItemValue : values) {
                answer.addCheckedItem(checkedItemValue.value);
            }
        }

        answer = setAssociationData(answer);

        return answer;
    }

    private Answer setAssociationData(Answer answer) {
        List<String> associatedTitles = new ArrayList<>();
        List<Integer> associatedIdentifiers = new ArrayList<>();

        if(associatedValuesTitle != null && associatedValuesIdentifier != null){
            for (AnswerAssociatedTitleModel titleModel : associatedValuesTitle) {
                associatedTitles.add(titleModel.title);
            }

            for (AnswerAssociatedIdentifier identifierModel : associatedValuesIdentifier) {
                associatedIdentifiers.add(identifierModel.identifier);
            }
        }

        answer.setAssociatedValuesTitle(associatedTitles);
        answer.setAssociatedValuesIdentifier(associatedIdentifiers);

        return answer;
    }

    private RealmList<AnswerItemModel> buildCheckedItems(Answer answer) {
        RealmList<AnswerItemModel> list = new RealmList<>();
        for (String s : answer.getValues()) {
            list.add(new AnswerItemModel(s));
        }
        return list;
    }

    private RealmList<AnswerItemModel> buildCheckedItems(List<PopulationValueModel> values) {
        RealmList<AnswerItemModel> list = new RealmList<>();
        for (PopulationValueModel value : values) {
            if (value.value != null) {
                list.add(new AnswerItemModel(value.value));
            } else if (value.fromValue != null) {
                list.add(new AnswerItemModel(value.fromValue));
            }
        }
        return list;
    }

    private RealmList<AnswerAssociatedTitleModel> buildAssociatedTitleList(Answer answer) {
        RealmList<AnswerAssociatedTitleModel> list = new RealmList<>();
        List<String> associatedTitles = answer.getAssociatedValuesTitle();
        for (String title : associatedTitles) {
            list.add(new AnswerAssociatedTitleModel(title));
        }
        return list;
    }

    private RealmList<AnswerAssociatedIdentifier> buildAssociatedIdentifierList(Answer answer) {
        RealmList<AnswerAssociatedIdentifier> list = new RealmList<>();
        List<Integer> associatedIdentifiers = answer.getAssociatedValuesIdentifier();
        for (int identifier : associatedIdentifiers) {
            list.add(new AnswerAssociatedIdentifier(identifier));
        }
        return list;
    }
}
