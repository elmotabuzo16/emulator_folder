package com.vitalityactive.va.partnerjourney.service;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.activerewards.landing.service.EffectiveDate;

class PartnerListRequest {
    @SerializedName("effectiveDate")
    EffectiveDate effectiveDate;
    @SerializedName("productFeatureCategoryTypeKey")
    int productFeatureCategoryTypeKey;
}
