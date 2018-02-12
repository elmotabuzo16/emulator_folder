package com.vitalityactive.va.membershippass.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by christian.j.p.capin on 11/16/2017.
 */

public class MembershipPassResponse {

    @SerializedName("vitalityMembership")
    public List<MembershipPassResponse.VitalityMembership> sections;

    public static class VitalityMembership {
        @SerializedName("id")
        public String id;
        @SerializedName("currentVitalityMembershipPeriod")
        public List<MembershipPassResponse.CurrentVitalityMembershipPeriod> currentVitalityMembershipPeriod;
        @SerializedName("membershipProducts")
        public List<MembershipPassResponse.MembershipProducts> membershipProduct;
        @SerializedName("stateCategory")
        public List<MembershipPassResponse.StateCategory> stateCategories;
        /*party
        references
        renewalPeriods
        stateCategories*/

    }
    public static class CurrentVitalityMembershipPeriod{
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
        @SerializedName("effectiveTo")
        public String effectiveTo;
    }

    public static class MembershipProducts{
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
        @SerializedName("effectiveTo")
        public String effectiveTo;
        @SerializedName("identifierTypeCode")
        public long identifierTypeCode;
        @SerializedName("identifierTypeKey")
        public long identifierTypeKey;
        @SerializedName("identifierTypeName")
        public long identifierTypeName;

    }

    public static class StateCategory{
        @SerializedName("state")
        public List<MembershipPassResponse.State> stateList;
        @SerializedName("typeCode")
        public String typeCode;
        @SerializedName("typeKey")
        public int typeKey;
        @SerializedName("typeName")
        public String typeName;

    }
    public static class State{
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
        @SerializedName("effectiveTo")
        public String effectiveTo;
        @SerializedName("statusTypeCode")
        public String statusTypeCode;
        @SerializedName("statusTypeKey")
        public int statusTypeKey;
        @SerializedName("statusTypeName")
        public String statusTypeName;
    }
}
