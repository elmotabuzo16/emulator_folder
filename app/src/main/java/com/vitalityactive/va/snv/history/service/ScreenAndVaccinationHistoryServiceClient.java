package com.vitalityactive.va.snv.history.service;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.VitalityMembershipRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.dto.FeatureLinkDto;
import com.vitalityactive.va.dto.MembershipProductDto;
import com.vitalityactive.va.dto.ProductFeatureDto;
import com.vitalityactive.va.dto.VitalityMembershipDto;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.home.service.EventByPartyResponse;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.persistence.models.screeningsandvaccinations.ScreeningVaccinationEventByParty;
import com.vitalityactive.va.snv.history.interactor.ScreeningAndVaccinationsHistoryInteractor;
import com.vitalityactive.va.snv.history.presenter.GetScreenAndVaccinationHistoryFailEvent;
import com.vitalityactive.va.snv.history.presenter.GetScreenAndVaccinationHistorySuccessEvent;
import com.vitalityactive.va.snv.history.repository.ScreenAndVaccinationHistoryRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

/**
 * Created by dharel.h.rosell on 11/30/2017.
 */

public class ScreenAndVaccinationHistoryServiceClient implements WebServiceResponseParser<EventByPartyResponse>{
    private AccessTokenAuthorizationProvider accessToken;
    private EventDispatcher eventDispatcher;
    private WebServiceClient webServiceClient;
    private ScreenAndVaccinationHistoryRepository repository;
    private ScreeningAndVaccinationHistoryService service;
    private ScreeningAndVaccinationHistoryRequest request;
    Call<EventByPartyResponse> response;

    private PartyInformationRepository partyInformationRepository;
    private VitalityMembershipRepository vitalityMembershipRepository;

    private Context context;

    @Inject
    public ScreenAndVaccinationHistoryServiceClient(AccessTokenAuthorizationProvider accessToken,
                                                    EventDispatcher eventDispatcher,
                                                    WebServiceClient webServiceClient,
                                                    ScreenAndVaccinationHistoryRepository repository,
                                                    ScreeningAndVaccinationHistoryService service,
                                                    PartyInformationRepository partyInformationRepository,
                                                    VitalityMembershipRepository vitalityMembershipRepository) {
        this.accessToken = accessToken;
        this.eventDispatcher = eventDispatcher;
        this.webServiceClient = webServiceClient;
        this.repository = repository;
        this.service = service;
        this.partyInformationRepository = partyInformationRepository;
        this.vitalityMembershipRepository = vitalityMembershipRepository;

    }

    public void invokeApi(Context context) {
        response = service.getScreenAndVaccinationHistory(accessToken.getAuthorization(),
                partyInformationRepository.getTenantId(),
                partyInformationRepository.getPartyId(), generateRequest());
        webServiceClient.executeAsynchronousRequest(response, this);
        this.context = context;
//        mockResponse(context, this);
    }

    private ScreeningAndVaccinationHistoryRequest generateRequest(){
        List<Integer> eventTypeses = new ArrayList<Integer>();
        List<VitalityMembershipDto> vitalityMembershipDtoList = vitalityMembershipRepository.getVitalityMembership();
        String effectiveFrom = "";
        String effectiveTo = "";
        if (vitalityMembershipDtoList != null && !vitalityMembershipDtoList.isEmpty()) {
            if (vitalityMembershipDtoList.get(0).membershipProducts != null) {
                effectiveFrom = vitalityMembershipDtoList.get(0).currentVitalityMembershipPeriod.getEffectiveFrom();
                effectiveTo = vitalityMembershipDtoList.get(0).currentVitalityMembershipPeriod.getEffectiveTo();
                eventTypeses.add(EventType._DOCUMENTUPLOADSSV);
//                for(MembershipProductDto membershipProductDto: vitalityMembershipDtoList.get(0).membershipProducts) {
//                    if (membershipProductDto.productFeatures != null) {
//                        for(ProductFeatureDto productFeatureDto: membershipProductDto.productFeatures) {
//                            if(productFeatureDto.getFeatureType() == 34 || productFeatureDto.getFeatureType() == 35) {
//                                if (productFeatureDto.getFeatureLinks() != null) {
//                                    for(FeatureLinkDto featureLinkDto: productFeatureDto.getFeatureLinks()) {
//                                        if (featureLinkDto.getTypeKey() == 1) {
//                                            if (!eventTypeses.contains(featureLinkDto.getLinkedKey())) {
//                                                eventTypeses.add(featureLinkDto.getLinkedKey());
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
            }
        }
        Log.d("REQUEST: ", "Effective from: " + effectiveFrom + " Effective To: " + effectiveTo);
        Integer[] eventTypesesArr = new Integer[eventTypeses.size()];
        for(int i=0; i<eventTypeses.size(); i++) {
            eventTypesesArr[i] = eventTypeses.get(i);
            Log.d("TypeKeys: ", String.valueOf(eventTypesesArr[i]));
        }

        return new ScreeningAndVaccinationHistoryRequest(effectiveFrom, effectiveTo, eventTypesesArr);
    }

    @Override
    public void parseResponse(EventByPartyResponse response) {
            repository.persisScreenAndVaccinationHistoryListResponse(new ScreeningVaccinationEventByParty(response));
            eventDispatcher.dispatchEvent(new GetScreenAndVaccinationHistorySuccessEvent(response));
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
            eventDispatcher.dispatchEvent(new GetScreenAndVaccinationHistoryFailEvent(
                    ScreeningAndVaccinationsHistoryInteractor
                            .ScreeningAndVaccinationsHistoryResult.GENERIC_ERROR));
    }

    @Override
    public void handleGenericError(Exception exception) {
            eventDispatcher.dispatchEvent(new GetScreenAndVaccinationHistoryFailEvent(
                ScreeningAndVaccinationsHistoryInteractor
                        .ScreeningAndVaccinationsHistoryResult.GENERIC_ERROR));
    }

    @Override
    public void handleConnectionError() {
        eventDispatcher.dispatchEvent(new GetScreenAndVaccinationHistoryFailEvent(
                ScreeningAndVaccinationsHistoryInteractor
                        .ScreeningAndVaccinationsHistoryResult.CONNECTION_ERROR));
    }

    private void mockResponse(Context context, WebServiceResponseParser<EventByPartyResponse> parser) {
            ScreeningAndVaccinationHistoryStub stubResponse = new ScreeningAndVaccinationHistoryStub(context);
        try {
            parser.parseResponse(stubResponse.getResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
