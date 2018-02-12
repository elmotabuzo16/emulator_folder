package com.vitalityactive.va.activerewards.rewards.service;

import com.google.gson.annotations.SerializedName;

class PartnersServiceRequest {
    @SerializedName("productFeatureCategoryTypeKey")
    private int productFeatureCategoryTypeKey;

    PartnersServiceRequest(int productFeatureCategoryTypeKey) {
        this.productFeatureCategoryTypeKey = productFeatureCategoryTypeKey;
    }
}
