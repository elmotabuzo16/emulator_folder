package com.vitalityactive.va.snv.partners.service;

import android.util.Log;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.VitalityMembershipRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.snv.onboarding.repository.ScreeningsAndVaccinationsRepositoy;
import com.vitalityactive.va.snv.partners.presenter.GetPartnersByCategoryFailedEvent;
import com.vitalityactive.va.snv.partners.presenter.GetPartnersByCategorySuccessEvent;
import com.vitalityactive.va.snv.shared.SnvConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import retrofit2.Call;

/**
 * Created by kerry.e.lawagan on 12/8/2017.
 */

public class GetPartnersByCategoryServiceClient implements WebServiceResponseParser<GetPartnersByCategoryResponse> {
    private AccessTokenAuthorizationProvider accessToken;
    private EventDispatcher eventDispatcher;
    private Call<GetPartnersByCategoryResponse> request;
    private WebServiceClient webServiceClient;
    private GetPartnersByCategoryService service;
    private ScreeningsAndVaccinationsRepositoy repository;

    private PartyInformationRepository partyInformationRepository;
    private VitalityMembershipRepository vitalityMembershipRepository;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Inject
    public GetPartnersByCategoryServiceClient(WebServiceClient webServiceClient,
                                              GetPartnersByCategoryService service,
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

    public void invokeApi() {
        request = service.getPartnersByCategoryResponseRequest(accessToken.getAuthorization(),
                partyInformationRepository.getTenantId(),
                partyInformationRepository.getPartyId(),
                Locale.getDefault().getISO3Language(),
                generateRequest());
        //webServiceClient.executeAsynchronousRequest(request, this);
        mockData();
    }

    private void mockData() {
        GetPartnersByCategoryResponse response = new GetPartnersByCategoryResponse();
        List<GetPartnersByCategoryResponse.ProductFeatureGroup> productFeatureGroupList = new ArrayList<>();

        GetPartnersByCategoryResponse.ProductFeatureGroup product = new GetPartnersByCategoryResponse.ProductFeatureGroup();
        product.setLogoFileName("sample.jpg");
        product.setName("Clicks");
        product.setLongDescription("You will get one free gender, age, and lifestyle-specific Screenings and Vaccinations per year paid from your Screening Benefit as Dis-Chem is a participating partner on the Vitality Wellness network.\\n\\nIf you don't have all the screening tests and vaccinations on the same day, you will have to pay for the tests and screenings you have later from your Medical Savings Account or your pockets.");

        productFeatureGroupList.add(product);

        product = new GetPartnersByCategoryResponse.ProductFeatureGroup();
        product.setLogoFileName("sample.jpg");
        product.setName("Dis-Chem");
        product.setLongDescription("You will get one free gender, age, and lifestyle-specific Screenings and Vaccinations per year paid from your Screening Benefit as Dis-Chem is a participating partner on the Vitality Wellness network.\\n\\nIf you don't have all the screening tests and vaccinations on the same day, you will have to pay for the tests and screenings you have later from your Medical Savings Account or your pockets.");

        productFeatureGroupList.add(product);

        product = new GetPartnersByCategoryResponse.ProductFeatureGroup();
        product.setLogoFileName("sample.jpg");
        product.setName("Easy Way");
        product.setLongDescription("You will get one free gender, age, and lifestyle-specific Screenings and Vaccinations per year paid from your Screening Benefit as Dis-Chem is a participating partner on the Vitality Wellness network.\\n\\nIf you don't have all the screening tests and vaccinations on the same day, you will have to pay for the tests and screenings you have later from your Medical Savings Account or your pockets.");

        productFeatureGroupList.add(product);

        response.setProductFeatureGroups(productFeatureGroupList);

        parseResponse(response);
    }

    private GetPartnersByCategoryRequest generateRequest() {
        String dateString = dateFormat.format(new Date());
        return new GetPartnersByCategoryRequest(dateString, 11);
    }

    @Override
    public void parseResponse(GetPartnersByCategoryResponse response) {
        eventDispatcher.dispatchEvent(new GetPartnersByCategorySuccessEvent(response));
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        eventDispatcher.dispatchEvent(new GetPartnersByCategoryFailedEvent(SnvConstants.SnvApiResult.GENERIC_ERROR));
    }

    @Override
    public void handleGenericError(Exception exception) {
        eventDispatcher.dispatchEvent(new GetPartnersByCategoryFailedEvent(SnvConstants.SnvApiResult.GENERIC_ERROR));
    }

    @Override
    public void handleConnectionError() {
        eventDispatcher.dispatchEvent(new GetPartnersByCategoryFailedEvent(SnvConstants.SnvApiResult.CONNECTION_ERROR));
    }
}
