package com.vitalityactive.va.shared.questionnaire.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QuestionnaireSetService {
    @Headers({"Content-Type: application/json"})
    @POST("vitality-assessment-agreement-events-points-services-service-1/1.0/svc/{tenantid}/questionnaireProgressAndPointsTracking/{partyid}/{vitalitymemberhipid}")
    Call<QuestionnaireSetResponse> getQuestionnaireSetsRequest(@Header("Authorization") String authorization,
                                                               @Path("tenantid") long tenantId,
                                                               @Path("partyid") long partyId,
                                                               @Path("vitalitymemberhipid") String vitalityMembershipId,
                                                               @Body QuestionnaireSetRequestBody questionnaireSetRequestBody);
}
