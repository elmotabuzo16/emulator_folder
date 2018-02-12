package com.vitalityactive.va.shared.questionnaire.repository.model;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class QuestionnaireLastSeenModel extends RealmObject implements Model {
    @PrimaryKey
    private long questionnaireKey;
    private long sectionTypeKey;

    public QuestionnaireLastSeenModel(long questionnaireKey, long sectionTypeKey) {
        this.questionnaireKey = questionnaireKey;
        this.sectionTypeKey = sectionTypeKey;
    }

    public QuestionnaireLastSeenModel() {
    }

    public long getSectionTypeKey() {
        return sectionTypeKey;
    }
}
