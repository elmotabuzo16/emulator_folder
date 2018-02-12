package com.vitalityactive.va.partnerjourney.service.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PartnerListResponse {
    @SerializedName("productFeatureGroups")
    public List<ProductFeatureGroups> productFeatureGroups;

    public class ProductFeatureGroups {
        @SerializedName("name")
        public String name;
        @SerializedName("partnerProductFeatures")
        public List<PartnerResponse> productFeatures;
    }

    public class PartnerResponse {
        @SerializedName("typeKey")
        public Long typeKey;
        @SerializedName("name")
        public String name;
        @SerializedName("description")
        public String description;
        @SerializedName("logoFileName")
        public String logoFileName;
        @SerializedName("longDescription")
        public String longDescription;
    }
}
