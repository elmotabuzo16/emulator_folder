package com.vitalityactive.va.vhc.captureresults;

import android.support.annotation.NonNull;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.constants.ProductFeature;
import com.vitalityactive.va.constants.ProductFeatureType;
import com.vitalityactive.va.persistence.models.vhc.HealthAttributeTypeValidValues;
import com.vitalityactive.va.persistence.models.vhc.ValidOption;
import com.vitalityactive.va.utilities.MethodCallTrace;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vhc.HealthAttributeGroup;
import com.vitalityactive.va.vhc.captureresults.models.CapturedField;
import com.vitalityactive.va.vhc.captureresults.models.CapturedGroup;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;
import com.vitalityactive.va.vhc.dto.MeasurementItem;
import com.vitalityactive.va.vhc.dto.MeasurementItemField;
import com.vitalityactive.va.vhc.dto.UnitAbbreviationDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

public class MeasurementPersister {
    private final HealthAttributeRepository repository;
    private final VHCHealthAttributeContent content;
    private InsurerConfigurationRepository insurerConfigurationRepository;

    public MeasurementPersister(HealthAttributeRepository repository, VHCHealthAttributeContent content,
                                InsurerConfigurationRepository insurerConfigurationRepository) {
        this.repository = repository;
        this.content = content;
        this.insurerConfigurationRepository = insurerConfigurationRepository;
    }

    List<MeasurementItem> buildMeasurementItems() {
        long trace = MethodCallTrace.enterMethod();
        ArrayList<MeasurementItem> items = new ArrayList<>();
        for (HealthAttributeGroup group : repository.getAllHealthAttributeGroups()) {
            items.add(buildMeasurementItem(group));
        }
        MethodCallTrace.exitMethod(trace);
        return items;
    }

    @NonNull
    public MeasurementItem buildMeasurementItem(HealthAttributeGroup healthAttributeGroup) {
        long trace = MethodCallTrace.enterMethod();
        CapturedGroup capturedGroup = getOrCreateCapturedGroup(healthAttributeGroup);
        List<MeasurementItemField> fields = buildMeasurementItemFields(healthAttributeGroup, capturedGroup);
        MethodCallTrace.exitMethod(trace);
        return MeasurementItem.Builder.build(healthAttributeGroup, capturedGroup.getGroupType(), fields);
    }

    @NonNull
    private CapturedGroup getOrCreateCapturedGroup(HealthAttributeGroup healthAttributeGroup) {
        CapturedGroup capturedGroup = repository.getCapturedGroup(healthAttributeGroup);
        if (capturedGroup == null) {
            capturedGroup = createCapturedGroup(healthAttributeGroup);
            repository.persistCapturedGroup(capturedGroup);
        }
        return capturedGroup;
    }

    @NonNull
    private CapturedGroup createCapturedGroup(HealthAttributeGroup healthAttributeGroup) {
        CapturedGroup capturedGroup = buildEmptyCapturedGroup(healthAttributeGroup.getFeatureType(), healthAttributeGroup.getGroupDescription());
        for (String key : repository.getGroupFields(healthAttributeGroup).keySet()) {
            if (healthAttributeGroup.getFeatureType() == ProductFeatureType._VHCBMI && String.valueOf(insurerConfigurationRepository.getBMIEventTypeKey()).equals(key)) {
                continue;
            }
            capturedGroup.addCapturedField(key);
        }
        return capturedGroup;
    }

    @NonNull
    private CapturedGroup buildEmptyCapturedGroup(int featureType, String groupDescription) {
        switch (featureType) {
            case ProductFeatureType._VHCWAISTCIRCUM:
                return new CapturedGroup(GroupType.WAIST_CIRCUMFERENCE, groupDescription);
            case ProductFeatureType._VHCHBA1C:
                return new CapturedGroup(GroupType.HBA1C, groupDescription);
            case ProductFeatureType._VHCBMI:
                return new CapturedGroup(GroupType.BODY_MASS_INDEX, groupDescription);
            case ProductFeatureType._VHCBLOODGLUCOSE:
                return new CapturedGroup(GroupType.BLOOD_GLUCOSE, groupDescription);
            case ProductFeatureType._VHCBLOODPRESSURE:
                return new CapturedGroup(GroupType.BLOOD_PRESSURE, groupDescription);
            case ProductFeatureType._VHCCHOLESTEROL:
                return new CapturedGroup(GroupType.CHOLESTEROL, groupDescription);
            case ProductFeatureType._VHCURINARYPROTEIN:
                return new CapturedGroup(GroupType.URINE_PROTEIN, groupDescription);
            case ProductFeatureType._OTHERBLOODLIPID:
                return new CapturedGroup(GroupType.OTHER_BLOOD_LIPID, groupDescription);
            default:
                return new CapturedGroup(GroupType.UNKNOWN, groupDescription);
        }
    }

