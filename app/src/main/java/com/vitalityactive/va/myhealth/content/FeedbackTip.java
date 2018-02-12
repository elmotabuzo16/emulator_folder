package com.vitalityactive.va.myhealth.content;

public class FeedbackTip {

    String attributeTitle;
    String attributeValue;
    String attributeFriendlyValue;
    String feedbackName;
    String whyIsThisImportant;
    String feedbackTipName;
    String feedbackTipNote;
    String feedbackTipTypeCode;
    int feedbackTipTypeKey;
    int attributeSortOrder;
    int sectionTypeKey;
    int attributeTypeKey;

    public FeedbackTip() {
    }

    public FeedbackTip(String attributeTitle, String attributeValue, String attributeFriendlyValue, String feedbackName, String whyIsThisImportant, String feedbackTipName, String feedbackTipNote, String feedbackTipTypeCode, int feedbackTipTypeKey, int attributeSortOrder, int sectionTypeKey, int attributeTypeKey) {
        this.attributeTitle = attributeTitle;
        this.attributeValue = attributeValue;
        this.attributeFriendlyValue = attributeFriendlyValue;
        this.feedbackName = feedbackName;
        this.whyIsThisImportant = whyIsThisImportant;
        this.feedbackTipName = feedbackTipName;
        this.feedbackTipNote = feedbackTipNote;
        this.attributeSortOrder = attributeSortOrder;
        this.sectionTypeKey = sectionTypeKey;
        this.attributeTypeKey = attributeTypeKey;
        this.feedbackTipTypeCode = feedbackTipTypeCode;
        this.feedbackTipTypeKey = feedbackTipTypeKey;
    }

    public String getAttributeTitle() {
        return attributeTitle;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public String getFeedbackName() {
        return feedbackName;
    }

    public String getFeedbackTipName() {
        return feedbackTipName;
    }

    public String getFeedbackTipNote() {
        return feedbackTipNote;
    }

    public int getAttributeSortOrder() {
        return attributeSortOrder;
    }

    public int getSectionTypeKey() {
        return sectionTypeKey;
    }

    public int getAttributeTypeKey() {
        return attributeTypeKey;
    }

    public String getWhyIsThisImportant() {
        return whyIsThisImportant;
    }

    public String getAttributeFriendlyValue() {
        return attributeFriendlyValue;
    }

    public String getFeedbackTipTypeCode() {
        return feedbackTipTypeCode;
    }

    public int getFeedbackTipTypeKey() {
        return feedbackTipTypeKey;
    }

    public static class Builder {
        public String feedbackTipTypeCode;
        public int feedbackTipTypeKey;
        private String attributeTitle;
        private String attributeValue;
        private String attributeFriendlyValue;
        private String feedbackName;
        private String whyIsThisImportant;
        private String feedbackTipName;
        private String feedbackTipNote;
        private int attributeSortOrder;
        private int sectionTypeKey;
        private int attributeTypeKey;

        public Builder setAttributeTitle(String attributeTitle) {
            this.attributeTitle = attributeTitle;
            return this;
        }

        public Builder setAttributeValue(String attributeValue) {
            this.attributeValue = attributeValue;
            return this;
        }

        public Builder setAttributeFriendlyValue(String attributeFriendlyValue) {
            this.attributeFriendlyValue = attributeFriendlyValue;
            return this;
        }

        public Builder setFeedbackName(String feedbackName) {
            this.feedbackName = feedbackName;
            return this;
        }

        public Builder setFeedbackTipName(String feedbackTipName) {
            this.feedbackTipName = feedbackTipName;
            return this;
        }

        public Builder setFeedbackTipNote(String feedbackTipNote) {
            this.feedbackTipNote = feedbackTipNote;
            return this;
        }

        public Builder setWhyIsThisImportant(String whyIsThisImportant) {
            this.whyIsThisImportant = whyIsThisImportant;
            return this;
        }

        public Builder setAttributeSortOrder(int attributeSortOrder) {
            this.attributeSortOrder = attributeSortOrder;
            return this;
        }

        public Builder setSectionTypeKey(int sectionTypeKey) {
            this.sectionTypeKey = sectionTypeKey;
            return this;
        }

        public Builder setFeedbackTipTypeCode(String feedbackTipTypeCode) {
            this.feedbackTipTypeCode = feedbackTipTypeCode;
            return this;
        }

        public Builder setFeedbackTipTypeKey(int feedbackTipTypeKey) {
            this.feedbackTipTypeKey = feedbackTipTypeKey;
            return this;
        }

        public Builder setAttributeTypeKey(int attributeTypeKey) {
            this.attributeTypeKey = attributeTypeKey;
            return this;
        }

        public FeedbackTip build() {
            return new FeedbackTip(attributeTitle, attributeValue, attributeFriendlyValue, feedbackName, whyIsThisImportant, feedbackTipName, feedbackTipNote, feedbackTipTypeCode, feedbackTipTypeKey, attributeSortOrder, sectionTypeKey, attributeTypeKey);
        }
    }
}
