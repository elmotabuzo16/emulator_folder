package com.vitalityactive.va.myhealth.dto;

import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.persistence.models.myhealth.Attribute;
import com.vitalityactive.va.persistence.models.myhealth.HealthAttributeMetadata;
import com.vitalityactive.va.persistence.models.myhealth.Recommendation;
import com.vitalityactive.va.persistence.models.vhc.HealthAttributeFeedback;

import java.util.ArrayList;
import java.util.List;

public class AttributeDTO {
    String attributeTypeCode;
    Integer attributeTypeKey;
    String attributeTypeName;
    String measuredOn;
    Integer sortOrder;
    String unitofMeasure;
    String value;
    String friendlyValue;
    String displayValue;
    EventDTO eventDto;
    List<RecommendationDTO> recommendationDtos = null;
    List<AttributeFeedbackDTO> attributeFeedbackDtos = null;
    List<HealthAttributeMetadataDto> healthAttributeMetadataDtos = null;

    public AttributeDTO() {
    }

    public AttributeDTO(Attribute attribute) {
        this.attributeTypeCode = attribute.getAttributeTypeCode();
        this.attributeTypeKey = attribute.getAttributeTypeKey();
        this.attributeTypeName = attribute.getAttributeTypeName();
        this.measuredOn = attribute.getMeasuredOn();
        this.sortOrder = attribute.getSortOrder();
        this.unitofMeasure = attribute.getUnitofMeasure();
        this.value = attribute.getValue();
        this.friendlyValue = attribute.getFriendlyValue();
        this.eventDto = toEventDto(attribute);
        this.recommendationDtos = new ArrayList<>();
        for (Recommendation recommendation : attribute.getRecommendations()) {
            this.recommendationDtos.add(new RecommendationDTO(recommendation));
        }
        this.attributeFeedbackDtos = new ArrayList<>();
        for (HealthAttributeFeedback healthAttributeFeedback : attribute.getHealthAttributeFeedbacks()) {
            this.attributeFeedbackDtos.add(new AttributeFeedbackDTO(healthAttributeFeedback));
        }
        this.healthAttributeMetadataDtos = new ArrayList<>();
        for (HealthAttributeMetadata healthAttributeMetadata : attribute.getHealthAttributeMetadatas()) {
            this.healthAttributeMetadataDtos.add(new HealthAttributeMetadataDto(healthAttributeMetadata));
        }
    }

    public AttributeDTO(String attributeTypeCode, Integer attributeTypeKey, String attributeTypeName, String friendlyValue, String measuredOn, Integer sortOrder, String unitofMeasure, String value, List<RecommendationDTO> recommendationDtos, List<AttributeFeedbackDTO> attributeFeedbackDtos) {
        this.attributeTypeCode = attributeTypeCode;
        this.attributeTypeKey = attributeTypeKey;
        this.attributeTypeName = attributeTypeName;
        this.measuredOn = measuredOn;
        this.sortOrder = sortOrder;
        this.unitofMeasure = unitofMeasure;
        this.value = value;
        this.friendlyValue = friendlyValue;
        this.recommendationDtos = recommendationDtos;
        this.attributeFeedbackDtos = attributeFeedbackDtos;
    }

    private EventDTO toEventDto(Attribute attribute) {
        if (attribute.getEvent() != null) {
            return new EventDTO(attribute.getEvent());
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

    public String getUnitofMeasure() {
        return unitofMeasure;
    }

    public String getValue() {
        return value;
    }

    public String getFriendlyValue() {
        return friendlyValue;
    }

    private String getEffectiveValue() {
        if (healthAttributeMetadataDtos != null) {
            for (HealthAttributeMetadataDto healthAttributeMetadataDto : healthAttributeMetadataDtos) {
                if (healthAttributeMetadataDto.getTypeKey() == VitalityAgeConstants.ATTRIBUTE_FRIENDLY_VALUE_KEY) {
                    if (healthAttributeMetadataDto.getValue() != null && !healthAttributeMetadataDto.getValue().trim().isEmpty()) {
                        return healthAttributeMetadataDto.getValue();
                    }
                }
            }
        }
        return value;
    }

    private String getEffectiveFriendlyValue() {
        if (healthAttributeMetadataDtos != null) {
            for (HealthAttributeMetadataDto healthAttributeMetadataDto : healthAttributeMetadataDtos) {
                if (healthAttributeMetadataDto.getTypeKey() == VitalityAgeConstants.ATTRIBUTE_FRIENDLY_VALUE_KEY) {
                    if (healthAttributeMetadataDto.getFriendlyValue() != null && !healthAttributeMetadataDto.getFriendlyValue().trim().isEmpty()) {
                        return healthAttributeMetadataDto.getFriendlyValue();
                    }
                }
            }
        }
        return friendlyValue;
    }

    public String getDisplayValue() {
        String effectiveFriendlyValue = getEffectiveFriendlyValue();
        return effectiveFriendlyValue != null ? effectiveFriendlyValue : getEffectiveValue();
    }

    public List<RecommendationDTO> getRecommendationDtos() {
        return recommendationDtos;
    }

    public EventDTO getEventDto() {
        return eventDto;
    }

    public List<AttributeFeedbackDTO> getAttributeFeedbackDtos() {
        return attributeFeedbackDtos;
    }

    public List<HealthAttributeMetadataDto> getHealthAttributeMetadataDtos() {
        return healthAttributeMetadataDtos;
    }
}
