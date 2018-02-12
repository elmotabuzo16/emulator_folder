package com.vitalityactive.va.snv.dto;

/**
 * Created by kerry.e.lawagan on 11/27/2017.
 */

public class ReasonDto {
    private String categoryCode;
    private int categoryKey;
    private String categoryName;
    private String reasonCode;
    private int reasonKey;
    private String reasonName;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public int getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(int categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public int getReasonKey() {
        return reasonKey;
    }

    public void setReasonKey(int reasonKey) {
        this.reasonKey = reasonKey;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }
}
