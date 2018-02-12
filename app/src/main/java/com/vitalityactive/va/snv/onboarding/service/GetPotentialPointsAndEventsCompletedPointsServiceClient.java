package com.vitalityactive.va.snv.onboarding.service;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.VitalityMembershipRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.dto.FeatureLinkDto;
import com.vitalityactive.va.dto.MembershipProductDto;
import com.vitalityactive.va.dto.ProductFeatureDto;
import com.vitalityactive.va.dto.VitalityMembershipDto;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.persistence.models.screeningsandvaccinations.GetPotentialPointsAndEventsCompletedPointsFeedback;
import com.vitalityactive.va.snv.onboarding.interactor.ScreeningsAndVaccinationsInteractor;
import com.vitalityactive.va.snv.onboarding.presenter.GetPotentialPointsAndEventsCompletedPointFailedEvent;
import com.vitalityactive.va.snv.onboarding.presenter.GetPotentialPointsAndEventsCompletedPointSuccessEvent;
import com.vitalityactive.va.snv.onboarding.repository.ScreeningsAndVaccinationsRepositoy;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

/**
 * Created by kerry.e.lawagan on 11/24/2017.
 */

public class GetPotentialPointsAndEventsCompletedPointsServiceClient implements WebServiceResponseParser<GetPotentialPointsAndEventsCompletedPointsResponse> {
    private AccessTokenAuthorizationProvider accessToken;
    private EventDispatcher eventDispatcher;
    private Call<GetPotentialPointsAndEventsCompletedPointsResponse> request;
    private WebServiceClient webServiceClient;
    private GetPotentialPointsAndEventsCompletedPointsService service;
    private ScreeningsAndVaccinationsRepositoy repository;

    private PartyInformationRepository partyInformationRepository;
    private VitalityMembershipRepository vitalityMembershipRepository;

    @Inject
    public GetPotentialPointsAndEventsCompletedPointsServiceClient(WebServiceClient webServiceClient,
                                        GetPotentialPointsAndEventsCompletedPointsService service,
                                        ScreeningsAndVaccinationsRepositoy repository,
                                        EventDispatcher eventDispatcher,
                                        AccessTokenAuthorizationProvider accessToken,
                                        PartyInformationRepository partyInformationRepository,
                                        VitalityMembershipRepository vitalityMembershipRepository) {
        this.webServiceClient = webServiceClient;
        this.service = service;
        this.repository = repository;
        this.eventDispatcher = eventDispatcher;
        this.accessToken = accessToken;
        this.partyInformationRepository = partyInformationRepository;
        this.vitalityMembershipRepository = vitalityMembershipRepository;
    }

