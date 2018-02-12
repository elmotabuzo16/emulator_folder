package com.vitalityactive.va.myhealth.entity;

public class VitalityAge {


    private String age;

    private String feedbackTitle;

    private String feedbackContent;

    private int actualType;

    private int effectiveType;

    private String variance;

    public VitalityAge(Builder builder) {
        this.age = builder.age;
        this.feedbackTitle = builder.feedbackTitle;
        this.feedbackContent = builder.feedbackContent;
        this.effectiveType = builder.effectiveType;
        this.variance = builder.variance;
        this.actualType = builder.actualType;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getFeedbackTitle() {
        return feedbackTitle;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public int getEffectiveType() {
        return effectiveType;
    }

    public String getVariance() {
        return variance;
    }

    public int getActualType() {
        return actualType;
    }

    public static class Builder {
        private String age;

        private String feedbackTitle;

        private String feedbackContent;

        private String variance;

        private int actualType;

        private int effectiveType;


        public Builder() {
        }

        public Builder age(String age) {
            this.age = age;
            return this;
        }

        public Builder feedbackTitle(String feedbackTitle) {
            this.feedbackTitle = feedbackTitle;
            return this;
        }

        public Builder feedbackContent(String feedbackContent) {
            this.feedbackContent = feedbackContent;
            return this;
        }

        public Builder effectiveType(int effectiveType) {
            this.effectiveType = effectiveType;
            return this;
        }

        public Builder actualType(int actualType) {
            this.actualType = actualType;
            return this;
        }

        public Builder variance(String variance) {
            this.variance = variance;
            return this;
        }

        public VitalityAge build() {
            return new VitalityAge(this);
        }
    }
}
