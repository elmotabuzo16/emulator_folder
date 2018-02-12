package com.vitalityactive.va.persistence.models.vitalityage;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class VitalityAgePersistenceModel extends RealmObject implements Model {

    String age;
    Integer feedbackType;
    String feedbackContent;
    String feedbackTitle;

    public VitalityAgePersistenceModel() {
        super();
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(Integer feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public String getFeedbackTitle() {
        return feedbackTitle;
    }

    public void setFeedbackTitle(String feedbackTitle) {
        this.feedbackTitle = feedbackTitle;
    }
}
