package com.vitalityactive.va.profile;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.authentication.BasicAuthorizationProvider;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.ChangeEmailRequest;
import com.vitalityactive.va.networking.model.PersonalDetailsRequest;
import com.vitalityactive.va.networking.model.PersonalDetailsResponse;
import com.vitalityactive.va.register.entity.EmailAddress;
import com.vitalityactive.va.register.entity.Password;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class PersonalDetailsClient {
    private WebServiceClient webServiceClient;
    private PartyInformationRepository partyInformationRepository;
    private BasicAuthorizationProvider basicAuthorizationProvider;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    private ServiceGenerator serviceGenerator;

    @Inject
    PersonalDetailsClient(WebServiceClient webServiceClient, BasicAuthorizationProvider authorizationProvider, ServiceGenerator serviceGenerator, PartyInformationRepository partyInformationRepository, AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.basicAuthorizationProvider = authorizationProvider;
        this.partyInformationRepository = partyInformationRepository;
        this.serviceGenerator = serviceGenerator;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    void verifyNewEmailAddress(EmailAddress newEmailAddress, WebServiceResponseParser<PersonalDetailsResponse.VerifyResponse> parser) {
        PersonalDetailsRequest.VerifyNewEmailRequest verifyNewEmailRequest = new PersonalDetailsRequest.VerifyNewEmailRequest(newEmailAddress);
        PersonalDetailsRequest personalDetailsRequest = new PersonalDetailsRequest(verifyNewEmailRequest);

        Call<PersonalDetailsResponse.VerifyResponse> request = serviceGenerator.create(PersonalDetailsService.class).getVerifyNewEmailRequest(
                partyInformationRepository.getTenantId(), personalDetailsRequest, accessTokenAuthorizationProvider.getAuthorization());
        webServiceClient.executeAsynchronousRequest(request, parser);
    }

    void changeEmailAddress(EmailAddress existingEmailAddress, EmailAddress newEmailAddress, Password password, WebServiceResponseParser<PersonalDetailsResponse.ChangeEmailResponse> parser) {
        ChangeEmailRequest changeEmailRequest = new ChangeEmailRequest(existingEmailAddress, newEmailAddress, password);

        Call<PersonalDetailsResponse.ChangeEmailResponse> request = serviceGenerator.create(PersonalDetailsService.class).getChangeEmailRequest(changeEmailRequest, basicAuthorizationProvider.getAuthorization());
        webServiceClient.executeAsynchronousRequest(request, parser);
    }
}
