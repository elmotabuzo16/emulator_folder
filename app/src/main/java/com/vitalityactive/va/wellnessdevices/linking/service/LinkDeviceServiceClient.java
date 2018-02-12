package com.vitalityactive.va.wellnessdevices.linking.service;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.dto.EmailAddressDto;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

import static com.vitalityactive.va.wellnessdevices.Constants.SUCCESS_LINK_REDIRECT_URL;

@Singleton
public class LinkDeviceServiceClient {

    private final WebServiceClient webServiceClient;
    private final PartyInformationRepository partyInformationRepository;
    private final LinkDeviceService service;
    private Call<String> linkRequest;
    private Call<String> delinkRequest;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    @Inject
    public LinkDeviceServiceClient(WebServiceClient webServiceClient,
                                   PartyInformationRepository partyInformationRepository,
                                   LinkDeviceService service,
                                   AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.partyInformationRepository = partyInformationRepository;
        this.service = service;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    public boolean isRequestInProgress() {
//        return linkRequest != null && webServiceClient.isRequestInProgress(linkRequest.request().toString());
        return false; // not used
    }

    public void linkDevice(PartnerDto partner,
                            WebServiceResponseParser<String> responseParser) {
        linkRequest = getLinkRequest(partner);
        webServiceClient.executeAsynchronousRequest(linkRequest, responseParser);
    }

    public void delinkDevice(PartnerDto partner,
                           WebServiceResponseParser<String> responseParser) {
        delinkRequest = getDelinkRequest(partner);
        webServiceClient.executeAsynchronousRequest(delinkRequest, responseParser);
    }

    private Call<String> getLinkRequest(PartnerDto partner) {
        long tenantId = partyInformationRepository.getTenantId();
        long partyId = partyInformationRepository.getPartyId();
        GetFullListResponse.Partner partnerOutput = new GetFullListResponse.Partner(partner);
        partnerOutput.partnerLink.redirectUrl = SUCCESS_LINK_REDIRECT_URL;
        String authorization = accessTokenAuthorizationProvider.getAuthorization();
        return service.linkDevice(tenantId, authorization, generateRequestBody(partnerOutput));
    }

    private Call<String> getDelinkRequest(PartnerDto partner) {
        long tenantId = partyInformationRepository.getTenantId();
        long partyId = partyInformationRepository.getPartyId();
//        partner.partnerLink.redirectUrl = SUCCESS_REDIRECT_URL;
        String authorization = accessTokenAuthorizationProvider.getAuthorization();
        GetFullListResponse.Partner partnerOutput = new GetFullListResponse.Partner(partner);
        return service.delinkDevice(tenantId, partyId, partnerOutput.partnerSystem, authorization,
                partnerOutput.partnerDelink.url, partnerOutput.partnerDelink.method);
    }

    private LinkDeviceRequest generateRequestBody(GetFullListResponse.Partner partner){
        LinkDeviceRequest requestBody = new LinkDeviceRequest();
        requestBody.partner = partner;
        LinkDeviceRequest.User user = new LinkDeviceRequest.User();
        List<EmailAddressDto> emails = partyInformationRepository.getEmails();
        if(emails != null && !emails.isEmpty()) {
            user.email = emails.get(0).getValue();
        }
        user.identifierType = "PARTY_ID";
        user.userIdentifier = partyInformationRepository.getPartyId()+"";
        requestBody.user = user;
        return requestBody;
    }
}
