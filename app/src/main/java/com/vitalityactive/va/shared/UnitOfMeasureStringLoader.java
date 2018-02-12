package com.vitalityactive.va.shared;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vitalityactive.va.R;
import com.vitalityactive.va.UnitsOfMeasure;

public class UnitOfMeasureStringLoader {
    protected final Context context;

    public UnitOfMeasureStringLoader(Context context) {
        this.context = context;
    }

    @NonNull
    private String getBMIAbbreviation() {
        return "";
    }

    @NonNull
    private String getStepsAbbreviation() {
        return getString(R.string.steps_unit_of_measure_9991);
    }

    @NonNull
    private String getAverageHeartRateAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_percentage_abbreviation_9999);
    }

    @NonNull
    private String getKmPerHourDisplayAbbreviation() {
        return getString(R.string.unit_of_measure_kilometer_per_hour_abbreviation);
    }

    @NonNull
    private String getGramAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_gram_abbreviation_9999);
    }

    @NonNull
    private String getKilogramAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_kilogram_abbreviation_9999);
    }

    @NonNull
    private String getMeterAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_meter_abbreviation_9999);
    }

    @NonNull
    private String getKilometerAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_kilometer_abbreviation_9999);
    }

    @NonNull
    private String getInchAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_inch_abbreviation_9999);
    }

    @NonNull
    private String getFootAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_foot_abbreviation_9999);
    }

    @NonNull
    private String getMileAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_mile_abbreviation_9999);
    }

    @NonNull
    private String getOunceAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_ounce_abbreviation_9999);
    }

    @NonNull
    private String getPoundAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_pound_abbreviation_9999);
    }

    @NonNull
    private String getTonAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_ton_abbreviation_9999);
    }

    @NonNull
    private String getCentimeterAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_centimeter_abbreviation_9999);
    }

    @NonNull
    private String getMillimeterOfMercuryAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_millimeter_of_mercury_abbreviation_9999);
    }

    @NonNull
    private String getPercentageAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_percentage_abbreviation_9999);
    }

    @NonNull
    private String getKiloPascalAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_kilopascal_abbreviation_9999);
    }

    @NonNull
    private String getStoneAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_stone_abbreviation_9999);
    }

    @NonNull
    private String getFootInchAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_foot_inch_abbreviation_9999);
    }

    @NonNull
    private String getMilligramsPerDeciliterAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_milligrams_per_deciliter_abbreviation_9999);
    }

    @NonNull
    private String getMilliMolesPerLiterAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_millimoles_per_liter_abbreviation_9999);
    }

    @NonNull
    private String getHoursAbbreviation() {
        return getString(R.string.measurement_hours_565);
    }

    @NonNull
    private String getMinutesAbbreviation() {
        return getString(R.string.measurement_minutes_566);
    }

    @NonNull
    private String getPerWeekAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_per_week_506);
    }

    @NonNull
    private String getPerDayAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_per_day_505);
    }

    @NonNull
    private String getDaysAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_days_abbreviation_641);
    }

    @NonNull
    private String getStonePoundAbbreviation() {
        return getString(R.string.assessment_unit_of_measure_stone_pound_abbreviation_643);
    }

    @NonNull
    private String getKilocaloriesAbbreviation() {
        return getString(R.string.unit_of_measure_kilocalories_abbreviation_852);
    }

    private String getMetersPerSecondAbbreviation() {
        return getString(R.string.unit_of_measure_meter_per_second_abbreviation_853);
    }

    private String getMinutesPerKilometerAbbreviation() {
        return getString(R.string.unit_of_measure_minutes_per_kilometer_abbreviation_854);
    }

    private String getBeatsPerMinuteAbbreviation() {
        return getString(R.string.unit_of_measure_beats_per_minute_abbreviation_855);
    }

    private String getKilocaloriesPerHourAbbreviation() {
        return getString(R.string.unit_of_measure_kilocalories_per_hour_abbreviation_856);
    }

    @NonNull
    private String getMilliMolesPerLiterDisplayName() {
        return getString(R.string.measurement_millimoles_564);
    }

    @NonNull
    private String getMilligramsPerDeciliterDisplayName() {
        return getString(R.string.assessment_unit_of_measure_milligrams_per_deciliter_text_562);
    }

    @NonNull
    private String getFootInchDisplayName() {
        return getString(R.string.assessment_unit_of_measure_foot_inch_text_9999);
    }

    @NonNull
    private String getStoneDisplayName() {
        return getString(R.string.assessment_unit_of_measure_stone_text_9999);
    }

    @NonNull
    private String getKiloPascalDisplayName() {
        return getString(R.string.assessment_unit_of_measure_kilopascal_text_9999);
    }

    @NonNull
    public String getPercentageDisplayName() {
        return getString(R.string.assessment_unit_of_measure_percentage_text_9999);
    }

    @NonNull
    private String getMillimeterOfMercuryDisplayName() {
        return getString(R.string.assessment_unit_of_measure_millimeter_of_mercury_text_9999);
    }

    @NonNull
    private String getCentimeterDisplayName() {
        return getString(R.string.assessment_unit_of_measure_centimeter_text_9999);
    }

    @NonNull
    private String getTonDisplayName() {
        return getString(R.string.assessment_unit_of_measure_ton_text_9999);
    }

    @NonNull
    private String getPoundDisplayName() {
        return getString(R.string.assessment_unit_of_measure_pound_text_9999);
    }

    @NonNull
    private String getOunceDisplayName() {
        return getString(R.string.assessment_unit_of_measure_ounce_text_9999);
    }

    @NonNull
    private String getMileDisplayName() {
        return getString(R.string.assessment_unit_of_measure_mile_text_9999);
    }

    @NonNull
    private String getFootDisplayName() {
        return getString(R.string.assessment_unit_of_measure_foot_text_9999);
    }

    @NonNull
    private String getInchDisplayName() {
        return getString(R.string.assessment_unit_of_measure_inch_text_9999);
    }

    @NonNull
    private String getKilometerDisplayName() {
        return getString(R.string.assessment_unit_of_measure_kilometer_text_9999);
    }

    @NonNull
    private String getMeterDisplayName() {
        return getString(R.string.assessment_unit_of_measure_meter_text_9999);
    }

    @NonNull
    private String getKilogramDisplayName() {
        return getString(R.string.assessment_unit_of_measure_kilogram_text_9999);
    }

    @NonNull
    private String getGramDisplayName() {
        return getString(R.string.assessment_unit_of_measure_gram_text_9999);
    }

    private String getMinutesDisplayName() {
        return getString(R.string.measurement_minutes_566);
    }

    private String getStepsDisplayName() {
        return "";
    }

    private String getAverageHeartRateDisplayName() {
        return getString(R.string.assessment_unit_of_measure_percentage_abbreviation_9999);
    }

    private String getKmPerHourDisplayName() {
        return getString(R.string.unit_of_measure_kilometer_per_hour_text);
    }

    private String getHoursDisplayName() {
        return getString(R.string.measurement_hours_565);
    }

    private String getPerWeekDisplayName() {
        return getString(R.string.assessment_unit_of_measure_per_week_506);
    }

    private String getPerDayDisplayName() {
        return getString(R.string.assessment_unit_of_measure_per_day_505);
    }

    @NonNull
    private String getDaysDisplayName() {
        return getString(R.string.assessment_unit_of_measure_days_text_642);
    }

    @NonNull
    private String getStonePoundDisplayName() {
        return getString(R.string.assessment_unit_of_measure_stone_pound_text_644);
    }

    @NonNull
    public String getUnitOfMeasureDisplayName(UnitsOfMeasure unitOfMeasure) {
        switch (unitOfMeasure) {
            case GRAM:
                return getGramDisplayName();
            case KILOGRAM:
                return getKilogramDisplayName();
            case METER:
                return getMeterDisplayName();
            case KILOMETER:
                return getKilometerDisplayName();
            case INCH:
                return getInchDisplayName();
            case FOOT:
                return getFootDisplayName();
            case MILE:
                return getMileDisplayName();
            case OUNCE:
                return getOunceDisplayName();
            case POUND:
                return getPoundDisplayName();
            case TON:
                return getTonDisplayName();
            case CENTIMETER:
                return getCentimeterDisplayName();
            case MILLIMETEROFMERCURY:
            case SYSTOLIC_MILLIMETER_OF_MERCURY:
            case DIASTOLIC_MILLIMETER_OF_MERCURY:
                return getMillimeterOfMercuryDisplayName();
            case PERCENTAGE:
                return getPercentageDisplayName();
            case KILOPASCAL:
            case DIASTOLIC_KILOPASCAL:
                return getKiloPascalDisplayName();
            case STONE:
                return getStoneDisplayName();
            case FOOTINCH:
                return getFootInchDisplayName();
            case CHOLESTEROL_MG_PER_DL:
            case TRIGLYCERIDE_MG_PER_DL:
            case FASTINGGLUCOSE_MG_PER_DL:
            case LDLCHOLESTEROL_MG_PER_DL:
            case HDLCHOLESTEROL_MG_PER_DL:
            case RANDOMGLUCOSE_MG_PER_DL:
                return getMilligramsPerDeciliterDisplayName();
            case TRIGLYCERIDE_MM_PER_L:
            case FASTINGGLUCOSE_MM_PER_L:
            case LDLCHOLESTEROL_MM_PER_L:
            case HDLCHOLESTEROL_MM_PER_L:
            case RANDOMGLUCOSE_MM_PER_L:
            case TOTALCHOLESTEROL:
                return getMilliMolesPerLiterDisplayName();
            case PER_DAY:
                return getPerDayDisplayName();
            case PER_WEEK:
                return getPerWeekDisplayName();
            case HOURS:
                return getHoursDisplayName();
            case MINUTES:
                return getMinutesDisplayName();
            case STEPS:
                return getStepsDisplayName();
            case AVERAGEHEARTRATE:
                return getAverageHeartRateDisplayName();
            case KILOMETERS_PER_HOUR:
                return getKmPerHourDisplayName();
            case DAYS:
                return getDaysDisplayName();
            case STONEPOUND:
                return getStonePoundDisplayName();
            case KILOCALORIES:
                return getKilocaloriesDisplayName();
            case METERS_PER_SECOND:
                return getMetersPerSecondDisplayName();
            case MINUTES_PER_KILOMETER:
                return getMinutesPerKilometerDisplayName();
            case BEATS_PER_MINUTE:
                return getBeatsPerMinuteDisplayName();
            default:
                return "Unknown";
        }
    }

    private String getBeatsPerMinuteDisplayName() {
        return "TODO";
    }

    private String getMinutesPerKilometerDisplayName() {
        return "TODO";
    }

    private String getMetersPerSecondDisplayName() {
        return "TODO";
    }

    private String getKilocaloriesDisplayName() {
        return "TODO";
    }

    @NonNull
    public String getUnitOfMeasureSymbol(UnitsOfMeasure unitOfMeasure) {
        switch (unitOfMeasure) {
            case GRAM:
                return getGramAbbreviation();
            case KILOGRAM:
                return getKilogramAbbreviation();
            case METER:
                return getMeterAbbreviation();
            case KILOMETER:
                return getKilometerAbbreviation();
            case INCH:
                return getInchAbbreviation();
            case FOOT:
                return getFootAbbreviation();
            case MILE:
                return getMileAbbreviation();
            case OUNCE:
                return getOunceAbbreviation();
            case POUND:
                return getPoundAbbreviation();
            case TON:
                return getTonAbbreviation();
            case CENTIMETER:
                return getCentimeterAbbreviation();
            case MILLIMETEROFMERCURY:
            case SYSTOLIC_MILLIMETER_OF_MERCURY:
            case DIASTOLIC_MILLIMETER_OF_MERCURY:
                return getMillimeterOfMercuryAbbreviation();
            case PERCENTAGE:
                return getPercentageAbbreviation();
            case KILOPASCAL:
            case DIASTOLIC_KILOPASCAL:
                return getKiloPascalAbbreviation();
            case STONE:
                return getStoneAbbreviation();
            case FOOTINCH:
                return getFootInchAbbreviation();
            case CHOLESTEROL_MG_PER_DL:
            case TRIGLYCERIDE_MG_PER_DL:
            case FASTINGGLUCOSE_MG_PER_DL:
            case LDLCHOLESTEROL_MG_PER_DL:
            case HDLCHOLESTEROL_MG_PER_DL:
            case RANDOMGLUCOSE_MG_PER_DL:
                return getMilligramsPerDeciliterAbbreviation();
            case TRIGLYCERIDE_MM_PER_L:
            case FASTINGGLUCOSE_MM_PER_L:
            case LDLCHOLESTEROL_MM_PER_L:
            case HDLCHOLESTEROL_MM_PER_L:
            case RANDOMGLUCOSE_MM_PER_L:
            case TOTALCHOLESTEROL:
                return getMilliMolesPerLiterAbbreviation();
            case PER_DAY:
                return getPerDayAbbreviation();
            case PER_WEEK:
                return getPerWeekAbbreviation();
            case MINUTES:
                return getMinutesAbbreviation();
            case HOURS:
                return getHoursAbbreviation();
            case BMI:
                return getBMIAbbreviation();
            case STEPS:
                return getStepsAbbreviation();
            case AVERAGEHEARTRATE:
                return getAverageHeartRateAbbreviation();
            case KILOMETERS_PER_HOUR:
                return getKmPerHourDisplayAbbreviation();
            case DAYS:
                return getDaysAbbreviation();
            case STONEPOUND:
                return getStonePoundAbbreviation();
            case KILOCALORIES:
                return getKilocaloriesAbbreviation();
            case METERS_PER_SECOND:
                return getMetersPerSecondAbbreviation();
            case MINUTES_PER_KILOMETER:
                return getMinutesPerKilometerAbbreviation();
            case BEATS_PER_MINUTE:
                return getBeatsPerMinuteAbbreviation();
            case KILOCALORIES_PER_HOUR:
                return getKilocaloriesPerHourAbbreviation();
            default:
                return "Unknown";
        }
    }

    @NonNull
    protected String getString(int resourceId) {
        return context.getString(resourceId);
    }
}
