package com.vitalityactive.va.home.events;

import com.vitalityactive.va.events.BaseEventWithResponse;
import com.vitalityactive.va.networking.RequestResult;

public class GetProductFeaturePointsResponseEvent extends BaseEventWithResponse<ProductFeaturePointsResponse> {
    public GetProductFeaturePointsResponseEvent(RequestResult requestResult) {
        super(requestResult);
    }

    public GetProductFeaturePointsResponseEvent(RequestResult requestResult, ProductFeaturePointsResponse responseBody) {
        super(requestResult, responseBody);
    }
}
