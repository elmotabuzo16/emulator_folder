package com.vitalityactive.va.myhealth.content;


import java.util.List;

public class FeedbackItem {

    String attributeTitle;
    String attributeValue;
    String attributeFriendlyValue;
    String feedbackName;
    String whyIsThisImportant;
    String displayValue;
    int feedbackTypeKey;
    String feedbackTypeCode;
    List<FeedbackTip> feedbackTips;
    int attributeSortOrder;
    int sectionTypeKey;
    int attributeTypeKey;

    public FeedbackItem() {
    }

    public FeedbackItem(String attributeTitle, String attributeValue, String attributeFriendlyValue, String feedbackName, String whyIsThisImportant, int attributeSortOrder, int sectionTypeKey, int attributeTypeKey, List<FeedbackTip> feedbackTips, int feedbackTypeKey,
                        String feedbackTypeCode) {
        this.attributeTitle = attributeTitle;
        this.attributeValue = attributeValue;
        this.attributeFriendlyValue = attributeFriendlyValue;
        this.feedbackName = feedbackName;
        this.whyIsThisImportant = whyIsThisImportant;
        this.attributeSortOrder = attributeSortOrder;
        this.sectionTypeKey = sectionTypeKey;
        this.attributeTypeKey = attributeTypeKey;
        this.feedbackTips = feedbackTips;
        this.feedbackTypeKey = feedbackTypeKey;
        this.feedbackTypeCode = feedbackTypeCode;
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

    public List<FeedbackTip> getFeedbackTips() {
        return feedbackTips;
    }

    public int getFeedbackTypeKey() {
        return feedbackTypeKey;
    }

    public String getFeedbackTypeCode() {
        return feedbackTypeCode;
    }

    public String getDisplayValue() {
        return getAttributeFriendlyValue() != null && !getAttributeFriendlyValue().trim().isEmpty() ? getAttributeFriendlyValue() : getAttributeValue();
    }

    public static class Builder {
        int feedbackTypeKey;
        String feedbackTypeCode;
        List<FeedbackTip> feedbackTips;
        private String attributeTitle;
        private String attributeValue;
        private String attributeFriendlyValue;
        private String feedbackName;
        private String whyIsThisImportant;
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

        public Builder setWhyIsThisImportant(String whyIsThisImportant) {
            this.whyIsThisImportant = whyIsThisImportant;
            return this;
        }

        public Builder setFeedbackTypeCode(String feedbackTypeCode) {
            this.feedbackTypeCode = feedbackTypeCode;
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

        public Builder setFeedbackTips(List<FeedbackTip> feedbackTips) {
            this.feedbackTips = feedbackTips;
            return this;
        }

        public Builder setFeedbackTypeKey(int feedbackTypeKey) {
            this.feedbackTypeKey = feedbackTypeKey;
            return this;
        }

        public Builder setAttributeTypeKey(int attributeTypeKey) {
            this.attributeTypeKey = attributeTypeKey;
            return this;
        }

        public FeedbackItem build() {
            return new FeedbackItem(attributeTitle, attributeValue, attributeFriendlyValue, feedbackName, whyIsThisImportant, attributeSortOrder, sectionTypeKey, attributeTypeKey, feedbackTips, feedbackTypeKey, feedbackTypeCode);
        }
    }
}
