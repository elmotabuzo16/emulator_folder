package com.vitalityactive.va.snv.partners.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kerry.e.lawagan on 12/8/2017.
 */

public class GetPartnersByCategoryResponse {
    @SerializedName("productFeatureGroups")
    private List<ProductFeatureGroup> productFeatureGroups;
    @SerializedName("typeCode")
    private String typeCode;
    @SerializedName("typeKey")
    private int typeKey;
    @SerializedName("typeName")
    private String typeName;

    public List<ProductFeatureGroup> getProductFeatureGroups() {
        return productFeatureGroups;
    }

    public void setProductFeatureGroups(List<ProductFeatureGroup> productFeatureGroups) {
        this.productFeatureGroups = productFeatureGroups;
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

    public static class ProductFeatureGroup {
        @SerializedName("description")
        private String description;
        @SerializedName("logoFileName")
        private String logoFileName;
        @SerializedName("longDescription")
        private String longDescription;
        @SerializedName("name")
        private String name;
        @SerializedName("typeCode")
        private String typeCode;
        @SerializedName("typeKey")
        private int typeKey;
        @SerializedName("typeName")
        private String typeName;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLogoFileName() {
            return logoFileName;
        }

        public void setLogoFileName(String logoFileName) {
            this.logoFileName = logoFileName;
        }

        public String getLongDescription() {
            return longDescription;
        }

        public void setLongDescription(String longDescription) {
            this.longDescription = longDescription;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
}
