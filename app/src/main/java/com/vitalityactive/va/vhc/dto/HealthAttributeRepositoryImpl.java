package com.vitalityactive.va.vhc.dto;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.constants.ProductFeatureType;
import com.vitalityactive.va.constants.Reason;
import com.vitalityactive.va.dto.HealthAttributeEvent;
import com.vitalityactive.va.dto.HealthAttributeEventType;
import com.vitalityactive.va.dto.HealthAttributePointsEntry;
import com.vitalityactive.va.dto.HealthAttributePointsEntryReason;
import com.vitalityactive.va.dto.HealthAttributeReading;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.vhc.EventMetaData;
import com.vitalityactive.va.persistence.models.vhc.EventSource;
import com.vitalityactive.va.persistence.models.vhc.HealthAttribute;
import com.vitalityactive.va.persistence.models.vhc.HealthAttributeTypeValidValues;
import com.vitalityactive.va.persistence.models.vhc.ValidOption;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;
import com.vitalityactive.va.vhc.HealthAttributeFeedbackType;
import com.vitalityactive.va.vhc.HealthAttributeGroup;
import com.vitalityactive.va.vhc.captureresults.models.CapturedField;
import com.vitalityactive.va.vhc.captureresults.models.CapturedGroup;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;
import com.vitalityactive.va.vhc.service.HealthAttributeResponse;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.Sort;

public class HealthAttributeRepositoryImpl implements HealthAttributeRepository {
    private final Persister persister;
    private final DataStore dataStore;
    private final InsurerConfigurationRepository insurerConfigurationRepository;
    private VHCHealthAttributeContent vhcHealthAttributeContent;

    public HealthAttributeRepositoryImpl(DataStore dataStore,
                                         InsurerConfigurationRepository insurerConfigurationRepository,
                                         VHCHealthAttributeContent vhcHealthAttributeContent) {

        this.persister = new Persister(dataStore);
        this.dataStore = dataStore;
        this.insurerConfigurationRepository = insurerConfigurationRepository;
        this.vhcHealthAttributeContent = vhcHealthAttributeContent;
    }

    @Override
    public void persistHealthAttributeResponse(HealthAttributeResponse healthAttributeResponse) {
        List<HealthAttributeGroup> healthAttributeGroups = getConfiguredHealthAttributeGroups();

        removeAllModels();

        sortByPotentialPoints(healthAttributeResponse);

        buildGroupsFromEventTypes(healthAttributeResponse, healthAttributeGroups);

        addModelsToDatabase(healthAttributeGroups);
    }

    private void addModelsToDatabase(List<HealthAttributeGroup> healthAttributeGroups) {
        persister.addModels(healthAttributeGroups, new Persister.InstanceCreator<Model, HealthAttributeGroup>() {
            @Override
            public Model create(HealthAttributeGroup model) {
                return model;
            }
        });
    }

    private void buildGroupsFromEventTypes(HealthAttributeResponse healthAttributeResponse, List<HealthAttributeGroup> healthAttributeGroups) {
        for (HealthAttributeResponse.EventType eventType : healthAttributeResponse.eventTypes) {
            final int index = groupContains(String.valueOf(eventType.typeKey), healthAttributeGroups);

            final HealthAttributeEventType healthAttributeEventType = new HealthAttributeEventType(eventType);

            if (index >= 0) {
                final HealthAttributeGroup existingGroup = healthAttributeGroups.get(index);
                existingGroup.addHealthAttributeEventType(healthAttributeEventType);
            }
        }
    }

    private void sortByPotentialPoints(HealthAttributeResponse healthAttributeResponse) {
        Collections.sort(healthAttributeResponse.eventTypes, new Comparator<HealthAttributeResponse.EventType>() {
            @Override
            public int compare(HealthAttributeResponse.EventType first, HealthAttributeResponse.EventType second) {
                return second.totalPotentialPoints - first.totalPotentialPoints;
            }
        });
    }

    private void removeAllModels() {
        dataStore.removeAll(HealthAttributeGroup.class);
        dataStore.removeAll(HealthAttributeEventType.class);
        dataStore.removeAll(HealthAttribute.class);
        dataStore.removeAll(HealthAttributeEvent.class);
        dataStore.removeAll(HealthAttributeTypeValidValues.class);
        dataStore.removeAll(ValidOption.class);
        dataStore.removeAll(EventMetaData.class);
        dataStore.removeAll(HealthAttributeReading.class);
        dataStore.removeAll(HealthAttributePointsEntry.class);
        dataStore.removeAll(HealthAttributePointsEntryReason.class);
        dataStore.removeAll(EventSource.class);
    }

