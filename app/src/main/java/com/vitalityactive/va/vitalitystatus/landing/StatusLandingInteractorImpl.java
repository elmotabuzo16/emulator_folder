package com.vitalityactive.va.vitalitystatus.landing;

import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.home.events.GetProductFeaturePointsResponseEvent;
import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.home.service.ProductFeaturePointsServiceClient;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;
import com.vitalityactive.va.vitalitystatus.earningpoints.PointsInformationDTO;
import com.vitalityactive.va.vitalitystatus.repository.ProductPointsRepository;
import com.vitalityactive.va.vitalitystatus.repository.VitalityStatusRepository;

import java.util.List;

public class StatusLandingInteractorImpl implements StatusLandingInteractor {
    private ProductFeaturePointsServiceClient serviceClient;
    private EventDispatcher eventDispatcher;
    private VitalityStatusRepository vitalityStatusRepository;
    private ProductPointsRepository productPointsRepository;
    private boolean requestInProgress;

    public StatusLandingInteractorImpl(ProductFeaturePointsServiceClient serviceClient,
                                       EventDispatcher eventDispatcher,
                                       VitalityStatusRepository vitalityStatusRepository,
                                       ProductPointsRepository productPointsRepository) {
        this.serviceClient = serviceClient;
        this.eventDispatcher = eventDispatcher;
        this.vitalityStatusRepository = vitalityStatusRepository;
        this.productPointsRepository = productPointsRepository;
    }

    @Override
    public void fetchProductFeaturePoints() {
        requestInProgress = true;

        serviceClient.fetchProductFeaturePoints(new WebServiceResponseParser<ProductFeaturePointsResponse>() {
            @Override
            public void parseResponse(ProductFeaturePointsResponse productFeaturePointsResponse) {
                if (productPointsRepository.persistProductFeaturePointsResponse(productFeaturePointsResponse)) {
                    eventDispatcher.dispatchEvent(new GetProductFeaturePointsResponseEvent(RequestResult.SUCCESSFUL, productFeaturePointsResponse));
                } else {
                    eventDispatcher.dispatchEvent(new GetProductFeaturePointsResponseEvent(RequestResult.GENERIC_ERROR));
                }

                requestInProgress = false;
            }

            @Override
            public void parseErrorResponse(String errorBody, int code) {
                eventDispatcher.dispatchEvent(new GetProductFeaturePointsResponseEvent(RequestResult.GENERIC_ERROR));
                requestInProgress = false;
            }

            @Override
            public void handleGenericError(Exception exception) {
                eventDispatcher.dispatchEvent(new GetProductFeaturePointsResponseEvent(RequestResult.GENERIC_ERROR));
                requestInProgress = false;
            }

            @Override
            public void handleConnectionError() {
                eventDispatcher.dispatchEvent(new GetProductFeaturePointsResponseEvent(RequestResult.CONNECTION_ERROR));
                requestInProgress = false;
            }
        });
    }

    @Override
    public VitalityStatusDTO loadVitalityStatus() {
        return vitalityStatusRepository.getVitalityStatus();
    }

    @Override
    public boolean shouldShowMyRewards() {
        return vitalityStatusRepository.shouldShowMyRewards();
    }

    @Override
    public boolean hasFeaturePointsData() {
        return productPointsRepository.hasCachedPointsInformation();
    }

    @Override
    public List<PointsInformationDTO> loadPointsCategories() {
        return productPointsRepository.getAllPointsCategories();
    }

    @Override
    public boolean requestInProgress() {
        return requestInProgress;
    }
}
