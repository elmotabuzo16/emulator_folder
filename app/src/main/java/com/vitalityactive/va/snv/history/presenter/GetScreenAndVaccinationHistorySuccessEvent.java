package com.vitalityactive.va.snv.history.presenter;

import com.vitalityactive.va.home.service.EventByPartyResponse;
import com.vitalityactive.va.snv.history.service.ScreeningAndVaccinationHistoryResponse;

/**
 * Created by dharel.h.rosell on 12/1/2017.
 */

public class GetScreenAndVaccinationHistorySuccessEvent {
    EventByPartyResponse response;

    public GetScreenAndVaccinationHistorySuccessEvent(EventByPartyResponse response) {
        this.response = response;
    }
}
