package com.vitalityactive.va.partnerjourney.service;

import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.partnerjourney.models.PartnerGroup;

import java.util.ArrayList;
import java.util.List;

public class PartnerListRequestEvent {
    public final RequestResult result;
    public List<PartnerGroup> list = new ArrayList<>();

    public PartnerListRequestEvent(RequestResult result) {
        this.result = result;
    }

    public PartnerListRequestEvent(List<PartnerGroup> list) {
        this(RequestResult.SUCCESSFUL);
        this.list = list;
    }
}
