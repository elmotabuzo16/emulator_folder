package com.vitalityactive.va.myhealth.dto;

import com.vitalityactive.va.myhealth.content.MyHealthContent;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.persistence.models.myhealth.VitalityAgeHealthAttribute;
import com.vitalityactive.va.persistence.models.vhc.HealthAttributeFeedback;
import com.vitalityactive.va.vhc.dto.HealthAttributeFeedbackDTO;

import java.util.ArrayList;
import java.util.List;

public class VitalityAgeHealthAttributeDTO {

    HealthAttributeMetadataDto healthAttributeMetadataDto = null;
    private String attributeTypeCode;
    private Integer attributeTypeKey;
    private String attributeTypeName;
    private List<HealthAttributeFeedbackDTO> healthAttributeFeedbacks = new ArrayList<>();
    private String measuredOn;
    private Integer sourceEventId;
    private String unitofMeasure;
    private String value;

    public VitalityAgeHealthAttributeDTO(VitalityAgeHealthAttribute ageHealthAttribute) {
        this.attributeTypeCode = ageHealthAttribute.attributeTypeCode;
        this.attributeTypeKey = ageHealthAttribute.attributeTypeKey;
        this.attributeTypeName = ageHealthAttribute.attributeTypeName;
        this.measuredOn = ageHealthAttribute.measuredOn;
        this.sourceEventId = ageHealthAttribute.sourceEventId;
        this.unitofMeasure = ageHealthAttribute.unitofMeasure;
        this.value = ageHealthAttribute.value;
        addHealthAttributeFeedback(ageHealthAttribute);
    }

    public VitalityAgeHealthAttributeDTO(AttributeDTO attr) {
        this.attributeTypeCode = attr.attributeTypeCode;
        this.attributeTypeKey = attr.attributeTypeKey;
        this.attributeTypeName = attr.attributeTypeName;
        this.measuredOn = attr.measuredOn;
        this.unitofMeasure = attr.unitofMeasure;
        this.value = attr.value;
        this.attributeTypeKey = attr.attributeTypeKey;
        addHealthAttributeFeedback(attr);
        addHealthAttributeVitalityAgeMetaData(attr);
    }

    public VitalityAgeHealthAttributeDTO(String attributeTypeCode, Integer attributeTypeKey, String attributeTypeName, List<HealthAttributeFeedbackDTO> healthAttributeFeedbacks, String measuredOn, Integer sourceEventId, String unitofMeasure, String value) {
        this.attributeTypeCode = attributeTypeCode;
        this.attributeTypeKey = attributeTypeKey;
        this.attributeTypeName = attributeTypeName;
        this.healthAttributeFeedbacks = healthAttributeFeedbacks;
        this.measuredOn = measuredOn;
        this.sourceEventId = sourceEventId;
        this.unitofMeasure = unitofMeasure;
        this.value = value;
    }

    private void addHealthAttributeVitalityAgeMetaData(AttributeDTO attr) {
        for (HealthAttributeMetadataDto healthAttributeMetadataDto : attr.getHealthAttributeMetadataDtos()) {
            if (VitalityAgeConstants.VITALITY_AGE_METADATA_TYPE_KEY == healthAttributeMetadataDto.getTypeKey()) {
                this.healthAttributeMetadataDto = healthAttributeMetadataDto;
            }
        }
    }

    private void addHealthAttributeFeedback(AttributeDTO attr) {
        for (AttributeFeedbackDTO vitalityAgeFeedback : attr.getAttributeFeedbackDtos()) {
            healthAttributeFeedbacks.add(new HealthAttributeFeedbackDTO(vitalityAgeFeedback));
        }
    }


    private void addHealthAttributeFeedback(VitalityAgeHealthAttribute ageHealthAttribute) {
        for (HealthAttributeFeedback vitalityAgeFeedback : ageHealthAttribute.healthAttributeFeedbacks) {
            healthAttributeFeedbacks.add(new HealthAttributeFeedbackDTO(vitalityAgeFeedback));
        }
    }

    public List<HealthAttributeFeedbackDTO> getHealthAttributeFeedbacks() {
        return healthAttributeFeedbacks;
    }

    public String getMeasuredOn() {
        return measuredOn;
    }

    public Integer getSourceEventId() {
        return sourceEventId;
    }

    public String getValue() {
        return value;
    }

    public boolean isOutdated() {
        if (hasFeedback()) {
            for (HealthAttributeFeedbackDTO healthAttributeFeedback : healthAttributeFeedbacks) {
                if (healthAttributeFeedback.getFeedbackTypeKey() == VitalityAgeConstants.VA_OUTDATED) {
                    return true;
                }
            }
        }
        return false;
    }

    public HealthAttributeFeedbackDTO getEffectiveFeedback() {
        if (hasFeedback()) {
            for (HealthAttributeFeedbackDTO healthAttributeFeedback : healthAttributeFeedbacks) {
                if (healthAttributeFeedback.getFeedbackTypeKey() == VitalityAgeConstants.VA_OUTDATED) {
                    return healthAttributeFeedback;
                }
            }
            return healthAttributeFeedbacks.get(0);
        }
        return null;
    }

    public HealthAttributeFeedbackDTO getActualFeedback() {
        if (hasFeedback()) {
            for (HealthAttributeFeedbackDTO healthAttributeFeedback : healthAttributeFeedbacks) {
                if (MyHealthContent.vitalityAgeCalculated(healthAttributeFeedback.getFeedbackTypeKey())) {
                    return healthAttributeFeedback;
                }
            }
            return healthAttributeFeedbacks.get(0);
        }
        return null;
    }

    public String getVariance() {
        if (healthAttributeMetadataDto != null && healthAttributeMetadataDto.getValue() != null) {
            return MyHealthUtils.getDisplayVitalityAge(healthAttributeMetadataDto.getValue());
        }
        return null;
    }

    public HealthAttributeMetadataDto getHealthAttributeMetadataDto() {
        return healthAttributeMetadataDto;
    }

    public boolean hasFeedback() {
        return (healthAttributeFeedbacks != null && !healthAttributeFeedbacks.isEmpty());
    }

    public Integer getAttributeTypeKey() {
        return attributeTypeKey;
    }
}
