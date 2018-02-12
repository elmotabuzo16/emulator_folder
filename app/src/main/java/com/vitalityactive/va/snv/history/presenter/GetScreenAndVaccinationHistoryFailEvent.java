package com.vitalityactive.va.snv.history.presenter;

import com.vitalityactive.va.snv.history.interactor.ScreeningAndVaccinationsHistoryInteractor;

/**
 * Created by dharel.h.rosell on 12/1/2017.
 */

public class GetScreenAndVaccinationHistoryFailEvent {
    ScreeningAndVaccinationsHistoryInteractor.ScreeningAndVaccinationsHistoryResult result;

    public GetScreenAndVaccinationHistoryFailEvent(ScreeningAndVaccinationsHistoryInteractor
                                                   .ScreeningAndVaccinationsHistoryResult result) {
        this.result = result;
    }
}
