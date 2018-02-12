package com.vitalityactive.va.shared.questionnaire.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QuestionnaireSubmissionService {
    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-assessments-service-1/1.0/svc/{tenantid}/captureAssessment/{partyid}/{QuestionnaireSetTypeKey}")
    Call<QuestionnaireSubmitResponse> getQuestionnaireSubmitRequest(@Header("Authorization") String authorization,
                                                                    @Path("tenantid") long tenantId,
                                                                    @Path("partyid") long partyId,
                                                                    @Path("QuestionnaireSetTypeKey") long questionnaireSetTypeKey,
                                                                    @Body QuestionnaireSubmitRequestBody questionnaireSubmitRequestBody);
}
