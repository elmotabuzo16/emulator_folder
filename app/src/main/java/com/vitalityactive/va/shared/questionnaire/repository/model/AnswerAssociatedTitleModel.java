package com.vitalityactive.va.shared.questionnaire.repository.model;


import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class AnswerAssociatedTitleModel extends RealmObject implements Model {
    public String title;

    public AnswerAssociatedTitleModel() {
    }

    public AnswerAssociatedTitleModel(String title) {
        this.title = title;
    }
}
