package com.vitalityactive.va.networking.model;


import com.google.gson.annotations.SerializedName;

public class VitalityAgeRequest {

    @SerializedName("healthAttributeTypes")
    public HealthAttributeType[] healthAttributeTypes;

    @SerializedName("healthAttributeEffectivePeriod")
    public HealthAttributeEffectivePeriod healthAttributeEffectivePeriod;

    public VitalityAgeRequest(HealthAttributeType[] healthAttributeTypes, HealthAttributeEffectivePeriod healthAttributeEffectivePeriod) {
        this.healthAttributeTypes = healthAttributeTypes;
        this.healthAttributeEffectivePeriod = healthAttributeEffectivePeriod;
    }

    public static class HealthAttributeEffectivePeriod {
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
        @SerializedName("effectiveTo")
        public String effectiveTo;
    }

    public static class HealthAttributeType {
        @SerializedName("typeKey")
        public Long typeKey;

        public HealthAttributeType(Long typeKey) {
            this.typeKey = typeKey;
        }
    }
}
