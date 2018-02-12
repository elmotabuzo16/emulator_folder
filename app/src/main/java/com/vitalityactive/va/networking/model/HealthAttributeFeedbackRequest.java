package com.vitalityactive.va.networking.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class HealthAttributeFeedbackRequest {
    @SerializedName("healthAttributeTypes")
    @NonNull
    public HealthAttributeType[] healthAttributeTypes;

    public static class HealthAttributeType {
        @SerializedName("typeKey")
        public Long typeKey;
    }

}