    private List<HealthAttributeGroup> getConfiguredHealthAttributeGroups() {
        final List<Integer> configuredVHCFeatureTypes = insurerConfigurationRepository.getConfiguredVHCFeatureTypes();

        List<HealthAttributeGroup> configuredGroups = new ArrayList<>();

        for (int featureType : configuredVHCFeatureTypes) {
            configuredGroups.add(new HealthAttributeGroup(featureType,
                    getGroupDescription(featureType),
                    getGroupSubtitle(featureType)));
        }

        return configuredGroups;
    }

    @Override
    public String getGroupDescription(int featureType) {
        switch (featureType) {
            case ProductFeatureType._VHCBMI:
                return vhcHealthAttributeContent.getBmiGroupTitle();
            case ProductFeatureType._VHCWAISTCIRCUM:
                return vhcHealthAttributeContent.getWaistCircumferenceGroupTitle();
            case ProductFeatureType._VHCBLOODGLUCOSE:
                return vhcHealthAttributeContent.getGlucoseGroupTitle();
            case ProductFeatureType._VHCBLOODPRESSURE:
                return vhcHealthAttributeContent.getBloodPressureGroupTitle();
            case ProductFeatureType._VHCCHOLESTEROL:
                return vhcHealthAttributeContent.getCholesterolGroupTitle();
            case ProductFeatureType._OTHERBLOODLIPID:
                return vhcHealthAttributeContent.getOtherBloodLipidsGroupTitle();
            case ProductFeatureType._VHCHBA1C:
                return vhcHealthAttributeContent.getHbA1cGroupTitle();
            case ProductFeatureType._VHCURINARYPROTEIN:
                return vhcHealthAttributeContent.getUrinaryProteinGroupTitle();
            default:
                return "Unknown";
        }
    }

    private String getGroupSubtitle(int featureType) {
        switch (featureType) {
            case ProductFeatureType._VHCBMI:
                return vhcHealthAttributeContent.getMeasurable0CaptureContent();
            case ProductFeatureType._VHCWAISTCIRCUM:
                return vhcHealthAttributeContent.getMeasurable1CaptureContent();
            case ProductFeatureType._VHCBLOODGLUCOSE:
                return vhcHealthAttributeContent.getMeasurable2CaptureContent();
            case ProductFeatureType._VHCBLOODPRESSURE:
                return vhcHealthAttributeContent.getMeasurable3CaptureContent();
            case ProductFeatureType._VHCCHOLESTEROL:
            case ProductFeatureType._OTHERBLOODLIPID:
                return vhcHealthAttributeContent.getMeasurable4CaptureContent();
            case ProductFeatureType._VHCHBA1C:
                return vhcHealthAttributeContent.getMeasurable5CaptureContent();
            case ProductFeatureType._VHCURINARYPROTEIN:
                return vhcHealthAttributeContent.getMeasurable6CaptureContent();
            default:
                return "Unknown";
        }
    }

    @Override
    public List<HealthAttributeGroup> getAllHealthAttributeGroups() {
        return dataStore.getModels(HealthAttributeGroup.class, new DataStore.ModelListMapper<HealthAttributeGroup, HealthAttributeGroup>() {
            @Override
            public List<HealthAttributeGroup> mapModels(List<HealthAttributeGroup> models) {
                return dataStore.copyFromDataStore(models);
            }
        });
    }

    @Override
    public long getHealthAttributeGroupCount() {
        return dataStore.getNumberOfModels(HealthAttributeGroup.class);
    }

    @Override
    public void persistCapturedGroup(CapturedGroup capturedGroup) {
        dataStore.addOrUpdate(Collections.singletonList(capturedGroup));
    }

    @Override
    public void persistCapturedData(GroupType groupType,
                                    MeasurementItemField field,
                                    MeasurementProperty property,
                                    String value, boolean valueIsValid) {

        Float floatValue = null;
        if (value != null && !value.isEmpty()) {
            try {
                floatValue = Float.valueOf(value);
            } catch (NumberFormatException ignored) {
            }
        }

        property.setValue(floatValue);
        CapturedGroup capturedGroup = getCapturedGroup(groupType.getTypeKey());

        if (groupType == GroupType.BLOOD_PRESSURE) {
            buildBloodPressureCapturedField(property, valueIsValid, floatValue, capturedGroup);
        } else {
            buildCapturedField(field, property, valueIsValid, floatValue, capturedGroup);
        }

        dataStore.addOrUpdate(Collections.singletonList(capturedGroup));
    }

