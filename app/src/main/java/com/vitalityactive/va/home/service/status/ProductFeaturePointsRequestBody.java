package com.vitalityactive.va.home.service.status;

import com.google.gson.annotations.SerializedName;

public class ProductFeaturePointsRequestBody {
    @SerializedName("effectiveDate")
    public String effectiveDate;
    @SerializedName("productFeatureCategoryTypeKey")
    public int productFeatureCategoryTypeKey;

    public ProductFeaturePointsRequestBody(int earnpointsmobile) {
        productFeatureCategoryTypeKey = earnpointsmobile;
    }
}
