package com.vitalityactive.va.home.service;

import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.home.service.status.ProductFeaturePointsRequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductFeaturePointsService {
    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-vitality-products-service-1/1.0/svc/{tenantId}/getProductFeaturePointsByProductFeatureCategoryType/{partyId}")
    Call<ProductFeaturePointsResponse> getProductFeaturePointsRequest(@Header("Authorization") String authorization,
                                                               @Path("tenantId") long tenantId,
                                                               @Path("partyId") long partyId,
                                                               @Body ProductFeaturePointsRequestBody productFeaturePointsRequestBody);
}
