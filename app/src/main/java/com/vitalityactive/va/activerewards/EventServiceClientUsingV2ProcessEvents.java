package com.vitalityactive.va.activerewards;

import com.vitalityactive.va.EventService;
import com.vitalityactive.va.EventServiceClient;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.ProcessEventsServiceResponse;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.ProcessEventsV2ServiceRequest;
import com.vitalityactive.va.utilities.TimeUtilities;

import javax.inject.Inject;

import retrofit2.Call;

public class EventServiceClientUsingV2ProcessEvents extends EventServiceClient {
    private WebServiceClient webServiceClient;
    private EventService eventService;
    private PartyInformationRepository partyInformationRepository;
    private TimeUtilities timeUtilities;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    @Inject
    public EventServiceClientUsingV2ProcessEvents(WebServiceClient webServiceClient,
                                                  EventService eventService,
                                                  PartyInformationRepository partyInformationRepository,
                                                  TimeUtilities timeUtilities,
                                                  AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        super(webServiceClient, eventService, partyInformationRepository, timeUtilities, accessTokenAuthorizationProvider);
        this.webServiceClient = webServiceClient;
        this.eventService = eventService;
        this.partyInformationRepository = partyInformationRepository;
        this.timeUtilities = timeUtilities;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    @Override
    public void sendEvents(long[] eventIds, WebServiceResponseParser<ProcessEventsServiceResponse> parser, String rewardName, String rewardKey, String instructionType) {
        Call<ProcessEventsServiceResponse> request = eventService.getProcessEventsV2Request(partyInformationRepository.getTenantId(),
                accessTokenAuthorizationProvider.getAuthorization(),
                new ProcessEventsV2ServiceRequest(eventIds, timeUtilities.getNowAsISODateTimeWithZoneId(), partyInformationRepository.getPartyId(), rewardName, rewardKey, instructionType));
        webServiceClient.executeAsynchronousRequest(request, parser);
    }

}
