package com.vitalityactive.va.networking.model;


import com.google.gson.annotations.SerializedName;

public class PersonalDetailsResponse {

    public static class VerifyResponse {

    }

    public static class ChangeEmailResponse {
        @SerializedName("newAccessToken")
        public String newAccessToken;
    }

}
