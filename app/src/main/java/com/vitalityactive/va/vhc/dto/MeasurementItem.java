package com.vitalityactive.va.vhc.dto;

import android.support.annotation.NonNull;

import com.vitalityactive.va.R;
import com.vitalityactive.va.vhc.HealthAttributeGroup;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;

import java.util.List;

public class MeasurementItem {
    public final GroupType type;
    public final String title;
    public final String description;
    public List<MeasurementItemField> measurementItemFields;
    public int iconResourceId;

    public MeasurementItem(GroupType type,
                           String title,
                           String description,
                           List<MeasurementItemField> measurementItemFields,
                           int iconResourceId) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.measurementItemFields = measurementItemFields;
        this.iconResourceId = iconResourceId;
    }

    public static class Builder {
        public static MeasurementItem build(HealthAttributeGroup healthAttributeGroup, GroupType type, List<MeasurementItemField> measurementItemFields) {
            switch (type) {
                case BODY_MASS_INDEX:
                    return bodyMassIndex(healthAttributeGroup, measurementItemFields);
                case WAIST_CIRCUMFERENCE:
                    return waistCircumference(healthAttributeGroup, measurementItemFields);
                case BLOOD_PRESSURE:
                    return bloodPressure(healthAttributeGroup, measurementItemFields);
                case BLOOD_GLUCOSE:
                    return bloodGlucose(healthAttributeGroup, measurementItemFields);
                case CHOLESTEROL:
                    return cholesterol(healthAttributeGroup, measurementItemFields);
                case OTHER_BLOOD_LIPID:
                    return otherBloodLipids(healthAttributeGroup, measurementItemFields);
                case HBA1C:
                    return hBA1c(healthAttributeGroup, measurementItemFields);
                case URINE_PROTEIN:
                    return urinaryProtein(healthAttributeGroup, measurementItemFields);
                default:
                    return bodyMassIndex(healthAttributeGroup, measurementItemFields);
            }
        }

        @NonNull
        public static MeasurementItem bodyMassIndex(HealthAttributeGroup healthAttributeGroup, List<MeasurementItemField> measurementItemFields) {

            return new MeasurementItem(GroupType.BODY_MASS_INDEX,
                    healthAttributeGroup.getGroupDescription(),
                    healthAttributeGroup.getGroupSubTitle(),
                    measurementItemFields,
                    R.drawable.health_measure_bmi
            );
        }

        @NonNull
        public static MeasurementItem waistCircumference(HealthAttributeGroup healthAttributeGroup, List<MeasurementItemField> measurementItemFields) {
            return new MeasurementItem(
                    GroupType.WAIST_CIRCUMFERENCE,
                    healthAttributeGroup.getGroupDescription(),
                    healthAttributeGroup.getGroupSubTitle(),
                    measurementItemFields,
                    R.drawable.health_measure_waist_circumference
            );
        }

        @NonNull
        public static MeasurementItem bloodGlucose(HealthAttributeGroup healthAttributeGroup, List<MeasurementItemField> measurementItemFields) {
            return new MeasurementItem(GroupType.BLOOD_GLUCOSE,
                    healthAttributeGroup.getGroupDescription(),
                    healthAttributeGroup.getGroupSubTitle(),
                    measurementItemFields,
                    R.drawable.health_measure_bloodglucose
            );
        }

        @NonNull
        public static MeasurementItem bloodPressure(HealthAttributeGroup healthAttributeGroup, List<MeasurementItemField> measurementItemFields) {
            return new MeasurementItem(GroupType.BLOOD_PRESSURE,
                    healthAttributeGroup.getGroupDescription(),
                    healthAttributeGroup.getGroupSubTitle(),
                    measurementItemFields,
                    R.drawable.health_measure_bloodpressure
            );
        }

        @NonNull
        public static MeasurementItem cholesterol(HealthAttributeGroup healthAttributeGroup, List<MeasurementItemField> measurementItemFields) {
            return new MeasurementItem(GroupType.CHOLESTEROL,
                    healthAttributeGroup.getGroupDescription(),
                    healthAttributeGroup.getGroupSubTitle(),
                    measurementItemFields,
                    R.drawable.health_measure_cholesterol
            );
        }

        @NonNull
        public static MeasurementItem otherBloodLipids(HealthAttributeGroup healthAttributeGroup, List<MeasurementItemField> measurementItemFields) {
            return new MeasurementItem(GroupType.OTHER_BLOOD_LIPID,
                    healthAttributeGroup.getGroupDescription(),
                    healthAttributeGroup.getGroupSubTitle(),
                    measurementItemFields,
                    R.drawable.health_measure_cholesterol
            );
        }

        @NonNull
        public static MeasurementItem hBA1c(HealthAttributeGroup healthAttributeGroup, List<MeasurementItemField> measurementItemFields) {
            return new MeasurementItem(GroupType.HBA1C,
                    healthAttributeGroup.getGroupDescription(),
                    healthAttributeGroup.getGroupSubTitle(),
                    measurementItemFields,
                    R.drawable.health_measure_hba_1_c
            );
        }

        @NonNull
        public static MeasurementItem urinaryProtein(HealthAttributeGroup group, List<MeasurementItemField> fields) {
            return new MeasurementItem(GroupType.URINE_PROTEIN,
                    group.getGroupDescription(),
                    group.getGroupSubTitle(),
                    fields,
                    R.drawable.health_measure_urine
            );
        }
    }
}
