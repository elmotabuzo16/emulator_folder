package com.vitalityactive.va.wellnessdevices;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.PointsEntryType;

public enum WdEventType {
    HEART_RATE(PointsEntryType._HEARTRATE, R.string.physical_activity_item_heart_rate_text_439, R.drawable.heart_rate, R.drawable.heart_rate_detail, R.string.potential_points_how_to_calculate_heart_rate_title_483, R.string.potential_points_how_to_calculate_heart_rate_484, R.color.wellnessdevices_blue),
    SPEED(PointsEntryType._SPEED, R.string.WDA_physical_activity_item_speed_text_441, R.drawable.speed, R.drawable.speed_detail, R.string.potential_points_how_to_calculate_speed_title_498, R.string.potential_points_how_to_calculate_speed_message_501, R.color.wellnessdevices_blue),
    DISTANCE(PointsEntryType._DISTANCE, R.string.physical_activity_item_distance_text_440, R.drawable.distance, R.drawable.distance_detail, 0, 0, R.color.wellnessdevices_blue),
    STEPS(PointsEntryType._STEPS, R.string.physical_activity_item_steps_text_442, R.drawable.steps, R.drawable.steps_detail, R.string.potential_points_how_to_calculate_steps_title_497, R.string.potential_points_how_to_calculate_steps_message_500, R.color.wellnessdevices_blue),
    CALORIES_BURNED(PointsEntryType._ENERGYEXPEND, R.string.physical_activity_item_calorlies_burned_text_443, R.drawable.calories, R.drawable.calories_detail, R.string.potential_points_how_to_calculate_calories_burned_title_499, R.string.potential_points_how_to_calculate_calories_burned_message_502, R.color.wellnessdevices_blue),
    CALORIES_ACTIVE(0, R.string.physical_activity_item_activeCalories_text_444, R.drawable.calories, R.drawable.calories_detail, R.string.potential_points_how_to_calculate_calories_burned_title_499, R.string.potential_points_how_to_calculate_calories_burned_message_502, R.color.wellnessdevices_blue),
    WEIGHT(0, R.string.WDA_health_measurements_item_weight_text_446, R.drawable.weight, R.drawable.weight_detail, 0, 0, R.color.jungle_green),
    HEIGHT(0, R.string.WDA_health_measurements_item_height_text_447, R.drawable.height, R.drawable.height_detail, 0, 0, R.color.jungle_green),
    SLEEP(0, R.string.WDA_other_item_sleep_text_449, R.drawable.sleep, R.drawable.sleep_detail, 0, 0, R.color.SilverDark),
    BMI_CAPTURED(PointsEntryType._BMI, R.string.measurement_body_mass_index_title_134, R.drawable.bmi_status, R.drawable.bmi_status, 0, 0, R.color.jungle_green),
    BMI_HEALTHY_RANGE(PointsEntryType._BMIHR, R.string.measurement_body_mass_index_title_134, R.drawable.bmi_status, R.drawable.bmi_status, 0, 0, R.color.jungle_green),
    GLUCOSE_CAPTURED(PointsEntryType._BLOODGLUCOSE, R.string.measurement_glucose_title_136, R.drawable.glucose_status, R.drawable.glucose_status, 0, 0, R.color.jungle_green),
    GLUCOSE_HEALTHY_RANGE(PointsEntryType._GLUCOSEHR, R.string.measurement_glucose_title_136, R.drawable.glucose_status, R.drawable.glucose_status, 0, 0, R.color.jungle_green),
    BLOOD_PRESSURE_CAPTURED(PointsEntryType._BLOODPRESSURE, R.string.measurement_blood_pressure_title_137, R.drawable.blood_pressure_status, R.drawable.blood_pressure_status, 0, 0, R.color.jungle_green),
    BLOOD_PRESSURE_HEALTHY_RANGE(PointsEntryType._BLOODPRESSUREHR, R.string.measurement_blood_pressure_title_137, R.drawable.blood_pressure_status, R.drawable.blood_pressure_status, 0, 0, R.color.jungle_green),
    VHR_CAPTURED(PointsEntryType._VHRASSMNTCOMPLETED, R.string.home_card_card_section_title_291, R.drawable.vitality_health_review_status, R.drawable.vitality_health_review_status, 0, 0, R.color.jungle_green),
    NON_SMOKERS_CAPTURED(PointsEntryType._NONSMOKERSDECLRTN, R.string.home_card_card_title_96, R.drawable.non_smokers_status, R.drawable.non_smokers_status, 0, 0, R.color.jungle_green),
    LDL_CHOLESTEROL_CAPTURED(PointsEntryType._LDLCHOLESTEROL, R.string.measurement_ldl_title_151, R.drawable.screenings_status, R.drawable.screenings_status, 0, 0, R.color.jungle_green),
    LDL_CHOLESTEROL_HEALTHY_RANGE(PointsEntryType._LDLCHOLESTEROLHR, R.string.measurement_ldl_title_151, R.drawable.screenings_status, R.drawable.screenings_status, 0, 0, R.color.jungle_green),
    TOTAL_CHOLESTEROL_CAPTURED(PointsEntryType._TOTCHOLESTEROL, R.string.measurement_total_cholesterol_title_149, R.drawable.screenings_status, R.drawable.screenings_status, 0, 0, R.color.jungle_green),
    HBA1C_CAPTURED(PointsEntryType._HBA1C, R.string.measurement_hba1c_title_139, R.drawable.screenings_status, R.drawable.screenings_status, 0, 0, R.color.jungle_green),
    HBA1C_HEALTHY_RANGE(PointsEntryType._HBA1CHR, R.string.measurement_hba1c_title_139, R.drawable.hba_1_c_status, R.drawable.hba_1_c_status, 0, 0, R.color.jungle_green),
    URINARY_PROTEIN_CAPTURED(PointsEntryType._URINEPROTIEN, R.string.measurement_urine_protein_title_283, R.drawable.urine_protein_status, R.drawable.urine_protein_status, 0, 0, R.color.jungle_green),
    URINARY_PROTEIN_HEALTHY_RANGE(PointsEntryType._URINEPROTIENHR, R.string.measurement_urine_protein_title_283, R.drawable.urine_protein_status, R.drawable.urine_protein_status, 0, 0, R.color.jungle_green),
    GYM_VISITS(PointsEntryType._GYMVISIT, R.string.measurement_urine_protein_title_283, R.drawable.distance, R.drawable.distance_detail, 0, 0, R.color.wellnessdevices_blue);

