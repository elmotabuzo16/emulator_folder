package com.vitalityactive.va.help.service;

import com.vitalityactive.va.help.model.HelpResponse;
import com.vitalityactive.va.search.ContentHelpResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by christian.j.p.capin on 11/30/2017.
 */

public interface HelpService {
    @SuppressWarnings("SpellCheckingInspection")
    @Headers({"Content-Type: application/json"})
    @GET("liferay-content-management/1.0/faq-service/get-faqbytag/{tagName}")
    Call<HelpResponse> getHelpSuggestions(@Path("tagName") String tagName,
                                          @Header("groupID") long groupID,
                                          @Header("Authorization") String authorization);

    @Headers({"Content-Type: application/json"})
    @GET("liferay-content-management/1.0/faq-service/get-faqbykey/{tagkey}/tagName/{tagName}")
    Call<ContentHelpResponse> getHelpDetails(@Header("Authorization") String authorizationHeader,
                                             @Header("groupId") long groupId,
                                             @Path("tagkey") String tagkey,
                                             @Path("tagName") String tagName);
    }

