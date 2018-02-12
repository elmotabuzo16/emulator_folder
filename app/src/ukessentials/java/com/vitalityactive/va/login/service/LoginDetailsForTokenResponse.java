package com.vitalityactive.va.login.service;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class LoginDetailsForTokenResponse {
    @NonNull
    @SerializedName("username")
    public String username = "";
    @NonNull
    @SerializedName("password")
    public String password = "";
}
