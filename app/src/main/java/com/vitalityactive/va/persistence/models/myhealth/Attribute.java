package com.vitalityactive.va.persistence.models.myhealth;

import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.vhc.HealthAttributeFeedback;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;


public class Attribute extends RealmObject implements Model {
    private String attributeTypeCode;
    private Integer attributeTypeKey;
    private String attributeTypeName;
    private String measuredOn;
    private Integer sortOrder;
    private Integer sourceEventId;
    private String unitofMeasure;
    private String value;
    private String friendlyValue;
    private Event event;
    private RealmList<Recommendation> recommendations = null;
    private RealmList<HealthAttributeFeedback> healthAttributeFeedbacks = null;
    private RealmList<HealthAttributeMetadata> healthAttributeMetadatas = null;

    public Attribute() {
    }

    public Attribute(HealthAttributeInformationResponse.Attribute attribute) {
        this.attributeTypeCode = attribute.attributeTypeCode;
        this.attributeTypeKey = attribute.attributeTypeKey;
        this.attributeTypeName = attribute.attributeTypeName;
        this.measuredOn = attribute.measuredOn;
        this.sortOrder = attribute.sortOrder;
        this.sourceEventId = attribute.sourceEventId;
        this.unitofMeasure = attribute.unitofMeasure;
        this.value = attribute.value;
        this.friendlyValue = attribute.friendlyValue;
        this.event = toEvent(attribute);
        this.recommendations = toRecommendations(attribute);
        this.healthAttributeFeedbacks = toHealthAttributeFeedback(attribute);
        this.healthAttributeMetadatas = toHealthAttributeMetadata(attribute.healthAttributeMetadatas);
    }

    private RealmList<Recommendation> toRecommendations(HealthAttributeInformationResponse.Attribute attribute) {
        RealmList<Recommendation> recommendations = new RealmList<>();
        if (attribute != null & attribute.recommendations != null) {
            for (HealthAttributeInformationResponse.Recommendation recommendation : attribute.recommendations) {
                recommendations.add(new Recommendation(recommendation));
            }
        }
        return recommendations;
    }

    private RealmList<HealthAttributeFeedback> toHealthAttributeFeedback(HealthAttributeInformationResponse.Attribute attribute) {
        RealmList<HealthAttributeFeedback> feedbacks = new RealmList<>();
        if (attribute != null & attribute.healthAttributeFeedbacks != null) {
            for (HealthAttributeInformationResponse.HealthAttributeFeedback feedback : attribute.healthAttributeFeedbacks) {
                feedbacks.add(new HealthAttributeFeedback(feedback));
            }
        }
        return feedbacks;
    }

    private RealmList<HealthAttributeMetadata> toHealthAttributeMetadata(List<HealthAttributeInformationResponse.HealthAttributeMetadata> healthAttributeMetadatas) {
        RealmList<HealthAttributeMetadata> healthAttributeMetadataOutput = new RealmList<>();
        if (healthAttributeMetadatas != null) {
            for (HealthAttributeInformationResponse.HealthAttributeMetadata healthAttributeMetadata : healthAttributeMetadatas) {
                healthAttributeMetadataOutput.add(new HealthAttributeMetadata(healthAttributeMetadata));
            }
        }
        return healthAttributeMetadataOutput;
    }

    private Event toEvent(HealthAttributeInformationResponse.Attribute attribute) {
        if (attribute.event != null) {
            return new Event(attribute.event);
        }
        return null;
    }

    public String getAttributeTypeCode() {
        return attributeTypeCode;
    }

    public Integer getAttributeTypeKey() {
        return attributeTypeKey;
    }

    public String getAttributeTypeName() {
        return attributeTypeName;
    }

    public String getMeasuredOn() {
        return measuredOn;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public Integer getSourceEventId() {
        return sourceEventId;
    }

    public String getUnitofMeasure() {
        return unitofMeasure;
    }

    public String getValue() {
        return value;
    }

    public String getFriendlyValue() {
        return friendlyValue;
    }

    public Event getEvent() {
        return event;
    }

    public RealmList<Recommendation> getRecommendations() {
        return recommendations;
    }

    public RealmList<HealthAttributeFeedback> getHealthAttributeFeedbacks() {
        return healthAttributeFeedbacks;
    }

    public RealmList<HealthAttributeMetadata> getHealthAttributeMetadatas() {
        return healthAttributeMetadatas;
    }
}
