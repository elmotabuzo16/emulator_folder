package com.vitalityactive.va.networking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class HealthAttributeFeedbackResponse {

    @SerializedName("healthAttribute")
    @Expose
    public List<HealthAttribute> healthAttributes = null;

    public static class HealthAttribute {

        @SerializedName("attributeTypeCode")
        @Expose
        public String attributeTypeCode;
        @SerializedName("attributeTypeKey")
        @Expose
        public Integer attributeTypeKey;
        @SerializedName("attributeTypeName")
        @Expose
        public String attributeTypeName;
        @SerializedName("healthAttributeFeedbacks")
        @Expose
        public List<HealthAttributeFeedback> healthAttributeFeedbacks = null;
        @SerializedName("healthAttributeMetadatas")
        @Expose
        public List<HealthAttributeMetadata> healthAttributeMetadatas = null;
        @SerializedName("measuredOn")
        @Expose
        public String measuredOn;
        @SerializedName("sourceEventId")
        @Expose
        public Integer sourceEventId;
        @SerializedName("unitofMeasure")
        @Expose
        public String unitofMeasure;
        @SerializedName("value")
        @Expose
        public String value;

    }

    public static class HealthAttributeFeedback_ {

        @SerializedName("feedbackTypeCode")
        @Expose
        public String feedbackTypeCode;
        @SerializedName("feedbackTypeKey")
        @Expose
        public Integer feedbackTypeKey;
        @SerializedName("feedbackTypeName")
        @Expose
        public String feedbackTypeName;
        @SerializedName("feedbackTypeTypeCode")
        @Expose
        public String feedbackTypeTypeCode;
        @SerializedName("feedbackTypeTypeKey")
        @Expose
        public Integer feedbackTypeTypeKey;
        @SerializedName("feedbackTypeTypeName")
        @Expose
        public String feedbackTypeTypeName;

    }

    public static class HealthAttributeMetadata {

        @SerializedName("healthAttributeFeedbacks")
        @Expose
        public List<HealthAttributeFeedback_> healthAttributeFeedbacks = null;
        @SerializedName("measurementUnitId")
        @Expose
        public Integer measurementUnitId;
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
    }

    public static class HealthAttributeFeedback {

        @SerializedName("feedbackTypeCode")
        @Expose
        public String feedbackTypeCode;
        @SerializedName("feedbackTypeKey")
        @Expose
        public Integer feedbackTypeKey;
        @SerializedName("feedbackTypeName")
        @Expose
        public String feedbackTypeName;
        @SerializedName("feedbackTypeTypeCode")
        @Expose
        public String feedbackTypeTypeCode;
        @SerializedName("feedbackTypeTypeKey")
        @Expose
        public Integer feedbackTypeTypeKey;
        @SerializedName("feedbackTypeTypeName")
        @Expose
        public String feedbackTypeTypeName;

    }

}


