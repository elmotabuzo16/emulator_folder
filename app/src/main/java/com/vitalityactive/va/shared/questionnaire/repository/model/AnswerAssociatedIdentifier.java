package com.vitalityactive.va.shared.questionnaire.repository.model;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class AnswerAssociatedIdentifier extends RealmObject implements Model {
    public int identifier;

    public AnswerAssociatedIdentifier() {
    }

    public AnswerAssociatedIdentifier(int identifier) {
        this.identifier = identifier;
    }
}
