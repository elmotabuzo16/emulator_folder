package com.vitalityactive.va.termsandconditions;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.constants.BooleanString;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.CaptureLoginEventServiceRequest;
import com.vitalityactive.va.utilities.TimeUtilities;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class CaptureLoginEventServiceClient {
    private final WebServiceClient webServiceClient;
    private final CaptureLoginEventService captureLoginEventService;
    private PartyInformationRepository partyInformationRepository;
    private TimeUtilities timeUtilities;
    private Call<String> captureLoginEventRequest;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    @Inject
    public CaptureLoginEventServiceClient(WebServiceClient webServiceClient, CaptureLoginEventService captureLoginEventService, PartyInformationRepository partyInformationRepository, TimeUtilities timeUtilities, AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.captureLoginEventService = captureLoginEventService;
        this.partyInformationRepository = partyInformationRepository;
        this.timeUtilities = timeUtilities;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    public void captureLoginEvent(long eventId, String eventType, boolean value, WebServiceResponseParser<String> parser) {
        CaptureLoginEventServiceRequest requestBody = new CaptureLoginEventServiceRequest();
        requestBody.loginSuccess = value;
        requestBody.loginDate = timeUtilities.getNowAsISODateTimeWithZoneId();
        CaptureLoginEventServiceRequest.UserInstructionResponse userInstructionResponse = new CaptureLoginEventServiceRequest.UserInstructionResponse(eventId, eventType, BooleanString.stringFromBoolean(value));
        requestBody.userInstructionResponses = new CaptureLoginEventServiceRequest.UserInstructionResponse[] {userInstructionResponse};
        captureLoginEventRequest = captureLoginEventService.getCaptureLoginEventRequest(accessTokenAuthorizationProvider.getAuthorization(),
                partyInformationRepository.getTenantId(),
                partyInformationRepository.getPartyId(),
                requestBody);
        webServiceClient.executeAsynchronousRequest(captureLoginEventRequest, parser);
    }

    public boolean isRequestInProgress() {
        return captureLoginEventRequest != null && webServiceClient.isRequestInProgress(captureLoginEventRequest.request().toString());
    }
}
