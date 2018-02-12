package com.vitalityactive.va.partnerjourney.service;

import com.google.gson.annotations.SerializedName;

class PartnerDetailRequest {
    @SerializedName("productEligibilityContentIn")
    ProductEligibilityContent productEligibilityContentIn;

    public PartnerDetailRequest(long partnerId) {
        productEligibilityContentIn = new ProductEligibilityContent(partnerId);
    }

    private class ProductEligibilityContent {
        @SerializedName("productFeatureKey")
        long productFeatureKey;

        public ProductEligibilityContent(long partnerId) {
            productFeatureKey = partnerId;
        }
    }
}
