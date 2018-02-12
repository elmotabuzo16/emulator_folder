package com.vitalityactive.va.snv.dto;

import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/27/2017.
 */

public class PointsEntryDto {
    private String categoryCode;
    private int categorykey;
    private String categoryName;
    private int earnedValue;
    private int id;
    private int potentialValue;
    private int preLimitValue;
    private List<ReasonDto> reason;
    private String statusChangeDate;
    private String statusTypeCode;
    private int statusTypeKey;
    private String statusTypeName;
    private String typeCode;
    private int typeKey;
    private String typeName;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public int getCategorykey() {
        return categorykey;
    }

    public void setCategorykey(int categorykey) {
        this.categorykey = categorykey;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getEarnedValue() {
        return earnedValue;
    }

    public void setEarnedValue(int earnedValue) {
        this.earnedValue = earnedValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPotentialValue() {
        return potentialValue;
    }

    public void setPotentialValue(int potentialValue) {
        this.potentialValue = potentialValue;
    }

    public int getPreLimitValue() {
        return preLimitValue;
    }

    public void setPreLimitValue(int preLimitValue) {
        this.preLimitValue = preLimitValue;
    }

    public List<ReasonDto> getReason() {
        return reason;
    }

    public void setReason(List<ReasonDto> reason) {
        this.reason = reason;
    }

    public String getStatusChangeDate() {
        return statusChangeDate;
    }

    public void setStatusChangeDate(String statusChangeDate) {
        this.statusChangeDate = statusChangeDate;
    }

    public String getStatusTypeCode() {
        return statusTypeCode;
    }

    public void setStatusTypeCode(String statusTypeCode) {
        this.statusTypeCode = statusTypeCode;
    }

    public int getStatusTypeKey() {
        return statusTypeKey;
    }

    public void setStatusTypeKey(int statusTypeKey) {
        this.statusTypeKey = statusTypeKey;
    }

    public String getStatusTypeName() {
        return statusTypeName;
    }

    public void setStatusTypeName(String statusTypeName) {
        this.statusTypeName = statusTypeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(int typeKey) {
        this.typeKey = typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
