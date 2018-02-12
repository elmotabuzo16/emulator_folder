package com.vitalityactive.va.home.events;

import com.vitalityactive.va.events.BaseEventWithResponse;
import com.vitalityactive.va.home.service.EventByPartyResponse;
import com.vitalityactive.va.networking.RequestResult;

public class GetEventStatusByPartyIdResponseEvent extends BaseEventWithResponse<EventByPartyResponse> {
    public GetEventStatusByPartyIdResponseEvent(RequestResult requestResult) {
        super(requestResult);
    }

    public GetEventStatusByPartyIdResponseEvent(RequestResult requestResult, EventByPartyResponse responseBody) {
        super(requestResult, responseBody);
    }
}
