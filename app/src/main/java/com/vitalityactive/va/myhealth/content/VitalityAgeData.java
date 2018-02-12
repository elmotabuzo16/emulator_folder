package com.vitalityactive.va.myhealth.content;

public class VitalityAgeData {
    public String itemFeedbackSummary;
    public int itemFeedbackType;
    public String itemFeedbackExplanation;
    public String itemVitalityAgeValue;
    public String itemVitalityAgeValuePlaceHolder;
    public int summaryIconResourceId;
    public int bgIconResource;
    public boolean isUnknown;

    public VitalityAgeData(String itemFeedbackSummary, int itemFeedbackType, String itemFeedbackExplanation, String itemVitalityAgeValue, String itemVitalityAgeValuePlaceHolder, int summaryIconResourceId, int bgIconResource, boolean isUnknown) {
        this.itemFeedbackSummary = itemFeedbackSummary;
        this.itemFeedbackType = itemFeedbackType;
        this.itemFeedbackExplanation = itemFeedbackExplanation;
        this.itemVitalityAgeValue = itemVitalityAgeValue;
        this.itemVitalityAgeValuePlaceHolder = itemVitalityAgeValuePlaceHolder;
        this.summaryIconResourceId = summaryIconResourceId;
        this.bgIconResource = bgIconResource;
        this.isUnknown = isUnknown;
    }


    public static class Builder {
        private String itemFeedbackSummary;
        private int itemFeedbackType;
        private String itemFeedbackExplanation;
        private String itemVitalityAgeValue;
        private String itemVitalityAgeValuePlaceHolder;
        private int summaryIconResourceId;
        private int bgIconResource;
        private boolean isUnknown;

        public Builder setItemFeedbackSummary(String itemFeedbackSummary) {
            this.itemFeedbackSummary = itemFeedbackSummary;
            return this;
        }

        public Builder setItemFeedbackType(int itemFeedbackType) {
            this.itemFeedbackType = itemFeedbackType;
            return this;
        }

        public Builder setItemFeedbackExplanation(String itemFeedbackExplanation) {
            this.itemFeedbackExplanation = itemFeedbackExplanation;
            return this;
        }

        public Builder setItemVitalityAgeValue(String itemVitalityAgeValue) {
            this.itemVitalityAgeValue = itemVitalityAgeValue;
            return this;
        }

        public Builder setItemVitalityAgeValuePlaceHolder(String itemVitalityAgeValuePlaceHolder) {
            this.itemVitalityAgeValuePlaceHolder = itemVitalityAgeValuePlaceHolder;
            return this;
        }

        public Builder setSummaryIconResourceId(int summaryIconResourceId) {
            this.summaryIconResourceId = summaryIconResourceId;
            return this;
        }

        public Builder setBgIconResource(int bgIconResource) {
            this.bgIconResource = bgIconResource;
            return this;
        }

        public Builder setIsUnknown(boolean isUnknown) {
            this.isUnknown = isUnknown;
            return this;
        }

        public VitalityAgeData build() {
            return new VitalityAgeData(itemFeedbackSummary, itemFeedbackType, itemFeedbackExplanation, itemVitalityAgeValue, itemVitalityAgeValuePlaceHolder, summaryIconResourceId, bgIconResource, isUnknown);
        }
    }
}
