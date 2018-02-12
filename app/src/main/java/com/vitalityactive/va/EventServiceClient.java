package com.vitalityactive.va;

import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.ProcessEventsServiceRequest;
import com.vitalityactive.va.networking.model.ProcessEventsServiceRequestSNV;
import com.vitalityactive.va.snv.dto.ConfirmAndSubmitItemDTO;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.vhc.captureresults.models.CapturedField;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class EventServiceClient {
    private final WebServiceClient webServiceClient;
    private final EventService eventService;
    private PartyInformationRepository partyInformationRepository;
    private TimeUtilities timeUtilities;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    private Call<ProcessEventsServiceResponse> processEventsRequest;
    private Call<ProcessEventsServiceResponse> processEventsRequestForSNV;

    @Inject
    public EventServiceClient(WebServiceClient webServiceClient, EventService eventService, PartyInformationRepository partyInformationRepository, TimeUtilities timeUtilities, AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.eventService = eventService;
        this.partyInformationRepository = partyInformationRepository;
        this.timeUtilities = timeUtilities;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    public void sendEvents(long[] eventIds, WebServiceResponseParser<ProcessEventsServiceResponse> parser) {
        sendEvents(eventIds, parser, "", "", "");
    }

    public void submitVHC(List<CapturedField> capturedFields, List<ProofItemDTO> proofItems, WebServiceResponseParser<ProcessEventsServiceResponse> parser) {
        processEventsRequest = eventService.getProcessEventsRequest(partyInformationRepository.getTenantId(), accessTokenAuthorizationProvider.getAuthorization(), createProcessEventsServiceRequest(capturedFields, proofItems));
        webServiceClient.executeAsynchronousRequest(processEventsRequest, parser);
    }

    private ProcessEventsServiceRequest createProcessEventsServiceRequest(List<CapturedField> capturedFields, List<ProofItemDTO> proofItems) {
        return new ProcessEventsServiceRequest(capturedFields, timeUtilities.getNowAsISODateTimeWithZoneId(), partyInformationRepository.getPartyId(), proofItems, timeUtilities);
    }

    public boolean isSubmittingVHC() {
        return processEventsRequest != null && webServiceClient.isRequestInProgress(processEventsRequest.request().toString());
    }

    public void sendEvents(long[] eventIds, WebServiceResponseParser<ProcessEventsServiceResponse> parser, String rewardName, String rewardKey, String instructionType) {
        Call<ProcessEventsServiceResponse> request = eventService.getProcessEventsRequest(partyInformationRepository.getTenantId(),
                accessTokenAuthorizationProvider.getAuthorization(),
                new ProcessEventsServiceRequest(eventIds, timeUtilities.getNowAsISODateTimeWithZoneId(), partyInformationRepository.getPartyId()));
        webServiceClient.executeAsynchronousRequest(request, parser);
    }

    public void submitSNV(List<ConfirmAndSubmitItemDTO> screeningItems, List<ConfirmAndSubmitItemDTO> vaccinationItems, List<ProofItemDTO> proofItems, WebServiceResponseParser<ProcessEventsServiceResponse> parser) {
        processEventsRequestForSNV = eventService.getProcessEventsRequest(partyInformationRepository.getTenantId(),
                accessTokenAuthorizationProvider.getAuthorization(),
                createProcessEventsServiceRequestForSNV(screeningItems, vaccinationItems, proofItems));
        webServiceClient.executeAsynchronousRequest(processEventsRequestForSNV, parser);
    }

    public boolean isSubmittingSNV() {
        return processEventsRequestForSNV != null && webServiceClient.isRequestInProgress(processEventsRequestForSNV.request().toString());
    }

    private ProcessEventsServiceRequestSNV createProcessEventsServiceRequestForSNV(List<ConfirmAndSubmitItemDTO> screeningItems, List<ConfirmAndSubmitItemDTO> vaccinationItems, List<ProofItemDTO> proofItems) {
        return new ProcessEventsServiceRequestSNV(screeningItems, vaccinationItems, timeUtilities.getNowAsISODateTimeWithZoneId(), partyInformationRepository.getPartyId(), proofItems, timeUtilities);
    }
}
