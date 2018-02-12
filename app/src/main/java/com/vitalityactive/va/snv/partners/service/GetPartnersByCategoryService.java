package com.vitalityactive.va.snv.partners.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by kerry.e.lawagan on 12/8/2017.
 */

public interface GetPartnersByCategoryService {

    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-vitality-products-service-1/1.0/svc/{tenant}/getPartnersByCategory/{partyId}")
    Call<GetPartnersByCategoryResponse> getPartnersByCategoryResponseRequest(@Header("Authorization") String authorization,
                                                                                                               @Path("tenant") long tenantId,
                                                                                                               @Path("partyId") long partyId,
                                                                                                               @Header("locale") String locale,
                                                                                                               @Body GetPartnersByCategoryRequest request);
}

