package com.vitalityactive.va.networking.model;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class HealthAttributeInformationRequest {

    @SerializedName("sectionKeys")
    @NonNull
    public Long sectionKeys[];

    @SerializedName("summary")
    @NonNull
    public Boolean summary;
}
