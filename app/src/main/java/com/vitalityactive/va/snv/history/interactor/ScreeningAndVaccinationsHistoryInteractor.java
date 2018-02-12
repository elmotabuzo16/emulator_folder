package com.vitalityactive.va.snv.history.interactor;

import android.content.Context;

import com.vitalityactive.va.persistence.models.screeningsandvaccinations.ScreeningVaccinationAssociatedEvents;
import com.vitalityactive.va.snv.dto.HistoryDetailDto;
import com.vitalityactive.va.snv.dto.ListHistoryListDto;
import com.vitalityactive.va.snv.history.views.model.SNVHistoryPresentableProof;
import com.vitalityactive.va.vhc.summary.PresentableProof;

import java.util.List;

/**
 * Created by dharel.h.rosell on 12/1/2017.
 */

public interface ScreeningAndVaccinationsHistoryInteractor {
    void fetchData();
    void setCcontext(Context context);
    List<HistoryDetailDto> getResponseData();
    List<ListHistoryListDto> getHistoryListDto();
    SNVHistoryPresentableProof getSNVHistoryPresentableProof();
    List<ScreeningVaccinationAssociatedEvents> getAssociatedEvents();
//    PresentableProof getSNVHistoryPresentableProof();


    enum ScreeningAndVaccinationsHistoryResult {
        CONNECTION_ERROR,
        GENERIC_ERROR,
        SUCCESSFUL
    }

}
