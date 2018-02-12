package com.vitalityactive.va.networking.model;

import com.google.gson.annotations.SerializedName;

public class CaptureLoginEventServiceRequest {
    @SerializedName("userInstructionResponses")
    public UserInstructionResponse[] userInstructionResponses;
    @SerializedName("loginSuccess")
    public boolean loginSuccess;
    @SerializedName("loginDate")
    public String loginDate;

    public static class UserInstructionResponse {
        @SerializedName("id")
        public long id;
        @SerializedName("typeKey")
        public long type;
        @SerializedName("value")
        public String value;

        public UserInstructionResponse(long id, String type, String value) {
            this.id = id;
            this.type = Long.parseLong(type);
            this.value = value;
        }
    }
}
