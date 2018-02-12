package com.vitalityactive.va.snv.partners.service;

/**
 * Created by kerry.e.lawagan on 12/8/2017.
 */

public class GetPartnersByCategoryRequest {
    public String effectiveDate;
    public int productFeatureCategoryTypeKey;

    public GetPartnersByCategoryRequest(String effectiveDate, int productFeatureCategoryTypeKey) {
        this.effectiveDate = effectiveDate;
        this.productFeatureCategoryTypeKey = productFeatureCategoryTypeKey;
    }
}
