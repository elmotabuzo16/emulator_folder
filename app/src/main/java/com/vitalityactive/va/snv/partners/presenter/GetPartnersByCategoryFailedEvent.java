package com.vitalityactive.va.snv.partners.presenter;

import com.vitalityactive.va.snv.shared.SnvConstants;

/**
 * Created by kerry.e.lawagan on 12/8/2017.
 */

public class GetPartnersByCategoryFailedEvent {
    private SnvConstants.SnvApiResult result;

    public GetPartnersByCategoryFailedEvent(SnvConstants.SnvApiResult result) {
        this.result = result;
    }

    public SnvConstants.SnvApiResult getResult() {
        return result;
    }

    public void setResult(SnvConstants.SnvApiResult result) {
        this.result = result;
    }
}
