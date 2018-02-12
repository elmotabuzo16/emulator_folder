package com.vitalityactive.va.myhealth.content;


import java.util.List;

public class SectionItem {

    public String sectionTitle;
    public int sectionSortOrder;
    public int sectionIcon;
    public String tintColor;
    public int sectionTypekey;
    public boolean hasSubsection;
    List<AttributeItem> attributeItems;

    public SectionItem(String sectionTitle, int sectionSortOrder, int sectionIcon, String tintColor, int sectionTypekey, boolean hasSubsection, List<AttributeItem> attributeItems) {
        this.sectionTitle = sectionTitle;
        this.sectionSortOrder = sectionSortOrder;
        this.sectionIcon = sectionIcon;
        this.tintColor = tintColor;
        this.sectionTypekey = sectionTypekey;
        this.hasSubsection = hasSubsection;
        this.attributeItems = attributeItems;
    }

    public void setHasSubsection(boolean hasSubsection) {
        this.hasSubsection = hasSubsection;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public int getSectionSortOrder() {
        return sectionSortOrder;
    }

    public int getSectionIcon() {
        return sectionIcon;
    }

    public String getTintColor() {
        return tintColor;
    }

    public int getSectionTypekey() {
        return sectionTypekey;
    }

    public boolean hasSubsection() {
        return hasSubsection;
    }

    public List<AttributeItem> getAttributeItems() {
        return attributeItems;
    }

    public static class Builder {
        List<AttributeItem> attributeItems;
        private String sectionTitle;
        private int sectionSortOrder;
        private int sectionIcon;
        private String tintColor;
        private int sectionTypekey;
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

        public Builder setSectionTypekey(int sectionTypekey) {
            this.sectionTypekey = sectionTypekey;
            return this;
        }

        public Builder setAttributeItems(List<AttributeItem> attributeItems) {
            this.attributeItems = attributeItems;
            return this;
        }

        public Builder setHasSubsection(boolean hasSubsection) {
            this.hasSubsection = hasSubsection;
            return this;
        }

        public SectionItem build() {
            return new SectionItem(sectionTitle, sectionSortOrder, sectionIcon, tintColor, sectionTypekey, hasSubsection, attributeItems);
        }
    }
}
