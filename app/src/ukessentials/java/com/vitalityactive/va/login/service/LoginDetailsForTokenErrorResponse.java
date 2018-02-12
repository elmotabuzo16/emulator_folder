package com.vitalityactive.va.login.service;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LoginDetailsForTokenErrorResponse {
    @NonNull
    @SerializedName("errors")
    public List<Error> errors = new ArrayList<>();

    public class Error {
        @NonNull
        @SerializedName("message")
        public String message = "";

        @SerializedName("code")
        public int code;
    }
}
