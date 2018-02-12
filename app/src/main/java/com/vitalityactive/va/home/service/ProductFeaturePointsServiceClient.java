package com.vitalityactive.va.home.service;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.constants.ProductFeatureCategoryType;
import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.home.service.status.ProductFeaturePointsRequestBody;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class ProductFeaturePointsServiceClient {
    private final WebServiceClient webServiceClient;
    private final ProductFeaturePointsService service;
    private PartyInformationRepository partyInformationRepository;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    private Call<ProductFeaturePointsResponse> request;

    @Inject
    public ProductFeaturePointsServiceClient(WebServiceClient webServiceClient,
                                             ProductFeaturePointsService service,
                                             PartyInformationRepository partyInformationRepository,
                                             AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.service = service;
        this.partyInformationRepository = partyInformationRepository;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    public boolean isRequestInProgress() {
        return request != null && webServiceClient.isRequestInProgress(request.request().toString());
    }

    public void fetchProductFeaturePoints(WebServiceResponseParser<ProductFeaturePointsResponse> responseParser) {
        request = getStatusRewardsResponseCall();
        webServiceClient.executeAsynchronousRequest(request, responseParser);
    }

    private Call<ProductFeaturePointsResponse> getStatusRewardsResponseCall() {
        ProductFeaturePointsRequestBody productFeaturePointsRequestBody = new ProductFeaturePointsRequestBody(ProductFeatureCategoryType._EARNPOINTSMOBILE);

        return service.getProductFeaturePointsRequest(accessTokenAuthorizationProvider.getAuthorization(),
                partyInformationRepository.getTenantId(),
                partyInformationRepository.getPartyId(),
                productFeaturePointsRequestBody);
    }
}
