package com.vitalityactive.va.userpreferences;

import com.vitalityactive.va.networking.model.UpdatePartyServiceRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserPreferencesService {
    @Headers({"Content-Type: application/json"})
    @POST("vitality-party-party-information-services-service-1/1.0/svc/{tenantId}/updateParty/{partyId}")
    Call<String> getEmailPreferenceUpdateRequest(@Header("Authorization") String authorization,
                                                 @Path("tenantId") long tenantId,
                                                 @Path("partyId") long partyId,
                                                 @Body UpdatePartyServiceRequest request);
}
