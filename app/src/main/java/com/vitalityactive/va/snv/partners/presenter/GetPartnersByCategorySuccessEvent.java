package com.vitalityactive.va.snv.partners.presenter;

import com.vitalityactive.va.snv.partners.service.GetPartnersByCategoryResponse;

/**
 * Created by kerry.e.lawagan on 12/8/2017.
 */

public class GetPartnersByCategorySuccessEvent {
    private GetPartnersByCategoryResponse response;

    public GetPartnersByCategorySuccessEvent(GetPartnersByCategoryResponse response) {
        this.response = response;
    }

    public GetPartnersByCategoryResponse getResponse() {
        return response;
    }

    public void setResponse(GetPartnersByCategoryResponse response) {
        this.response = response;
    }
}
