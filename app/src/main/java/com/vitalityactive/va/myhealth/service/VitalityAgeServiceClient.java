package com.vitalityactive.va.myhealth.service;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.myhealth.events.VitalityAgeEvents;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.VitalityAgeRequest;
import com.vitalityactive.va.networking.model.VitalityAgeResponse;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class VitalityAgeServiceClient implements WebServiceResponseParser<VitalityAgeResponse> {

    public final WebServiceClient webServiceClient;
    public final VitalityAgeService vitalityAgeService;
    public final AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    public final PartyInformationRepository partyInformationRepository;
    private final InsurerConfigurationRepository insurerConfigurationRepository;
    private final EventDispatcher eventDispatcher;
    private Call<VitalityAgeResponse> vitalityAgeServiceRequest;
    private RequestResult vitalityAgeServiceResult;

    @Inject
    public VitalityAgeServiceClient(WebServiceClient webServiceClient,
                                    PartyInformationRepository partyInformationRepository,
                                    VitalityAgeService vitalityAgeService,
                                    AccessTokenAuthorizationProvider accessTokenAuthorizationProvider, InsurerConfigurationRepository insurerConfigurationRepository, EventDispatcher eventDispatcher) {
        this.webServiceClient = webServiceClient;
        this.partyInformationRepository = partyInformationRepository;
        this.vitalityAgeService = vitalityAgeService;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
        this.insurerConfigurationRepository = insurerConfigurationRepository;
        this.eventDispatcher = eventDispatcher;
    }

    public void getVitalityAgeValue() {
        VitalityAgeRequest.HealthAttributeEffectivePeriod healthAttributeEffectivePeriod = new VitalityAgeRequest.HealthAttributeEffectivePeriod();
        healthAttributeEffectivePeriod.effectiveFrom = NonUserFacingDateFormatter.getYearMonthDayDateString(insurerConfigurationRepository.getCurrentMembershipPeriodStart());
        healthAttributeEffectivePeriod.effectiveTo = NonUserFacingDateFormatter.getYearMonthDayDateString(insurerConfigurationRepository.getCurrentMembershipPeriodEnd());
        VitalityAgeRequest.HealthAttributeType healthAttributeType = new VitalityAgeRequest.HealthAttributeType(VitalityAgeConstants.VITALITY_AGE_TYPE_KEY);
        VitalityAgeRequest vitalityAgeRequest = new VitalityAgeRequest(new VitalityAgeRequest.HealthAttributeType[]{healthAttributeType}, healthAttributeEffectivePeriod);
        vitalityAgeServiceRequest = vitalityAgeService.getVitalityAgeValue(accessTokenAuthorizationProvider.getAuthorization(),
                partyInformationRepository.getTenantId(),
                partyInformationRepository.getPartyId(), vitalityAgeRequest);
        webServiceClient.executeAsynchronousRequest(vitalityAgeServiceRequest, this);
    }

    @Override
    public void parseResponse(VitalityAgeResponse vitalityAgeResponse) {
        onVitalityAgeServiceRequestCompleted(RequestResult.SUCCESSFUL);
        VitalityAge vitalityAge = getVitalityAgeAttribute(vitalityAgeResponse);
        if (vitalityAge != null) {
            eventDispatcher.dispatchEvent(new VitalityAgeEvents.VitalityAgeResponseEvent(vitalityAge));
        } else {
            eventDispatcher.dispatchEvent(new VitalityAgeEvents.VitalityAgeResponseEvent(null));
        }
    }

    private VitalityAge getVitalityAgeAttribute(VitalityAgeResponse vitalityAgeResponse) {
        if (vitalityAgeResponse != null && vitalityAgeResponse.healthAttributeReadings != null && !vitalityAgeResponse.healthAttributeReadings.isEmpty()) {
            VitalityAgeResponse.HealthAttributeReading healthAttribute = vitalityAgeResponse.healthAttributeReadings.get(0);
            return new VitalityAge.Builder()
                    .age(healthAttribute.value)
                    .effectiveType(healthAttribute.attributeTypeKey)
                    .feedbackTitle(healthAttribute.attributeTypeCode)
                    .feedbackContent(healthAttribute.attributeTypeName)
                    .build();
        }
        return null;
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        onVitalityAgeServiceRequestCompleted(RequestResult.GENERIC_ERROR);
    }

    @Override
    public void handleGenericError(Exception exception) {
        onVitalityAgeServiceRequestCompleted(RequestResult.GENERIC_ERROR);
    }

    @Override
    public void handleConnectionError() {
        onVitalityAgeServiceRequestCompleted(RequestResult.CONNECTION_ERROR);
    }

    public synchronized RequestResult getVitalityAgeServiceResult() {
        return vitalityAgeServiceResult;
    }

    public synchronized void setVitalityAgeServiceRequest(RequestResult vitalityAgeServiceResult) {
        this.vitalityAgeServiceResult = vitalityAgeServiceResult;
    }

    private void onVitalityAgeServiceRequestCompleted(RequestResult requestResult) {
        setVitalityAgeServiceRequest(requestResult);
    }

    public void cancelRequest() {
        if (vitalityAgeServiceRequest != null) {
            vitalityAgeServiceRequest.cancel();
            vitalityAgeServiceRequest = null;
        }
    }
}
