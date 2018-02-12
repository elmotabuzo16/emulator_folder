package com.vitalityactive.va.myhealth.content;

import java.util.List;

public class ViewHolderFeedbackTip {

    public String sectionTitle;
    public int sectionSortOrder;
    public int sectionIcon;
    public String tintColor;
    public int typekey;

    List<VitalityAgeTip> vitalityAgeTipList;

    public ViewHolderFeedbackTip() {
    }

    public ViewHolderFeedbackTip(String sectionTitle, int sectionSortOrder, int sectionIcon, String tintColor, int typekey, List<VitalityAgeTip> vitalityAgeTipList) {
        this.sectionTitle = sectionTitle;
        this.sectionSortOrder = sectionSortOrder;
        this.sectionIcon = sectionIcon;
        this.tintColor = tintColor;
        this.typekey = typekey;
        this.vitalityAgeTipList = vitalityAgeTipList;
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

    public int getTypeKey() {
        return typekey;
    }

    public void setTypekey(int typekey) {
        this.typekey = typekey;
    }

    public List<VitalityAgeTip> getVitalityAgeTipList() {
        return vitalityAgeTipList;
    }

    public void setVitalityAgeTipList(List<VitalityAgeTip> vitalityAgeTipList) {
        this.vitalityAgeTipList = vitalityAgeTipList;
    }
}
