package com.vitalityactive.va.snv.history.repository;

import com.vitalityactive.va.persistence.models.screeningsandvaccinations.ScreeningVaccinationAssociatedEvents;
import com.vitalityactive.va.persistence.models.screeningsandvaccinations.ScreeningVaccinationEventByParty;
import com.vitalityactive.va.snv.dto.HistoryDetailDto;
import com.vitalityactive.va.snv.dto.ListHistoryListDto;
import com.vitalityactive.va.snv.dto.ScreeningVaccinationMetadataDto;
import com.vitalityactive.va.snv.history.views.model.SNVHistoryPresentableProof;
import com.vitalityactive.va.vhc.summary.PresentableProof;

import java.util.List;

/**
 * Created by dharel.h.rosell on 11/30/2017.
 */

public interface ScreenAndVaccinationHistoryRepository {
    boolean persisScreenAndVaccinationHistoryListResponse(ScreeningVaccinationEventByParty reponseSVHistoryList);
    List<HistoryDetailDto> getScreeningAndVaccinationHistoryList();
    List<ListHistoryListDto> getHistoryListDto();
    ListHistoryListDto getHistoryListItem(String date);
    SNVHistoryPresentableProof getSNVPresentableProof();
    List<ScreeningVaccinationMetadataDto> getEventMetaData(String date);
    List<ScreeningVaccinationAssociatedEvents> getAssociatedEvents();
//    PresentableProof getSNVPresentableProof();

}
