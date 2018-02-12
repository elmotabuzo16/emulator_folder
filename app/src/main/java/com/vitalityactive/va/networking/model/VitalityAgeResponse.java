package com.vitalityactive.va.networking.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class VitalityAgeResponse {

    @SerializedName("healthAttributeReading")
    @Expose
    public List<HealthAttributeReading> healthAttributeReadings = null;

    public static class GetHealthAttributeEvent {

        @SerializedName("eventDate")
        @Expose
        public String eventDate;
        @SerializedName("eventId")
        @Expose
        public Integer eventId;
        @SerializedName("typeCode")
        @Expose
        public String typeCode;
        @SerializedName("typeKey")
        @Expose
        public Integer typeKey;
        @SerializedName("typeName")
        @Expose
        public String typeName;

    }

    public static class HealthAttributeReading {

        @SerializedName("attributeTypeCode")
        @Expose
        public String attributeTypeCode;
        @SerializedName("attributeTypeKey")
        @Expose
        public Integer attributeTypeKey;
        @SerializedName("attributeTypeName")
        @Expose
        public String attributeTypeName;
        @SerializedName("getHealthAttributeEvent")
        @Expose
        public GetHealthAttributeEvent getHealthAttributeEvent;
        @SerializedName("measuredOn")
        @Expose
        public String measuredOn;
        @SerializedName("measurementUnitTypeId")
        @Expose
        public Integer measurementUnitTypeId;
        @SerializedName("value")
        @Expose
        public String value;

    }

}