package com.vitalityactive.va.shared.questionnaire.repository.model;

import io.realm.RealmModel;
import io.realm.RealmObject;

public class AnswerItemModel extends RealmObject implements RealmModel {
    public String value;

    public AnswerItemModel() {
    }

    public AnswerItemModel(String value) {
        this.value = value;
    }
}
