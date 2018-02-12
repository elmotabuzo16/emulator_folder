package com.vitalityactive.va.myhealth.content;

import java.util.List;

public class FeedbackTipItem {

    public String sectionTitle;
    public int sectionSortOrder;
    public int sectionIcon;
    public String tintColor;
    public int sectionTypekey;
    public boolean hasSubsection;

    List<FeedbackTip> feedbackTips;

    public FeedbackTipItem() {
    }

    public FeedbackTipItem(String sectionTitle, int sectionSortOrder, int sectionIcon, String tintColor, int sectionTypekey, List<FeedbackTip> feedbackTips, boolean hasSubsection) {
        this.sectionTitle = sectionTitle;
        this.sectionSortOrder = sectionSortOrder;
        this.sectionIcon = sectionIcon;
        this.tintColor = tintColor;
        this.sectionTypekey = sectionTypekey;
        this.feedbackTips = feedbackTips;
        this.hasSubsection = hasSubsection;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public int getSectionSortOrder() {
        return sectionSortOrder;
    }

    public void setSectionSortOrder(int sectionSortOrder) {
        this.sectionSortOrder = sectionSortOrder;
    }

    public int getSectionIcon() {
        return sectionIcon;
    }

    public void setSectionIcon(int sectionIcon) {
        this.sectionIcon = sectionIcon;
    }

    public String getTintColor() {
        return tintColor;
    }

    public void setTintColor(String tintColor) {
        this.tintColor = tintColor;
    }

    public int getSectionTypeKey() {
        return sectionTypekey;
    }

    public List<FeedbackTip> getFeedbackTips() {
        return feedbackTips;
    }

    public void setFeedbackTips(List<FeedbackTip> feedbackTips) {
        this.feedbackTips = feedbackTips;
    }

    public void setHasSubsection(boolean hasSubsection) {
        this.hasSubsection = hasSubsection;
    }

    public int getSectionTypekey() {
        return sectionTypekey;
    }

    public void setSectionTypekey(int sectionTypekey) {
        this.sectionTypekey = sectionTypekey;
    }

    public boolean hasSubsection() {
        return hasSubsection;
    }

    public static class Builder {
        private String sectionTitle;
        private int sectionSortOrder;
        private int sectionIcon;
        private String tintColor;
        private int typekey;
        private List<FeedbackTip> feedbackTips;
        private boolean hasSubsection;

        public Builder setSectionTitle(String sectionTitle) {
            this.sectionTitle = sectionTitle;
            return this;
        }

        public Builder setSectionSortOrder(int sectionSortOrder) {
            this.sectionSortOrder = sectionSortOrder;
            return this;
        }

        public Builder setSectionIcon(int sectionIcon) {
            this.sectionIcon = sectionIcon;
            return this;
        }

        public Builder setTintColor(String tintColor) {
            this.tintColor = tintColor;
            return this;
        }

        public Builder setTypekey(int typekey) {
            this.typekey = typekey;
            return this;
        }

        public Builder setHasSubsection(boolean hasSubsection) {
            this.hasSubsection = hasSubsection;
            return this;
        }

        public Builder setFeedbackTips(List<FeedbackTip> feedbackTips) {
            this.feedbackTips = feedbackTips;
            return this;
        }

        public FeedbackTipItem build() {
            return new FeedbackTipItem(sectionTitle, sectionSortOrder, sectionIcon, tintColor, typekey, feedbackTips, hasSubsection);
        }
    }
}