    @NonNull
    private List<MeasurementItemField> buildMeasurementItemFields(HealthAttributeGroup healthAttributeGroup, CapturedGroup capturedGroup) {
        if (healthAttributeGroup.getFeatureType() == ProductFeatureType._VHCBLOODPRESSURE) {
            return buildBloodPressureFieldUserInterface(healthAttributeGroup, capturedGroup);
        }

        List<MeasurementItemField> fields = new ArrayList<>();
        for (CapturedField capturedField : capturedGroup.getCapturedFields()) {
            fields.add(buildField(healthAttributeGroup, capturedField));
        }
        return fields;
    }

    @NonNull
    private List<MeasurementItemField> buildBloodPressureFieldUserInterface(HealthAttributeGroup healthAttributeGroup, CapturedGroup capturedGroup) {
        List<MeasurementItemField> fields = new ArrayList<>();
        CapturedField diastolic = capturedGroup.getCapturedField(String.valueOf(insurerConfigurationRepository.getDiastolicBloodPressureEventTypeKey()));
        CapturedField systolic = capturedGroup.getCapturedField(String.valueOf(insurerConfigurationRepository.getSystolicBloodPressureEventTypeKey()));

        if (diastolic != null && systolic != null) {
            fields.add(buildBloodPressureFieldUserInterface(healthAttributeGroup, systolic, diastolic));
        }

        return fields;
    }

    private MeasurementItemField buildBloodPressureFieldUserInterface(HealthAttributeGroup healthAttributeGroup, CapturedField systolic, CapturedField diastolic) {
        String key = String.valueOf(insurerConfigurationRepository.getBloodPressureEventTypeKey());
        String title = healthAttributeGroup.getGroupDescription();

        final UnitsOfMeasure selectedUnitOfMeasure = systolic.getSelectedUnitOfMeasure();
        MeasurementItemField.Builder builder = new MeasurementItemField.Builder(key, title)
                .setSelectedUnit(selectedUnitOfMeasure, content.getUnitOfMeasureSymbol(selectedUnitOfMeasure))
                .setDateTested(systolic.getDateTested())
                .setValues(systolic.getPrimaryValue(), diastolic.getPrimaryValue());
        Map<String, String> groupFields = repository.getGroupFields(healthAttributeGroup);
        String title1 = groupFields.get(systolic.getKey());
        String title2 = groupFields.get(diastolic.getKey());
        builder.setPropertyNames(title1, title2);

        getSubtitle(builder, key);

        //TODO: Enhance the workaround of adding detail of name on Subunit 1 without passing a dedicated instance
        UnitAbbreviationDescription.SubUnit subUnit1 = new UnitAbbreviationDescription.SubUnit(title1, new ValueLimit(0.0f , 0.0f, ""));

        buildUnitOfMeasurementRestrictions(healthAttributeGroup, systolic, builder, subUnit1);
        buildSystolicUnitOfMeasurementRestrictions(healthAttributeGroup, diastolic, builder);
        return builder.build();
    }

    @NonNull
    private MeasurementItemField buildField(HealthAttributeGroup healthAttributeGroup, CapturedField capturedField) {
        long trace = MethodCallTrace.enterMethod();
        String key = capturedField.key;
        String title = repository.getGroupFields(healthAttributeGroup).get(key);
        final UnitsOfMeasure selectedUnitOfMeasure = capturedField.getSelectedUnitOfMeasure();

        MeasurementItemField.Builder builder = new MeasurementItemField.Builder(key, title)
                .setSelectedUnit(selectedUnitOfMeasure, content.getUnitOfMeasureSymbol(selectedUnitOfMeasure))
                .setDateTested(capturedField.getDateTested())
                .setSelectedItem(capturedField.getSelectedItem())
                .setValues(capturedField.getPrimaryValue(), capturedField.getSecondaryValue());

        List<String> properties = repository.getGroupFieldProperties(healthAttributeGroup, key);
        if (properties.size() == 2) {
            builder.setPropertyNames(properties.get(0), properties.get(1));
        } else if (properties.size() == 1) {
            builder.setPropertyNames(properties.get(0), null);
        }

        getSubtitle(builder, key);

        buildUnitOfMeasurementRestrictions(healthAttributeGroup, capturedField, builder);
        buildSelectableItems(healthAttributeGroup, builder, key);
        MethodCallTrace.exitMethod(trace);
        return builder.build();
    }

