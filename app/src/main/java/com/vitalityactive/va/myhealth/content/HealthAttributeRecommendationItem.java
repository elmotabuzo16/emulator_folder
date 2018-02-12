package com.vitalityactive.va.myhealth.content;


import java.util.List;

public class HealthAttributeRecommendationItem {

    List<Recommendation> recommendations;
    List<FeedbackTip> feedbackTips;
    List<FeedbackItem> feedbacks;
    String measuredOn;
    String eventDateLogged;
    String friendlyValue;
    int sectionTypeKey;
    String source;

    public HealthAttributeRecommendationItem(List<Recommendation> recommendations,
                                             List<FeedbackItem> feedbacks, String measuredOn, String eventDateLogged, String friendlyValue, String source, int sectionTypeKey) {
        this.recommendations = recommendations;
        this.measuredOn = measuredOn;
        this.feedbacks = feedbacks;
        this.eventDateLogged = eventDateLogged;
        this.sectionTypeKey = sectionTypeKey;
        this.friendlyValue = friendlyValue;
        this.source = source;
    }

    public Recommendation getDisplayRecommendation() {
        if (recommendations != null && recommendations.size() > 0) {
            return recommendations.get(0);
        }
        return null;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public String getMeasuredOn() {
        return measuredOn;
    }

    public List<FeedbackTip> getFeedbackTips() {
        return feedbackTips;
    }

    public String getEventDateLogged() {
        return eventDateLogged;
    }

    public int getSectionTypeKey() {
        return sectionTypeKey;
    }

    public void setSectionTypeKey(int sectionTypeKey) {
        this.sectionTypeKey = sectionTypeKey;
    }

    public List<FeedbackItem> getFeedbacks() {
        return feedbacks;
    }

    public String getFriendlyValue() {
        return friendlyValue;
    }

    public String getSource() {
        return source;
    }

    public static class Recommendation {

        private String fromValue;
        private String toValue;
        private Integer unitOfMeasure;
        private String value;
        private String friendlyValue;

        public Recommendation(String fromValue, String toValue, Integer unitOfMeasure, String value, String friendlyValue) {
            this.fromValue = fromValue;
            this.toValue = toValue;
            this.unitOfMeasure = unitOfMeasure;
            this.value = value;
            this.friendlyValue = friendlyValue;
        }

        public String getFromValue() {
            return fromValue;
        }

        public String getToValue() {
            return toValue;
        }

        public Integer getUnitOfMeasure() {
            return unitOfMeasure;
        }

        public String getValue() {
            return value;
        }

        public String getFriendlyValue() {
            return friendlyValue;
        }

        public static class Builder {
            private String fromValue;
            private String toValue;
            private Integer unitOfMeasure;
            private String value;
            private String friendlyValue;
            private String source;

            public Builder setFromValue(String fromValue) {
                this.fromValue = fromValue;
                return this;
            }

            public Builder setToValue(String toValue) {
                this.toValue = toValue;
                return this;
            }

            public Builder setUnitOfMeasure(Integer unitOfMeasure) {
                this.unitOfMeasure = unitOfMeasure;
                return this;
            }

            public Builder setValue(String value) {
                this.value = value;
                return this;
            }

            public Builder setFriendlyValue(String friendlyValue) {
                this.friendlyValue = friendlyValue;
                return this;
            }


            public HealthAttributeRecommendationItem.Recommendation build() {
                return new HealthAttributeRecommendationItem.Recommendation(fromValue, toValue, unitOfMeasure, value, friendlyValue);
            }
        }
    }

    public static class Builder {

        List<HealthAttributeRecommendationItem.Recommendation> recommendations;
        String measuredOn, eventDateLogged, friendlyValue;
        List<FeedbackItem> feedbacks;
        int sectionTypeKey;
        private String source;


        public Builder setRecommendations(List<HealthAttributeRecommendationItem.Recommendation> recommendations) {
            this.recommendations = recommendations;
            return this;
        }

        public Builder setMeasuredOn(String measuredOn) {
            this.measuredOn = measuredOn;
            return this;
        }

        public Builder setFeedbacks(List<FeedbackItem> feedbacks) {
            this.feedbacks = feedbacks;
            return this;
        }

        public Builder setEventDateLogged(String eventDateLogged) {
            this.eventDateLogged = eventDateLogged;
            return this;
        }

        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        public Builder setSectionTypeKey(int sectionTypeKey) {
            this.sectionTypeKey = sectionTypeKey;
            return this;
        }


        public HealthAttributeRecommendationItem build() {
            return new HealthAttributeRecommendationItem(recommendations, feedbacks, measuredOn, eventDateLogged, friendlyValue, source, sectionTypeKey);
        }
    }


}
