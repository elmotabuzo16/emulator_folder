package com.vitalityactive.va.shared.questionnaire.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionnaireSubmitResponse {

    @SerializedName("assessmentOuts")
    public List<AssessmentOut> assessmentOuts;

    @SerializedName("healthAttributeMetadatas")
    public List<HealthAttributeMetadata> healthAttributeMetadatas;

    public static class AssessmentOut {

        @SerializedName("eventId")
        @Expose
        public Integer eventId;
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("questionnaireFeedback")
        @Expose
        public QuestionnaireFeedback questionnaireFeedback;
    }

    public static class Feedback {

        @SerializedName("feedbackTypeCode")
        @Expose
        public String feedbackTypeCode;
        @SerializedName("feedbackTypeKey")
        @Expose
        public Integer feedbackTypeKey;
        @SerializedName("feedbackTypeName")
        @Expose
        public String feedbackTypeName;
        @SerializedName("sortOrderId")
        @Expose
        public Integer sortOrderId;
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

    public static class HealthAttributeMetadata {

        @SerializedName("healthAttributeFeedbacks")
        @Expose
        public List<HealthAttributeFeedback> healthAttributeFeedbacks = null;
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

    public static class QuestionnaireFeedback {
        @SerializedName("feedbacks")
        @Expose
        public List<Feedback> feedbacks = null;
        @SerializedName("templateCode")
        @Expose
        public String templateCode;
        @SerializedName("templateVersion")
        @Expose
        public String templateVersion;
    }
}