    private void getSubtitle(MeasurementItemField.Builder builder, String eventTypeKey) {
        // todo: get the subtitle from the group's field
        if (isHDLCholesterol(eventTypeKey)) {
            builder.setSubtitle(content.getGoodCholesterolText());
        } else if (isLDLCholesterol(eventTypeKey)) {
            builder.setSubtitle(content.getBadCholesterolText());
        }
    }

    public boolean isHDLCholesterol(String eventTypeKey) {
        return insurerConfigurationRepository.getProductFeatureTypeKeyFromEventTypeKey(Integer.valueOf(eventTypeKey)) == ProductFeature._HDLCH;
    }

    private boolean isLDLCholesterol(String eventTypeKey) {
        return insurerConfigurationRepository.getProductFeatureTypeKeyFromEventTypeKey(Integer.valueOf(eventTypeKey)) == ProductFeature._LDLCH;
    }

    private void buildUnitOfMeasurementRestrictions(HealthAttributeGroup healthAttributeGroup,
                                                    CapturedField capturedField,
                                                    MeasurementItemField.Builder builder) {
        buildUnitOfMeasurementRestrictions(healthAttributeGroup, capturedField, builder, null);
    }

    private void buildUnitOfMeasurementRestrictions(HealthAttributeGroup healthAttributeGroup,
                                                    CapturedField capturedField,
                                                    MeasurementItemField.Builder builder, UnitAbbreviationDescription.SubUnit subUnitValue) {
        List<HealthAttributeTypeValidValues> validValues = repository.getValidValues(healthAttributeGroup, capturedField.key);
        for (HealthAttributeTypeValidValues validValue : validValues) {
            UnitsOfMeasure unit = validValue.getUnitOfMeasure();
            Float minValue = validValue.getMinValue();
            Float maxValue = validValue.getMaxValue();

            UnitAbbreviationDescription.SubUnit subUnit1 = null, subUnit2 = null;


            final String unitOfMeasureSymbol = content.getUnitOfMeasureSymbol(unit);
            final String unitOfMeasureDisplayName = content.getUnitOfMeasureDisplayName(unit);

            String rangeMessage;
            if (maxValue == null) {
                rangeMessage = buildRangeString(content.getValidationRangeBiggerString(), minValue);
            } else if (minValue == null) {
                rangeMessage = buildRangeString(content.getValidationRangeSmallerString(), maxValue);
            } else {
                rangeMessage = buildRangeString(content.getValidationRange(), minValue, maxValue);
            }

            //TODO: Enhance the workaround of adding detail of name on Subunit 1 without passing a dedicated instance
            if (subUnitValue != null) {
                subUnit1 = new  UnitAbbreviationDescription.SubUnit(subUnitValue.name, new ValueLimit(minValue , maxValue, rangeMessage));
            }


            if (unit.getTypeKey().equals(UnitsOfMeasure.FOOTINCH.getTypeKey())) {
                rangeMessage = buildRangeStringFullyRounded(content.getValidationRange(), minValue, maxValue);

                subUnit1 = new UnitAbbreviationDescription.SubUnit("Feet", new ValueLimit(minValue, maxValue, rangeMessage));
                subUnit2 = new UnitAbbreviationDescription.SubUnit("Inches", new ValueLimit(0f, 11f, "Range 0 - 11"));
            }

            if (unit.getTypeKey().equals(UnitsOfMeasure.STONEPOUND.getTypeKey())) {
                rangeMessage = buildRangeStringFullyRounded(content.getValidationRange(), minValue, maxValue);

                subUnit1 = new UnitAbbreviationDescription.SubUnit("Stone", new ValueLimit(minValue, maxValue, rangeMessage));
                subUnit2 = new UnitAbbreviationDescription.SubUnit("Pound", new ValueLimit(0f, 13f, "Range 0 - 13"));
            }

            builder.addUnit(new UnitAbbreviationDescription(unit, unitOfMeasureSymbol, unitOfMeasureDisplayName, new ValueLimit(minValue, maxValue, rangeMessage), subUnit1, subUnit2));
        }
    }

