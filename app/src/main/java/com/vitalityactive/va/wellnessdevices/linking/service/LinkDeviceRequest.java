package com.vitalityactive.va.wellnessdevices.linking.service;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;

public class LinkDeviceRequest {
    @SerializedName("partner")
    public GetFullListResponse.Partner partner;
    @SerializedName("user")
    public User user;

    public static class User {
        @SerializedName("email")
        public String email;
        @SerializedName("identifierType")
        public String identifierType;
        @SerializedName("userIdentifier")
        public String userIdentifier;
    }
}
