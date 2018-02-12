package com.vitalityactive.va.home.events;

import com.vitalityactive.va.events.BaseEventWithResponse;
import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;
import com.vitalityactive.va.networking.RequestResult;

public class GetCardCollectionResponseEvent extends BaseEventWithResponse<HomeScreenCardStatusResponse> {
    public GetCardCollectionResponseEvent(RequestResult requestResult) {
        super(requestResult);
    }

    public GetCardCollectionResponseEvent(RequestResult requestResult, HomeScreenCardStatusResponse responseBody) {
        super(requestResult, responseBody);
    }
}
