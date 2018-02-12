package com.vitalityactive.va.membershippass.service;

import android.annotation.SuppressLint;
import android.util.Log;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.membershippass.model.MembershipPassResponse;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

/**
 * Created by christian.j.p.capin on 11/17/2017.
 */
@Singleton
public class MembershipPassServiceClient {
    private final WebServiceClient webServiceClient;
    private final PartyInformationRepository partyInformationRepository;
    private final MembershipPassService service;
    private Call<MembershipPassResponse> request;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    private static final String TAG = "MembershipPassServiceClient";

    @Inject
    public MembershipPassServiceClient(WebServiceClient webServiceClient,
                                       PartyInformationRepository partyInformationRepository,
                                       MembershipPassService membershipPassService,
                                       AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {

        this.webServiceClient = webServiceClient;
        this.partyInformationRepository = partyInformationRepository;
        this.service = membershipPassService;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    @SuppressLint("LongLogTag")
    public boolean isRequestInProgress() {
        return request != null && webServiceClient.isRequestInProgress(request.request().toString());
    }

    public void fetchMembershipPassDetails(WebServiceResponseParser<MembershipPassResponse> responseParser) {
        request = getMembershipPassResponseCall();
        webServiceClient.executeAsynchronousRequest(request, responseParser);
    }

    @SuppressLint("LongLogTag")
    private Call<MembershipPassResponse> getMembershipPassResponseCall() {
        long tenantId = partyInformationRepository.getTenantId();

        long vitalityMembershipId = partyInformationRepository.getPartyId();
        return service.getVitalityMembershipById(tenantId, vitalityMembershipId, accessTokenAuthorizationProvider.getAuthorization());
    }

}
