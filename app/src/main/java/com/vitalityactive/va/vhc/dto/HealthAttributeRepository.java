package com.vitalityactive.va.vhc.dto;

import com.vitalityactive.va.persistence.models.vhc.HealthAttributeTypeValidValues;
import com.vitalityactive.va.vhc.HealthAttributeGroup;
import com.vitalityactive.va.vhc.captureresults.models.CapturedField;
import com.vitalityactive.va.vhc.captureresults.models.CapturedGroup;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;
import com.vitalityactive.va.vhc.service.HealthAttributeResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HealthAttributeRepository {
    void persistHealthAttributeResponse(HealthAttributeResponse healthAttributeResponse);

    String getGroupDescription(int featureType);

    List<HealthAttributeGroup> getAllHealthAttributeGroups();

    long getHealthAttributeGroupCount();

    void persistCapturedGroup(CapturedGroup capturedGroup);

    void persistCapturedData(GroupType groupType, MeasurementItemField field, MeasurementProperty property, String value, boolean valueIsValid);

    void persistSelectedItem(GroupType groupType, MeasurementItemField field, String value);

    void persistUnitOfMeasure(GroupType groupType, MeasurementItemField field, UnitAbbreviationDescription unit);

    void persistDateCaptured(GroupType groupType, MeasurementItemField field, Date captured);

    CapturedGroup getCapturedGroup(HealthAttributeGroup group);

    List<HealthAttributeTypeValidValues> getValidValues(HealthAttributeGroup group, String fieldKey);

    Map<String, String> getGroupFields(HealthAttributeGroup group);

    List<String> getGroupFieldProperties(HealthAttributeGroup group, String fieldKey);

    List<CapturedGroup> getCapturedGroupsWithOneOrMoreCompletelyCapturedFields();

    List<CapturedGroup> getCapturedGroupWithFieldsThatAreNotCompleted();

    List<CapturedField> getCompletelyCapturedFields(CapturedGroup capturedGroup);

    void removeAllCapturedGroups();

    HealthAttributeDTO getHealthAttribute(int typeKey, int healthAttributeGroupFeatureType);

    List<HealthAttributeDTO> getHealthAttributes(int healthAttributeGroupFeatureType);

    List<HealthAttributeGroupDTO> getHealthAttributeGroups(List<Integer> configuredVHCFeatureTypes);

    List<HealthAttributeDTO> getHealthAttributesWithReadings(int healthAttributeGroupFeatureType);
}
