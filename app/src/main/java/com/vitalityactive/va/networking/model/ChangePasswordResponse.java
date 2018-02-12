package com.vitalityactive.va.networking.model;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordResponse {
    @SerializedName("newAccessToken")
    public String newAccessToken;
}
