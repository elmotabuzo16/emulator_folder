package com.vitalityactive.va.snv.history.service;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dharel.h.rosell on 11/30/2017.
 */

public class ScreeningAndVaccinationHistoryRequest {
    @SerializedName("effectivePeriod")
    public EffectivePeriod effectivePeriod;
    @SerializedName("eventTypes")
    public EventType[] eventTypes;

    public ScreeningAndVaccinationHistoryRequest(String effectiveFrom, String effectiveTo, Integer[] key) {

        effectivePeriod = new EffectivePeriod(effectiveFrom, effectiveTo);
        this.eventTypes = new EventType[key.length];
        for(int i=0; i < key.length; i++){
            eventTypes[i] = new EventType(key[i]);
        }
    }

    public static class EffectivePeriod {
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
        @SerializedName("effectiveTo")
        public String effectiveTo;

        public EffectivePeriod(String effectiveFrom, String effectiveTo) {
            this.effectiveFrom = effectiveFrom;
            this.effectiveTo = effectiveTo;
        }
    }

    public static class EventType {
        @SerializedName("typeKey")
        public Integer typeKey;

        public EventType(Integer key) {
            this.typeKey = key;
        }
    }

}
