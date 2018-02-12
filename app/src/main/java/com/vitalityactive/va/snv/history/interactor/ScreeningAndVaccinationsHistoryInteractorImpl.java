package com.vitalityactive.va.snv.history.interactor;

import android.content.Context;

import com.vitalityactive.va.persistence.models.screeningsandvaccinations.ScreeningVaccinationAssociatedEvents;
import com.vitalityactive.va.snv.dto.HistoryDetailDto;
import com.vitalityactive.va.snv.dto.ListHistoryListDto;
import com.vitalityactive.va.snv.history.repository.ScreenAndVaccinationHistoryRepository;
import com.vitalityactive.va.snv.history.service.ScreenAndVaccinationHistoryServiceClient;
import com.vitalityactive.va.snv.history.views.model.SNVHistoryPresentableProof;
import com.vitalityactive.va.vhc.summary.PresentableProof;

import java.util.List;

/**
 * Created by dharel.h.rosell on 12/1/2017.
 */

public class ScreeningAndVaccinationsHistoryInteractorImpl implements ScreeningAndVaccinationsHistoryInteractor {
    private ScreenAndVaccinationHistoryServiceClient serviceClient;
    private ScreenAndVaccinationHistoryRepository repository;
    private Context context;

    public ScreeningAndVaccinationsHistoryInteractorImpl(ScreenAndVaccinationHistoryServiceClient serviceClient, ScreenAndVaccinationHistoryRepository repository) {
        this.serviceClient = serviceClient;
        this.repository = repository;
    }

    @Override
    public void fetchData() {
        serviceClient.invokeApi(context);
    }

    @Override
    public void setCcontext(Context context) {
        this.context = context;
    }

    @Override
    public List<HistoryDetailDto> getResponseData() {
        return repository.getScreeningAndVaccinationHistoryList();
    }

    @Override
    public List<ListHistoryListDto> getHistoryListDto() {
        return repository.getHistoryListDto();
    }

//    @Override
//    public PresentableProof getSNVHistoryPresentableProof() {
//        return repository.getSNVPresentableProof();
//    }


    @Override
    public SNVHistoryPresentableProof getSNVHistoryPresentableProof() {
        return repository.getSNVPresentableProof();
    }

    @Override
    public List<ScreeningVaccinationAssociatedEvents> getAssociatedEvents() {
        return repository.getAssociatedEvents();
    }
}
