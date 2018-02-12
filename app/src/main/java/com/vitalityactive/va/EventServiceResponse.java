package com.vitalityactive.va;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventServiceResponse {
    // not all fields mapped

    String reason;
    boolean success;
    @SerializedName("eventOut")
    List<ResponseBody> createEventResponse;

    public static class ResponseBody {
        String eventSouceCode;
        String eventSouceName;
        long partyId;
    }
}