    private void buildCapturedField(MeasurementItemField field,
                                    MeasurementProperty property,
                                    boolean valueIsValid,
                                    Float floatValue,
                                    CapturedGroup capturedGroup) {

        CapturedField capturedField = capturedGroup.getCapturedField(field.getFieldKey());
        if (property.isPrimaryProperty()) {
            capturedField.setPrimaryValue(floatValue, valueIsValid);
        } else {
            capturedField.setSecondaryValue(floatValue, valueIsValid);
        }
    }

    private void buildBloodPressureCapturedField(MeasurementProperty property,
                                                 boolean valueIsValid,
                                                 Float floatValue,
                                                 CapturedGroup capturedGroup) {

        CapturedField capturedField;
        if (property.isPrimaryProperty()) {
            capturedField = capturedGroup.getCapturedField(String.valueOf(insurerConfigurationRepository.getSystolicBloodPressureEventTypeKey()));
        } else {
            capturedField = capturedGroup.getCapturedField(String.valueOf(insurerConfigurationRepository.getDiastolicBloodPressureEventTypeKey()));
        }
        capturedField.setPrimaryValue(floatValue, valueIsValid);
    }

    @Override
    public void persistSelectedItem(GroupType groupType, MeasurementItemField field, String value) {
        CapturedGroup capturedGroup = getCapturedGroup(groupType.getTypeKey());
        CapturedField capturedField = capturedGroup.getCapturedField(field.getFieldKey());
        capturedField.setSelectedItem(value);
        dataStore.addOrUpdate(Collections.singletonList(capturedGroup));
    }

    @Override
    public void persistUnitOfMeasure(GroupType groupType,
                                     MeasurementItemField field,
                                     UnitAbbreviationDescription unit) {

        CapturedGroup capturedGroup = getCapturedGroup(groupType.getTypeKey());
        if (groupType == GroupType.BLOOD_PRESSURE) {
            for (CapturedField capturedField : capturedGroup.getCapturedFields()) {
                capturedField.setSelectedUnitOfMeasure(unit.getUnitOfMeasure());
            }
        } else {
            CapturedField capturedField = capturedGroup.getCapturedField(field.getFieldKey());
            capturedField.setSelectedUnitOfMeasure(unit.getUnitOfMeasure());
        }
        dataStore.addOrUpdate(Collections.singletonList(capturedGroup));
    }

    @Override
    public void persistDateCaptured(GroupType groupType, MeasurementItemField field, Date date) {
        CapturedGroup capturedGroup = getCapturedGroup(groupType.getTypeKey());
        if (groupType == GroupType.BLOOD_PRESSURE) {
            for (CapturedField capturedField : capturedGroup.getCapturedFields()) {
                capturedField.setDateTested(date.getTime());
            }
        } else {
            CapturedField capturedField = capturedGroup.getCapturedField(field.getFieldKey());
            capturedField.setDateTested(date.getTime());
        }
        dataStore.addOrUpdate(Collections.singletonList(capturedGroup));
    }

    @Override
    public CapturedGroup getCapturedGroup(HealthAttributeGroup group) {
        int mappedTypeKey = getMappedTypeKey(group.getFeatureType());
        return getCapturedGroup(mappedTypeKey);
    }

    private CapturedGroup getCapturedGroup(int groupTypeKey) {
        return dataStore.getModelInstance(CapturedGroup.class, new DataStore.ModelMapper<CapturedGroup, CapturedGroup>() {
            @Override
            public CapturedGroup mapModel(CapturedGroup model) {
                return dataStore.copyFromDataStore(model);
            }
        }, "typeKey", groupTypeKey);
    }

    private int getMappedTypeKey(int groupFeatureTypeKey) {
        switch (groupFeatureTypeKey) {
            case ProductFeatureType._VHCWAISTCIRCUM:
                return GroupType.WAIST_CIRCUMFERENCE.getTypeKey();
            case ProductFeatureType._VHCHBA1C:
                return GroupType.HBA1C.getTypeKey();
            case ProductFeatureType._VHCBMI:
                return GroupType.BODY_MASS_INDEX.getTypeKey();
            case ProductFeatureType._VHCBLOODGLUCOSE:
                return GroupType.BLOOD_GLUCOSE.getTypeKey();
            case ProductFeatureType._VHCBLOODPRESSURE:
                return GroupType.BLOOD_PRESSURE.getTypeKey();
            case ProductFeatureType._VHCCHOLESTEROL:
                return GroupType.CHOLESTEROL.getTypeKey();
            case ProductFeatureType._OTHERBLOODLIPID:
                return GroupType.OTHER_BLOOD_LIPID.getTypeKey();
            case ProductFeatureType._VHCURINARYPROTEIN:
                return GroupType.URINE_PROTEIN.getTypeKey();
        }
        return GroupType.UNKNOWN.getTypeKey();
    }