    private String buildRangeString(String validationString, Float minValue, Float maxValue) {
        if (minValue == null || maxValue == null) {
            return "";
        }

        String minString = ViewUtilities.roundToTwoDecimalsRemoveTrailingZeros(minValue.toString());
        String maxString = ViewUtilities.roundToTwoDecimalsRemoveTrailingZeros(maxValue.toString());

        return String.format(validationString, minString, maxString);
    }


    private String buildRangeStringFullyRounded(String validationString, Float minValue, Float maxValue) {
        if (minValue == null || maxValue == null) {
            return "";
        }

        String minString = ViewUtilities.roundToNoDecimal(minValue);
        String maxString = ViewUtilities.roundToNoDecimal(maxValue);

        return String.format(validationString, minString, maxString);
    }


    private String buildRangeString(String validationString, Float value) {
        if (value == null) {
            return "";
        }

        return String.format(validationString, ViewUtilities.roundToTwoDecimalsRemoveTrailingZeros(value.toString()));
    }

    private void buildSystolicUnitOfMeasurementRestrictions(HealthAttributeGroup healthAttributeGroup,
                                                            CapturedField capturedField,
                                                            MeasurementItemField.Builder builder) {
        List<HealthAttributeTypeValidValues> validValues = repository.getValidValues(healthAttributeGroup, capturedField.key);
        for (HealthAttributeTypeValidValues validValue : validValues) {
            UnitsOfMeasure unit = validValue.getUnitOfMeasure();
            Float minValue = validValue.getMinValue();
            Float maxValue = validValue.getMaxValue();

            final String unitOfMeasureSymbol = content.getUnitOfMeasureSymbol(unit);
            final String unitOfMeasureDisplayName = content.getUnitOfMeasureDisplayName(unit);

            String rangeDetails;
            if (maxValue == null) {
                rangeDetails = buildRangeString(content.getValidationRangeBiggerString(), minValue);
            } else if (minValue == null) {
                rangeDetails = buildRangeString(content.getValidationRangeSmallerString(), maxValue);
            } else {
                rangeDetails = buildRangeString(content.getValidationRange(), minValue, maxValue);
            }
            builder.addSecondaryUnit(new UnitAbbreviationDescription(unit, unitOfMeasureSymbol, unitOfMeasureDisplayName, new ValueLimit(minValue, maxValue, rangeDetails)));
        }
    }


    private void buildSelectableItems(HealthAttributeGroup healthAttributeGroup, MeasurementItemField.Builder builder, String eventTypeKey) {
        List<HealthAttributeTypeValidValues> validValues = repository.getValidValues(healthAttributeGroup, eventTypeKey);
        for (HealthAttributeTypeValidValues validValue : validValues) {
            RealmList<ValidOption> validOptions = validValue.getValidOptions();
            if (validOptions != null && !validOptions.isEmpty()) {
                for (ValidOption validOption : validOptions) {
                    String value = validOption.getValue();
                    builder.addSelectableItem(value, getValidOptionDescription(value), value);
                }
            }
        }
    }

    private String getValidOptionDescription(String validOptionValue) {
        return content.getValidOptionDescription(validOptionValue);
    }

    public boolean isHeight(MeasurementItemField dataItem) {
        return insurerConfigurationRepository.getProductFeatureTypeKeyFromEventTypeKey(Integer.valueOf(dataItem.getFieldKey())) == ProductFeature._HEIGHT;
    }

    public boolean isWeight(MeasurementItemField dataItem) {
        return insurerConfigurationRepository.getProductFeatureTypeKeyFromEventTypeKey(Integer.valueOf(dataItem.getFieldKey())) == ProductFeature._WEIGHT;
    }

    public boolean isBloodPressure(MeasurementItemField dataItem) {
        return insurerConfigurationRepository.getProductFeatureTypeKeyFromEventTypeKey(Integer.valueOf(dataItem.getFieldKey())) == ProductFeature._BLOODPRESSURE;
    }

    public boolean isTotalCholesterol(MeasurementItemField dataItem) {
        return insurerConfigurationRepository.getProductFeatureTypeKeyFromEventTypeKey(Integer.valueOf(dataItem.getFieldKey())) == ProductFeature._TOTALCH;
    }
}
