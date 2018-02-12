package com.vitalityactive.va.vhc.service;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.utilities.NumberUtility;

public class HealthAttributeServiceRequestBody {
    @SerializedName("events")
    private Event[] events;

    public HealthAttributeServiceRequestBody(int[] typeKeys) {

        int[] finalKeys = NumberUtility.convertToUniqueIntArray(typeKeys);

        events = new Event[finalKeys.length];

        for (int i = 0; i < finalKeys.length; i++) {
            this.events[i] = new Event(finalKeys[i]);
        }
    }

    private class Event {
        @SerializedName("typeKey")
        int typeKey;

        public Event(int typeKey) {
            this.typeKey = typeKey;
        }
    }


}
