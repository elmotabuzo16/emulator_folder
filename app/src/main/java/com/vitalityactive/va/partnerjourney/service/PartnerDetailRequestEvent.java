package com.vitalityactive.va.partnerjourney.service;

import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.partnerjourney.models.PartnerItemDetails;

public class PartnerDetailRequestEvent {
    public final RequestResult result;
    public PartnerItemDetails details = null;

    PartnerDetailRequestEvent(RequestResult result) {
        this.result = result;
    }

    PartnerDetailRequestEvent(PartnerItemDetails details) {
        this(RequestResult.SUCCESSFUL);
        this.details = details;
    }
}
