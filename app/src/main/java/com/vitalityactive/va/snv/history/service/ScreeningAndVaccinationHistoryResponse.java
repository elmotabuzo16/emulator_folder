package com.vitalityactive.va.snv.history.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dharel.h.rosell on 11/30/2017.
 */

public class ScreeningAndVaccinationHistoryResponse {
    @SerializedName("history")
    private List<History> history;

    public List<History> getHistory() {
        return history;
    }

    public void setHistory(List<History> history) {
        this.history = history;
    }


    public static class History {
        @SerializedName("categoryCode")
        private String categoryCode;
        @SerializedName("categoryKey")
        private int categoryKey;
        @SerializedName("categoryName")
        private String categoryName;
        @SerializedName("typeCode")
        private String typeCode;
        @SerializedName("typeKey")
        private int typeKey;
        @SerializedName("typeName")
        private String typeName;
        @SerializedName("date")
        private String date;

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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

}
