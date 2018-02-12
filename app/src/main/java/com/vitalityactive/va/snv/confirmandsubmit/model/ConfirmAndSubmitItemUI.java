package com.vitalityactive.va.snv.confirmandsubmit.model;


public class ConfirmAndSubmitItemUI {

    public static final int SCREENING_TYPE = 0;
    public static final int VACCINATION_TYPE = 1;

    private String fieldTitle;
    private String dateTested;
    private boolean isEnabled;
    private int typeKey;
    private long dateTestedInLong;

    public ConfirmAndSubmitItemUI(String fieldTitle, String dateTested, boolean isEnabled, int typeKey, int dateTestedInLong) {
        this.fieldTitle = fieldTitle;
        this.dateTested = dateTested;
        this.isEnabled = isEnabled;
        this.typeKey = typeKey;
        this.dateTestedInLong = dateTestedInLong;
    }

    public long getDateTestedInLong() {
        return dateTestedInLong;
    }

    public void setDateTestedInLong(long dateTestedInLong) {
        this.dateTestedInLong = dateTestedInLong;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(int typeKey) {
        this.typeKey = typeKey;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    public String getDateTested() {
        return dateTested;
    }

    public void setDateTested(String dateTested) {
        this.dateTested = dateTested;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
