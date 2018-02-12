package com.vitalityactive.va.activerewards.rewards.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.annotations.Required;

public class SelectVoucherServiceResponse {
    @SerializedName("exchangeRewardResponse")
    public Response response;

    public class Response {
        @SerializedName("awardedRewardReferences")
        public List<Reference> awardedRewardReferences;
        @SerializedName("exchangedRewardId")
        public Long exchangedRewardId;
    }

    public class Reference {
        @SerializedName("typeCode")
        public String typeCode;
        @Required
        @SerializedName("typeKey")
        public Integer typeKey;
        @SerializedName("typeName")
        public String typeName;
        @Required
        @SerializedName("value")
        public String value;
    }
}
