package com.vitalityactive.va.partnerjourney.service.models;

import com.google.gson.annotations.SerializedName;

public class PartnerDetailResponse {
    @SerializedName("productFeatureEligibilityContent")
    public ProductFeatureEligibilityContent productFeatureEligibilityContent;

    public class ProductFeatureEligibilityContent {
        @SerializedName("content")
        public String mainContent;

        @SerializedName("partnerURL")
        public String url;
    }
}