    private int drawableIconColour;
    private final int typeKey;
    private final
    @DrawableRes
    int drawableIcon;
    private final
    @DrawableRes
    int drawableIconSmall;
    private final
    @StringRes
    int name;
    private final
    @StringRes
    int header;
    private final
    @StringRes
    int description;

    WdEventType(int typeKey,
                int name,
                int drawableIcon,
                int drawableIconSmall,
                int header,
                int description, int drawableIconColour) {
        this.typeKey = typeKey;
        this.name = name;
        this.drawableIcon = drawableIcon;
        this.drawableIconSmall = drawableIconSmall;
        this.header = header;
        this.description = description;
        this.drawableIconColour = drawableIconColour;
    }

    public int getDrawableIconColour() {
        return drawableIconColour;
    }

    public int getKey() {
        return typeKey;
    }

    public int getDrawableIcon() {
        return drawableIcon;
    }

    public int getDrawableIconSmall() {
        return drawableIconSmall;
    }

    public int getName() {
        return name;
    }

    public int getHeader() {
        return header;
    }

    public int getDescription() {
        return description;
    }

    public static WdEventType getEventTypeByKey(int pointsEntryTypeKey) {
        for (WdEventType eventType : WdEventType.values()) {
            if (eventType.getKey() == pointsEntryTypeKey) {
                return eventType;
            }
        }

        return HEART_RATE;
    }
}