    private GetPotentialPointsAndEventsCompletedPointsRequest generateRequest() {
        List<Integer> eventTypeses = new ArrayList<Integer>();
        List<VitalityMembershipDto> vitalityMembershipDtoList = vitalityMembershipRepository.getVitalityMembership();
        Long membershipId = null;
        if (vitalityMembershipDtoList != null && !vitalityMembershipDtoList.isEmpty()) {
            if (vitalityMembershipDtoList.get(0).membershipProducts != null) {
                membershipId = vitalityMembershipDtoList.get(0).id;
                for(MembershipProductDto membershipProductDto: vitalityMembershipDtoList.get(0).membershipProducts) {
                    if (membershipProductDto.productFeatures != null) {
                        for(ProductFeatureDto productFeatureDto: membershipProductDto.productFeatures) {
                            if(productFeatureDto.getFeatureType() == 34 || productFeatureDto.getFeatureType() == 35) {
                                if (productFeatureDto.getFeatureLinks() != null) {
                                    for(FeatureLinkDto featureLinkDto: productFeatureDto.getFeatureLinks()) {
                                        if (featureLinkDto.getTypeKey() == 1) {
                                            if (!eventTypeses.contains(featureLinkDto.getLinkedKey())) {
                                                eventTypeses.add(featureLinkDto.getLinkedKey());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Integer[] eventTypesesArr = new Integer[eventTypeses.size()];
        for(int i=0; i<eventTypeses.size(); i++) {
            eventTypesesArr[i] = eventTypeses.get(i);
        }
        return new GetPotentialPointsAndEventsCompletedPointsRequest(eventTypesesArr, membershipId);
    }
    public void invokeApi() {
        request = service.getPotentialPointsAndEventsCompletedPointsRequest(accessToken.getAuthorization(),
                partyInformationRepository.getTenantId(),
                partyInformationRepository.getPartyId(),
                generateRequest());
        webServiceClient.executeAsynchronousRequest(request, this);
        //mockResponse(this);
    }

    private void mockResponse(WebServiceResponseParser<GetPotentialPointsAndEventsCompletedPointsResponse> parser) {
        GetPotentialPointsAndEventsCompletedPointsResponse response = new GetPotentialPointsAndEventsCompletedPointsResponse();

        List<GetPotentialPointsAndEventsCompletedPointsResponse.EventType> eventTypeList = new ArrayList<>();
        List<GetPotentialPointsAndEventsCompletedPointsResponse.Event> events = new ArrayList<>();

        GetPotentialPointsAndEventsCompletedPointsResponse.EventType eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Screening");
        eventType.setCategoryKey(23);
        eventType.setCategoryName("Screening");
        eventType.setTotalPotentialPoints(2500);
        eventType.setTypeCode("Pap Smear");
        eventType.setTypeKey(106);
        eventType.setTypeName("Pap Smear");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Screening");
        eventType.setCategoryKey(23);
        eventType.setCategoryName("Screening");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("Gastric Cancer");
        eventType.setTypeKey(106);
        eventType.setTypeName("Gastric Cancer");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Screening");
        eventType.setCategoryKey(23);
        eventType.setCategoryName("Screening");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("Glaucoma");
        eventType.setTypeKey(106);
        eventType.setTypeName("Glaucoma");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Screening");
        eventType.setCategoryKey(23);
        eventType.setCategoryName("Screening");
        eventType.setTotalPotentialPoints(5000);
        eventType.setTypeCode("HIV");
        eventType.setTypeKey(106);
        eventType.setTypeName("HIV");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Screening");
        eventType.setCategoryKey(23);
        eventType.setCategoryName("Screening");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("Dental Checkups");
        eventType.setTypeKey(106);
        eventType.setTypeName("Dental Checkups");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Screening");
        eventType.setCategoryKey(23);
        eventType.setCategoryName("Screening");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("FOBT");
        eventType.setTypeKey(106);
        eventType.setTypeName("FOBT");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Screening");
        eventType.setCategoryKey(23);
        eventType.setCategoryName("Screening");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("Colonoscopy");
        eventType.setTypeKey(106);
        eventType.setTypeName("Colonoscopy");
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Screening");
        eventType.setCategoryKey(23);
        eventType.setCategoryName("Screening");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("Skin Cancer");
        eventType.setTypeKey(106);
        eventType.setTypeName("Skin Cancer");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Screening");
        eventType.setCategoryKey(23);
        eventType.setCategoryName("Screening");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("Lung Cancer");
        eventType.setTypeKey(106);
        eventType.setTypeName("Lung Cancer");
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Screening");
        eventType.setCategoryKey(23);
        eventType.setCategoryName("Screening");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("Cardiovascular");
        eventType.setTypeKey(106);
        eventType.setTypeName("Cardiovascular");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Screening");
        eventType.setCategoryKey(23);
        eventType.setCategoryName("Screening");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("Ovarian Cancer");
        eventType.setTypeKey(106);
        eventType.setTypeName("Ovarian Cancer");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Screening");
        eventType.setCategoryKey(23);
        eventType.setCategoryName("Screening");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("Liver Cancer");
        eventType.setTypeKey(106);
        eventType.setTypeName("Liver Cancer");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Vaccination");
        eventType.setCategoryKey(24);
        eventType.setCategoryName("Vaccination");
        eventType.setTotalPotentialPoints(2500);
        eventType.setTypeCode("Flu");
        eventType.setTypeKey(106);
        eventType.setTypeName("Flu");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Vaccination");
        eventType.setCategoryKey(24);
        eventType.setCategoryName("Vaccination");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("Childhood");
        eventType.setTypeKey(106);
        eventType.setTypeName("Childhood");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Vaccination");
        eventType.setCategoryKey(24);
        eventType.setCategoryName("Vaccination");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("HPV");
        eventType.setTypeKey(106);
        eventType.setTypeName("HPV");
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Vaccination");
        eventType.setCategoryKey(24);
        eventType.setCategoryName("Vaccination");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("Pneumococcal");
        eventType.setTypeKey(106);
        eventType.setTypeName("Pneumococcal");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Vaccination");
        eventType.setCategoryKey(24);
        eventType.setCategoryName("Vaccination");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("Zoster");
        eventType.setTypeKey(106);
        eventType.setTypeName("Zoster");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Vaccination");
        eventType.setCategoryKey(24);
        eventType.setCategoryName("Vaccination");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("Hepatitis B");
        eventType.setTypeKey(106);
        eventType.setTypeName("Hepatitis B");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        eventType = new GetPotentialPointsAndEventsCompletedPointsResponse.EventType();
        eventType.setCategoryCode("Vaccination");
        eventType.setCategoryKey(24);
        eventType.setCategoryName("Vaccination");
        eventType.setTotalPotentialPoints(2000);
        eventType.setTypeCode("Meningcococcal");
        eventType.setTypeKey(106);
        eventType.setTypeName("Meningcococcal");
        eventType.setEvent(events);
        eventTypeList.add(eventType);

        response.setEventType(eventTypeList);

        parser.parseResponse(response);

    }

    @Override
    public void parseResponse(GetPotentialPointsAndEventsCompletedPointsResponse response) {
        repository.persistGetPotentialPointsAndEventsCompletedPointsResponse(new GetPotentialPointsAndEventsCompletedPointsFeedback((response)));
        eventDispatcher.dispatchEvent(new GetPotentialPointsAndEventsCompletedPointSuccessEvent(response));
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        eventDispatcher.dispatchEvent(new GetPotentialPointsAndEventsCompletedPointFailedEvent(ScreeningsAndVaccinationsInteractor.GetPotentialPointsAndEventsCompletedPointsResult.GENERIC_ERROR));
    }

    @Override
    public void handleGenericError(Exception exception) {
        eventDispatcher.dispatchEvent(new GetPotentialPointsAndEventsCompletedPointFailedEvent(ScreeningsAndVaccinationsInteractor.GetPotentialPointsAndEventsCompletedPointsResult.GENERIC_ERROR));
    }

    @Override
    public void handleConnectionError() {
        eventDispatcher.dispatchEvent(new GetPotentialPointsAndEventsCompletedPointFailedEvent(ScreeningsAndVaccinationsInteractor.GetPotentialPointsAndEventsCompletedPointsResult.CONNECTION_ERROR));
    }
}
