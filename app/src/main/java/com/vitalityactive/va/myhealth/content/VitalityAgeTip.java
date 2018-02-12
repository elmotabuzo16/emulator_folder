package com.vitalityactive.va.myhealth.content;

public class VitalityAgeTip {

    String attributeTitle;
    String attributeValue;
    String feedbackName;
    String feedbackTipName;
    String feedbackTipNote;
    int attributeSortOrder;
    int typeKey;

    public VitalityAgeTip() {
    }

    public VitalityAgeTip(String attributeTitle, String attributeValue, String feedbackName, String feedbackTipName, String feedbackTipNote, int attributeSortOrder, int typeKey) {
        this.attributeTitle = attributeTitle;
        this.attributeValue = attributeValue;
        this.feedbackName = feedbackName;
        this.feedbackTipName = feedbackTipName;
        this.feedbackTipNote = feedbackTipNote;
        this.attributeSortOrder = attributeSortOrder;
        this.typeKey = typeKey;
    }

    public String getAttributeTitle() {
        return attributeTitle;
    }

    public void setAttributeTitle(String attributeTitle) {
        this.attributeTitle = attributeTitle;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getFeedbackName() {
        return feedbackName;
    }

    public void setFeedbackName(String feedbackName) {
        this.feedbackName = feedbackName;
    }

    public String getFeedbackTipName() {
        return feedbackTipName;
    }

    public void setFeedbackTipName(String feedbackTipName) {
        this.feedbackTipName = feedbackTipName;
    }

    public String getFeedbackTipNote() {
        return feedbackTipNote;
    }

    public void setFeedbackTipNote(String feedbackTipNote) {
        this.feedbackTipNote = feedbackTipNote;
    }

    public int getAttributeSortOrder() {
        return attributeSortOrder;
    }

    public void setAttributeSortOrder(int attributeSortOrder) {
        this.attributeSortOrder = attributeSortOrder;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(int typeKey) {
        this.typeKey = typeKey;
    }

}