    @Override
    public List<HealthAttributeTypeValidValues> getValidValues(HealthAttributeGroup group, String fieldKey) {
        for (HealthAttributeEventType type : group.getAllHealthAttributeEventTypes()) {
            if (fieldKey.equals(String.valueOf(type.getTypeKey()))) {
                RealmList<HealthAttribute> healthAttributes = type.getHealthAttributes();
                if (healthAttributes.size() == 0) {
                    return new ArrayList<>();
                }
                return healthAttributes.get(0).getValidValues();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public Map<String, String> getGroupFields(HealthAttributeGroup group) {
        HashMap<String, String> fields = new HashMap<>();
        for (HealthAttributeEventType eventType : group.getAllHealthAttributeEventTypes()) {
            fields.put(String.valueOf(eventType.getTypeKey()), getFieldName(eventType));
        }
        return fields;
    }

    private String getFieldName(HealthAttributeEventType eventType) {
        return vhcHealthAttributeContent.getFieldName(eventType.getTypeKey());
    }

    @Override
    public List<String> getGroupFieldProperties(HealthAttributeGroup group, String fieldKey) {
        for (HealthAttributeEventType type : group.getAllHealthAttributeEventTypes()) {
            if (fieldKey.equals(String.valueOf(type.getTypeKey()))) {
                ArrayList<String> properties = new ArrayList<>();
                for (HealthAttribute healthAttribute : type.getHealthAttributes()) {
                    properties.add(getFieldPropertyName(healthAttribute));
                }
                return properties;
            }
        }
        return Collections.singletonList(fieldKey);
    }

    private String getFieldPropertyName(HealthAttribute healthAttribute) {
        int healthAttributeTypeKey = healthAttribute.getTypeKey();
        return vhcHealthAttributeContent.getFieldPropertyName(healthAttributeTypeKey);
    }

    @Override
    public List<CapturedGroup> getCapturedGroupsWithOneOrMoreCompletelyCapturedFields() {
        return dataStore.getModels(CapturedGroup.class, new DataStore.QueryExecutor<CapturedGroup, RealmQuery<CapturedGroup>>() {
            @Override
            public List<CapturedGroup> executeQueries(RealmQuery<CapturedGroup> initialQuery) {
                return initialQuery.equalTo("capturedFields.fieldCaptureComplete", true).findAll();
            }
        }, new DataStore.ModelListMapper<CapturedGroup, CapturedGroup>() {
            @Override
            public List<CapturedGroup> mapModels(List<CapturedGroup> models) {
                return dataStore.copyFromDataStore(models);
            }
        });
    }

    @Override
    public List<CapturedGroup> getCapturedGroupWithFieldsThatAreNotCompleted() {
        return dataStore.getModels(CapturedGroup.class, new DataStore.QueryExecutor<CapturedGroup, RealmQuery<CapturedGroup>>() {
            @Override
            public List<CapturedGroup> executeQueries(RealmQuery<CapturedGroup> initialQuery) {
                return initialQuery.equalTo("capturedFields.fieldCaptureComplete", false).findAll();
            }
        }, new DataStore.ModelListMapper<CapturedGroup, CapturedGroup>() {
            @Override
            public List<CapturedGroup> mapModels(List<CapturedGroup> models) {
                return dataStore.copyFromDataStore(models);
            }
        });
    }

    @Override
    public List<CapturedField> getCompletelyCapturedFields(CapturedGroup capturedGroup) {
        ArrayList<CapturedField> completelyCapturedFields = new ArrayList<>();
        for (CapturedField capturedField : capturedGroup.getCapturedFields()) {
            if (capturedField.isFieldCaptureComplete()) {
                completelyCapturedFields.add(capturedField);
            }
        }
        return completelyCapturedFields;
    }

    @Override
    public void removeAllCapturedGroups() {
        dataStore.removeAll(CapturedGroup.class);
        dataStore.removeAll(CapturedField.class);
    }

    @Override
    public HealthAttributeDTO getHealthAttribute(final int eventTypeKey,
                                                 final int healthAttributeGroupFeatureType) {
        return dataStore.getModelInstance(HealthAttributeEventType.class, new DataStore.SingleModelQueryExecutor<HealthAttributeEventType, RealmQuery<HealthAttributeEventType>>() {
            @Override
            public HealthAttributeEventType executeQueries(RealmQuery<HealthAttributeEventType> initialQuery) {
                return initialQuery.equalTo("typeKey", eventTypeKey).findFirst();
            }
        }, new DataStore.ModelMapper<HealthAttributeEventType, HealthAttributeDTO>() {
            @Override
            public HealthAttributeDTO mapModel(HealthAttributeEventType model) {
                if (model == null) {
                    return new HealthAttributeDTO();
                }

                EventMetaDataDTO eventMetaDataType = getLatestEventMetaData(eventTypeKey);

                List<HealthAttributeEventDTO> events = getEventsContainingReadings(model.getTypeKey());
                HealthAttributeEventDTO firstEvent = new HealthAttributeEventDTO();

                HealthAttributeReadingDTO latestHealthAttributeReading = new HealthAttributeReadingDTO();

                if (events.size() > 0) {
                    firstEvent = events.get(0);

                    latestHealthAttributeReading = new HealthAttributeReadingDTO();

                    List<HealthAttributeReadingDTO> attributeReadings = firstEvent.getAttributeReadings();
                    if (attributeReadings.size() > 0) {
                        Integer healthAttributeTypeKey = insurerConfigurationRepository.getHealthAttributeTypeKey(eventTypeKey, healthAttributeGroupFeatureType);
                        for (HealthAttributeReadingDTO reading : attributeReadings) {
                            if (healthAttributeTypeKey == reading.getHealthAttributeTypeKey()) {
                                latestHealthAttributeReading = reading;
                                break;
                            }
                        }
                    }
                }

                String sourceSubmitted = "";
                String pointsStatus = "";

                if (firstEvent.getEventSourceKey() == com.vitalityactive.va.constants.EventSource._MOBILEAPP) {
                    sourceSubmitted = vhcHealthAttributeContent.getSelfSubmittedString();
                } else {
                    sourceSubmitted = firstEvent.getEventSourceName();
                }

                pointsStatus = getHealthAttributePointStatus(model, pointsStatus, eventTypeKey, healthAttributeGroupFeatureType);
                String attributeValue = eventMetaDataType.getValue();
                HealthAttributeFeedbackDTO feedback = latestHealthAttributeReading.getFeedback();

                return new HealthAttributeDTO(model.getTypeKey(),
                        sourceSubmitted,
                        pointsStatus,
                        model.getTotalEarnedPoints(),
                        model.getTotalPotentialPoints(),
                        healthAttributeGroupFeatureType == ProductFeatureType._VHCBLOODPRESSURE ? ViewUtilities.removeTrailingZeros(attributeValue) : ViewUtilities.roundToTwoDecimalsRemoveTrailingZeros(attributeValue),
                        latestHealthAttributeReading.getMeasuredOn(),
                        eventMetaDataType.getUnitOfMeasureTypeKey(),
                        feedback.getFeedbackTypeKey(),
                        feedback.getFeedbackTypeName());
            }
        });
    }

    private String getHealthAttributePointStatus(HealthAttributeEventType model, String pointsStatus, int eventTypeKey, int healthAttributeGroupFeatureType) {
        boolean groupAtMaxPoints = isGroupAtMaxPointsEarned(healthAttributeGroupFeatureType);

        int eventTypeReasonKey = getEventTypeReasonKey(eventTypeKey);

        if (groupAtMaxPoints || eventTypeReasonKey == Reason._MAXIMUMPOINTS) {
            pointsStatus = vhcHealthAttributeContent.getMaxPointsEarnedString();
        } else if (eventTypeReasonKey == Reason._NOTWITHINRANGE) {
            int pointsAvailable = model.getTotalPotentialPoints() - model.getTotalEarnedPoints();
            if (pointsAvailable > 0) {
                pointsStatus = String.format(vhcHealthAttributeContent.getEarnMorePointsString(), pointsAvailable);
            }
        } else {
            pointsStatus = getEventTypeReasonName(eventTypeKey);
        }

        return pointsStatus;
    }

    private boolean isGroupAtMaxPointsEarned(int healthAttributeGroupFeatureType) {
        List<Integer> eventTypeKeys = insurerConfigurationRepository.getEventTypeKeys(healthAttributeGroupFeatureType);

        int groupMaxPoints = 0;
        int groupEarnedPoints = 0;

        for (Integer eventTypeKey : eventTypeKeys) {
            int attributePotentialPoints = getAttributePotentialPoints(eventTypeKey);
            if (attributePotentialPoints > groupMaxPoints) {
                groupMaxPoints = attributePotentialPoints;
            }

            groupEarnedPoints += getAttributeEarnedpoints(eventTypeKey);
        }

        return groupEarnedPoints >= groupMaxPoints;
    }

    private int getAttributeEarnedpoints(Integer eventTypeKey) {
        return dataStore.getFieldValue(HealthAttributeEventType.class, "typeKey", eventTypeKey, new DataStore.FieldAccessor<HealthAttributeEventType, Integer>() {
            @NonNull
            @Override
            public Integer getField(@Nullable HealthAttributeEventType model) {
                return model == null ? 0 : model.getTotalEarnedPoints();
            }
        });
    }

    private int getAttributePotentialPoints(Integer eventTypeKey) {
        return dataStore.getFieldValue(HealthAttributeEventType.class, "typeKey", eventTypeKey, new DataStore.FieldAccessor<HealthAttributeEventType, Integer>() {
            @NonNull
            @Override
            public Integer getField(@Nullable HealthAttributeEventType model) {
                return model == null ? 0 : model.getTotalPotentialPoints();
            }
        });
    }

    private String getEventTypeReasonName(int eventTypeKey) {
        return dataStore.getFieldValue(HealthAttributeEventType.class, "typeKey", eventTypeKey, new DataStore.FieldAccessor<HealthAttributeEventType, String>() {
            @NonNull
            @Override
            public String getField(@Nullable HealthAttributeEventType model) {
                return model == null ? "" : model.getReasonName();
            }
        });
    }

    private int getEventTypeReasonKey(int eventTypeKey) {
        return dataStore.getFieldValue(HealthAttributeEventType.class, "typeKey", eventTypeKey, new DataStore.FieldAccessor<HealthAttributeEventType, Integer>() {
            @NonNull
            @Override
            public Integer getField(@Nullable HealthAttributeEventType model) {
                return model == null ? -1 : model.getReasonKey();
            }
        });
    }

    private EventMetaDataDTO getLatestEventMetaData(int eventTypeKey) {
        List<HealthAttributeEventDTO> events = getEventsContainingReadings(eventTypeKey);

        if (events.size() > 0) {
            HealthAttributeEventDTO eventDTO = events.get(0);
            List<EventMetaDataDTO> eventMetaDataDTOs = eventDTO.getEventMetaDataDTOs();
            if (eventMetaDataDTOs.size() > 0) {
                return eventMetaDataDTOs.get(0);
            }
        }

        return new EventMetaDataDTO();
    }

    @NonNull
    private List<HealthAttributeEventDTO> getEventsContainingReadings(final int typeKey) {
        return dataStore.getModels(HealthAttributeEvent.class, new DataStore.QueryExecutor<HealthAttributeEvent, RealmQuery<HealthAttributeEvent>>() {
            @Override
            public List<HealthAttributeEvent> executeQueries(RealmQuery<HealthAttributeEvent> initialQuery) {
                return initialQuery.equalTo("eventTypeTypeKey", typeKey).findAllSorted("eventDateTime", Sort.DESCENDING);
            }
        }, new DataStore.ModelListMapper<HealthAttributeEvent, HealthAttributeEventDTO>() {
            @Override
            public List<HealthAttributeEventDTO> mapModels(List<HealthAttributeEvent> models) {
                List<HealthAttributeEventDTO> healthAttributeEventDTOs = new ArrayList<>();

                for (HealthAttributeEvent event : models) {
                    if (event.getAttributeReadings().size() > 0) {
                        healthAttributeEventDTOs.add(new HealthAttributeEventDTO(event));
                    }
                }

                return healthAttributeEventDTOs;
            }
        });
    }

    @Override
    public List<HealthAttributeDTO> getHealthAttributes(int healthAttributeGroupFeatureType) {
        List<HealthAttributeDTO> healthAttributes = new ArrayList<>();

        List<Integer> eventTypeKeys = insurerConfigurationRepository.getEventTypeKeys(healthAttributeGroupFeatureType);
        if (healthAttributeGroupFeatureType == ProductFeatureType._VHCBLOODPRESSURE) {

            String sourceString = null;
            String pointsStatus = "";
            int potentialPoints = 0;
            int earnedPoints = 0;
            String systolicValue = null;
            String diastolicValue = null;
            String measuredOn = "";
            String unitOfMeasureTypeKey = "";
            Integer feedbackTypeKey = null;
            String feedbackTypeName = null;

            for (int eventTypeKey : eventTypeKeys) {
                HealthAttributeDTO healthAttribute = getHealthAttribute(eventTypeKey, healthAttributeGroupFeatureType);

                if (eventTypeKey == insurerConfigurationRepository.getDiastolicBloodPressureEventTypeKey()) {
                    diastolicValue = healthAttribute.getValue();
                    measuredOn = healthAttribute.getMeasuredOn();
                    unitOfMeasureTypeKey = healthAttribute.getUnitOfMeasureTypeKey();

                } else if (eventTypeKey == insurerConfigurationRepository.getSystolicBloodPressureEventTypeKey()) {
                    systolicValue = healthAttribute.getValue();
                } else {
                    sourceString = healthAttribute.getSourceString();
                    pointsStatus = healthAttribute.getPointsStatus();
                    potentialPoints = healthAttribute.getPotentialPoints();
                    earnedPoints = healthAttribute.getEarnedPoints();
                }

                if (feedbackTypeKey == null && healthAttribute.getFeedbackTypeKey() != null) {
                    feedbackTypeKey = healthAttribute.getFeedbackTypeKey();
                    feedbackTypeName = healthAttribute.getFeedbackTypeName();
                }

                if (feedbackTypeKey != null
                        && healthAttribute.getFeedbackTypeKey() != null
                        && HealthAttributeFeedbackType.fromValue(healthAttribute.getFeedbackTypeKey()).inHealthyRange()) {
                    feedbackTypeKey = healthAttribute.getFeedbackTypeKey();
                    feedbackTypeName = healthAttribute.getFeedbackTypeName();
                }
            }

            String value = "";
            if (systolicValue != null && diastolicValue != null) {
                value = buildBloodPressureString(systolicValue, diastolicValue);
            }

            HealthAttributeDTO bloodPressureAttribute = new HealthAttributeDTO(
                    insurerConfigurationRepository.getBloodPressureEventTypeKey(),
                    sourceString,
                    pointsStatus,
                    earnedPoints,
                    potentialPoints,
                    value,
                    measuredOn,
                    unitOfMeasureTypeKey,
                    feedbackTypeKey,
                    feedbackTypeName);

            healthAttributes.add(bloodPressureAttribute);

        } else {
            for (int eventTypeKey : eventTypeKeys) {
                HealthAttributeDTO healthAttribute = getHealthAttribute(eventTypeKey, healthAttributeGroupFeatureType);
                healthAttributes.add(healthAttribute);
            }
        }

        sortByPotentialPointsThenByAlphabet(healthAttributes);

        return healthAttributes;
    }

    @NonNull
    private String buildBloodPressureString(String systolicValue, String diastolicValue) {
        return systolicValue + "/" + diastolicValue;
    }

    private void sortByPotentialPointsThenByAlphabet(List<HealthAttributeDTO> healthAttributeDTOs) {
        Collections.sort(healthAttributeDTOs, new Comparator<HealthAttributeDTO>() {
            @Override
            public int compare(HealthAttributeDTO first, HealthAttributeDTO second) {
                int compareInt = second.getPotentialPoints() - first.getPotentialPoints();
                if (compareInt != 0) return compareInt;

                String firstFieldName = vhcHealthAttributeContent.getFieldName(first.getEventTypeKey());
                String secondFieldName = vhcHealthAttributeContent.getFieldName(second.getEventTypeKey());

                return firstFieldName.compareTo(secondFieldName);
            }
        });
    }

    @Override
    public List<HealthAttributeGroupDTO> getHealthAttributeGroups(List<Integer> configuredVHCFeatureTypes) {
        List<HealthAttributeGroupDTO> groups = new ArrayList<>();

        for (int featureTypeKey : configuredVHCFeatureTypes) {
            List<HealthAttributeDTO> healthAttributes = getHealthAttributes(featureTypeKey);

            Date measuredOn = new Date();
            int maxPotentialPoints = 0;
            int totalEarnedPoints = 0;

            boolean hasGroupToDisplay = false;
            boolean groupHasNoReadings = true;
            Integer groupFeedBackTypeKey = null;

            int systolicBloodPressureEventTypeKey = insurerConfigurationRepository.getSystolicBloodPressureEventTypeKey();
            int diastolicBloodPressureEventTypeKey = insurerConfigurationRepository.getDiastolicBloodPressureEventTypeKey();

            for (HealthAttributeDTO healthAttribute : healthAttributes) {
                if (healthAttribute.hasReading()) {
                    if (systolicBloodPressureEventTypeKey == healthAttribute.getEventTypeKey()
                            || diastolicBloodPressureEventTypeKey == healthAttribute.getEventTypeKey()) {
                        groupHasNoReadings = handleBloodPressureReading(featureTypeKey, systolicBloodPressureEventTypeKey, diastolicBloodPressureEventTypeKey);
                    } else {
                        groupHasNoReadings = false;
                    }

                    if (healthAttribute.getPotentialPoints() > 0) {
                        hasGroupToDisplay = true;
                        measuredOn = getDate(healthAttribute.getMeasuredOn());
                    }

                    groupFeedBackTypeKey = setGroupFeedbackIfNotSet(groupFeedBackTypeKey, healthAttribute);
                    groupFeedBackTypeKey = setGroupFeedbackIfAttributeValueIsOutOfHealthyRange(groupFeedBackTypeKey, healthAttribute);
                }

                totalEarnedPoints += healthAttribute.getEarnedPoints();

                if (healthAttribute.getPotentialPoints() > maxPotentialPoints) {
                    maxPotentialPoints += healthAttribute.getPotentialPoints();
                }
            }

            groups.add(new HealthAttributeGroupDTO(getGroupDescription(featureTypeKey),
                    groupHasNoReadings,
                    groupFeedBackTypeKey,
                    measuredOn,
                    featureTypeKey,
                    maxPotentialPoints,
                    totalEarnedPoints,
                    (!hasGroupToDisplay && !groupHasNoReadings)));
        }

        return groups;
    }

    private Integer setGroupFeedbackIfAttributeValueIsOutOfHealthyRange(Integer groupFeedBackTypeKey, HealthAttributeDTO healthAttribute) {
        if (groupFeedBackTypeKey != null
                && healthAttribute.getFeedbackTypeKey() != null
                && HealthAttributeFeedbackType.fromValue(healthAttribute.getFeedbackTypeKey()).inHealthyRange()) {
            groupFeedBackTypeKey = healthAttribute.getFeedbackTypeKey();
        }
        return groupFeedBackTypeKey;
    }

    @Nullable
    private Integer setGroupFeedbackIfNotSet(Integer groupFeedBackTypeKey, HealthAttributeDTO healthAttribute) {
        if (groupFeedBackTypeKey == null && healthAttribute.getFeedbackTypeKey() != null) {
            groupFeedBackTypeKey = healthAttribute.getFeedbackTypeKey();
        }
        return groupFeedBackTypeKey;
    }

    private boolean handleBloodPressureReading(int featureTypeKey, int systolicBloodPressureEventTypeKey, int diastolicBloodPressureEventTypeKey) {
        boolean groupHasNoReadings;
        Integer systolicLinkedKey = insurerConfigurationRepository.getHealthAttributeTypeKey(systolicBloodPressureEventTypeKey, featureTypeKey);
        Integer diastolicLinkedKey = insurerConfigurationRepository.getHealthAttributeTypeKey(diastolicBloodPressureEventTypeKey, featureTypeKey);

        EventMetaDataDTO systolicReading = getLatestEventMetaData(systolicLinkedKey);
        EventMetaDataDTO diastolicReading = getLatestEventMetaData(diastolicLinkedKey);
        groupHasNoReadings = !(systolicReading != null && diastolicReading != null);
        return groupHasNoReadings;
    }

    @Override
    public List<HealthAttributeDTO> getHealthAttributesWithReadings(int healthAttributeGroupFeatureType) {
        List<HealthAttributeDTO> healthAttributeDTOs = new ArrayList<>();

        for (HealthAttributeDTO healthAttributeDTO : getHealthAttributes(healthAttributeGroupFeatureType)) {
            if (healthAttributeDTO.hasReading()) {
                healthAttributeDTOs.add(healthAttributeDTO);
            }
        }

        return healthAttributeDTOs;
    }

    private Date getDate(String measuredOn) {
        Date date = new Date();

        if (measuredOn != null) {
            try {
                date = NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(measuredOn);
            } catch (ParseException ignored) {
            }
        }
        return date;
    }

    private int groupContains(String eventTypeKey, List<HealthAttributeGroup> healthAttributeGroups) {
        int index = -1;

        int featureType = insurerConfigurationRepository.getFeatureTypeFromEventTypeKey(Integer.valueOf(eventTypeKey));

        for (int i = 0; i < healthAttributeGroups.size(); i++) {
            HealthAttributeGroup currentGroup = healthAttributeGroups.get(i);
            if (Objects.equals(currentGroup.getFeatureType(), featureType)) {
                index = i;
                break;
            }
        }

        return index;
    }
}
