package com.vitalityactive.va.myhealth.dto;

import com.vitalityactive.va.networking.model.HealthAttributeFeedbackResponse;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;

import java.util.List;

public interface MyHealthRepository {

    void persistMostRecentHealthAttributeFeedback(HealthAttributeFeedbackResponse healthAttributeFeedbackResponse);

    VitalityAgeHealthAttributeDTO getVitalityAgeHealthAttributeFeedback();

    boolean sectionHasSubsection(long sectionTypeKey);

    List<HealthInformationSectionDTO> getHealthInformationSectionByParentTypeKey(int parentTypeKey);

    void persistHealthAttributeTipResponse(HealthAttributeInformationResponse healthAttributeInformationResponse);

    List<HealthInformationSectionDTO> getHealthInformationSections();

    HealthInformationSectionDTO getHealthInformationSectionByTypeKey(long typeKey);

    AttributeDTO getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(int sectionTypeKey, int attributeTypeKey);
}
