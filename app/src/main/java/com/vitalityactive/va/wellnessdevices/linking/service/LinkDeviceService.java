package com.vitalityactive.va.wellnessdevices.linking.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LinkDeviceService {
    @Headers({"Content-Type: application/json"})
    @POST("vdp-api/1.0/devices/tenant/{tenant}")
    Call<String> linkDevice(@Path("tenant") long tenantId,
                            @Header("Authorization") String authorization,
                            @Body LinkDeviceRequest request);

    @Headers({"Content-Type: application/json"})
    @DELETE("vdp-api/1.0/devices/tenant/{tenant}/identifier/{partyId}/partnerSystem/{partnerSystem}")
    Call<String> delinkDevice(@Path("tenant") long tenantId,
                              @Path("partyId") long partyId,
                              @Path("partnerSystem") String partnerSystem,
                              @Header("Authorization") String authorization,
                              @Query(value = "delinkUrl", encoded = true) String delinkUrl,
                              @Query(value = "delinkMethod", encoded = true) String delinkMethod);
}
