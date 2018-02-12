package com.vitalityactive.va.myhealth.content;


import java.util.List;

public class AttributeItem {


    String attributeTitle;
    String attributeValue;
    String attributeFriendlyValue;
    String displayValue;
    int attributeSortOrder;
    int sectionTypeKey;
    int attributeTypeKey;
    List<FeedbackItem> feedbackItems;

    public AttributeItem(String attributeTitle, String attributeValue, String attributeFriendlyValue, int attributeSortOrder, int sectionTypeKey, int attributeTypeKey, List<FeedbackItem> feedbackItems) {
        this.attributeTitle = attributeTitle;
        this.attributeValue = attributeValue;
        this.attributeFriendlyValue = attributeFriendlyValue;
        this.attributeSortOrder = attributeSortOrder;
        this.sectionTypeKey = sectionTypeKey;
        this.attributeTypeKey = attributeTypeKey;
        this.feedbackItems = feedbackItems;
    }

    public String getAttributeTitle() {
        return attributeTitle;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public String getAttributeFriendlyValue() {
        return attributeFriendlyValue;
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

    public List<FeedbackItem> getFeedbackItems() {
        return feedbackItems;
    }

    public String getDisplayValue() {
        return getAttributeFriendlyValue() != null && !getAttributeFriendlyValue().trim().isEmpty() ? getAttributeFriendlyValue() : getAttributeValue();
    }

    public static class Builder {
        List<FeedbackItem> feedbackItems;
        private String attributeTitle;
        private String attributeValue;
        private String attributeFriendlyValue;
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

        public Builder setAttributeSortOrder(int attributeSortOrder) {
            this.attributeSortOrder = attributeSortOrder;
            return this;
        }

        public Builder setSectionTypeKey(int sectionTypeKey) {
            this.sectionTypeKey = sectionTypeKey;
            return this;
        }

        public Builder setFeedbackItems(List<FeedbackItem> feedbackItems) {
            this.feedbackItems = feedbackItems;
            return this;
        }

        public Builder setAttributeTypeKey(int attributeTypeKey) {
            this.attributeTypeKey = attributeTypeKey;
            return this;
        }

        public AttributeItem build() {
            return new AttributeItem(attributeTitle, attributeValue, attributeFriendlyValue, attributeSortOrder, sectionTypeKey, attributeTypeKey, feedbackItems);
        }
    }
}
