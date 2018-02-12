package com.vitalityactive.va.snv.onboarding.service;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kerry.e.lawagan on 11/24/2017.
 */

public class GetPotentialPointsAndEventsCompletedPointsRequest {
    @SerializedName("eventTypeses")
    public EventType[] eventTypeses;
    @SerializedName("vitalityMembershipId")
    public Long vitalityMembershipId;

    public GetPotentialPointsAndEventsCompletedPointsRequest(Integer[] eventTypeses, Long vitalityMembershipId) {
        this.vitalityMembershipId = vitalityMembershipId;
        this.eventTypeses = new EventType[eventTypeses.length];

        int i = 0;
        for (Integer eventType: eventTypeses) {
            this.eventTypeses[i++] = new EventType(eventType);
        }
    }

    public static class EventType {
        @SerializedName("typeKey")
        public int typeKey;

        public EventType(int typeKey) {
            this.typeKey = typeKey;
        }

    }
}
