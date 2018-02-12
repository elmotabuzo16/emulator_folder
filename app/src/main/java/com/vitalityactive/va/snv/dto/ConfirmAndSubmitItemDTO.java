package com.vitalityactive.va.snv.dto;

import com.vitalityactive.va.persistence.models.ConfirmAndSubmitItem;

public class ConfirmAndSubmitItemDTO {
    private String fieldTitle = "";
    private String dateTested = "";
    private boolean isEnabled = false;
    private int type;
    private int typeKey;
    private long dateTestedInLong;

    public ConfirmAndSubmitItemDTO(ConfirmAndSubmitItem item) {
        this.fieldTitle = item.getFieldTitle();
        this.dateTested = item.getDateTested();
        this.isEnabled = item.isEnabled();
        this.type = item.getType();
        this.typeKey = item.getTypeKey();
        this.dateTestedInLong = item.getDateTestedinLong();
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDateTestedInLong() {
        return dateTestedInLong;
    }

    public void setDateTestedInLong(long dateTestedInLong) {
        this.dateTestedInLong = dateTestedInLong;
    }
}
