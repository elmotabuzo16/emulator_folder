package com.vitalityactive.va.networking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HealthAttributeInformationResponse {

    @SerializedName("sections")
    public List<Section> sections = null;

    public static class Attribute {
        @SerializedName("attributeTypeCode")
        public String attributeTypeCode;

        @SerializedName("attributeTypeKey")
        public Integer attributeTypeKey;

        @SerializedName("attributeTypeName")
        public String attributeTypeName;

        @SerializedName("event")
        public Event event;

        @SerializedName("healthAttributeFeedbacks")
        public List<HealthAttributeFeedback> healthAttributeFeedbacks = null;

        @SerializedName("measuredOn")
        public String measuredOn;

        @SerializedName("recommendations")
        public List<Recommendation> recommendations = null;

        @SerializedName("sortOrder")
        public Integer sortOrder;

        @SerializedName("sourceEventId")
        public Integer sourceEventId;

        @SerializedName("unitofMeasure")
        public String unitofMeasure;

        @SerializedName("value")
        public String value;

        @SerializedName("friendlyValue")
        @Expose
        public String friendlyValue;

        @SerializedName("healthAttributeMetadatas")
        @Expose
        public List<HealthAttributeMetadata> healthAttributeMetadatas;

    }

    public static class Event {

        @SerializedName("applicableTo")
        public Long applicableTo;

        @SerializedName("dateLogged")
        public String dateLogged;

        @SerializedName("effectiveDateTime")
        public String effectiveDateTime;

        @SerializedName("eventId")
        public Integer eventId;

        @SerializedName("eventSourceTypeCode")
        public String eventSourceTypeCode;

        @SerializedName("eventSourceTypeKey")
        public Integer eventSourceTypeKey;

        @SerializedName("eventSourceTypeName")
        public String eventSourceTypeName;

        @SerializedName("reportedBy")
        public Long reportedBy;

        @SerializedName("typeCode")
        public String typeCode;

        @SerializedName("typeKey")
        public Integer typeKey;

        @SerializedName("typeName")
        public String typeName;

    }


    public static class FeedbackTip {

        @SerializedName("note")
        public String note;

        @SerializedName("sortOrder")
        public Integer sortOrder;

        @SerializedName("typeCode")
        public String typeCode;

        @SerializedName("typeKey")
        public Integer typeKey;

        @SerializedName("typeName")
        public String typeName;

    }

    public static class HealthAttributeFeedback {

        @SerializedName("feedbackTips")
        public List<FeedbackTip> feedbackTips = null;

        @SerializedName("feedbackTypeCode")
        public String feedbackTypeCode;

        @SerializedName("feedbackTypeKey")
        public Integer feedbackTypeKey;

        @SerializedName("feedbackTypeName")
        public String feedbackTypeName;

        @SerializedName("feedbackTypeTypeCode")
        public String feedbackTypeTypeCode;

        @SerializedName("feedbackTypeTypeKey")
        public Integer feedbackTypeTypeKey;

        @SerializedName("feedbackTypeTypeName")
        public String feedbackTypeTypeName;
        @SerializedName("whyIsThisImportant")
        @Expose
        public String whyIsThisImportant;

    }

    public static class Recommendation {

        @SerializedName("fromValue")
        public String fromValue;

        @SerializedName("toValue")
        public String toValue;

        @SerializedName("unitOfMeasureId")
        public Integer unitOfMeasureId;

        @SerializedName("value")
        public String value;
        @SerializedName("friendlyValue")
        @Expose
        public String friendlyValue;

    }

    public static class HealthAttributeMetadata {

        @SerializedName("measurementUnitId")
        @Expose
        public Long measurementUnitId;

        @SerializedName("typeCode")
        @Expose
        public String typeCode;

        @SerializedName("typeKey")
        @Expose
        public Integer typeKey;

        @SerializedName("typeName")
        @Expose
        public String typeName;

        @SerializedName("value")
        @Expose
        public String value;

        @SerializedName("friendlyValue")
        @Expose
        public String friendlyValue;

    }

    public static class Section {

        @SerializedName("sections")
        @Expose
        public List<Section> sections;

        @SerializedName("attributes")
        @Expose
        public List<Attribute> attributes;

        @SerializedName("sortOrder")
        public Integer sortOrder;

        @SerializedName("typeCode")
        public String typeCode;

        @SerializedName("typeKey")
        public Integer typeKey;

        @SerializedName("typeName")
        public String typeName;

    }

}
