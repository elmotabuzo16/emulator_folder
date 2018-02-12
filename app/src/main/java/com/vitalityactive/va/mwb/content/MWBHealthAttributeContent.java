package com.vitalityactive.va.mwb.content;

import android.support.annotation.NonNull;

import com.vitalityactive.va.UnitsOfMeasure;

/**
 * Created by christian.j.p.capin on 2/6/2018.
 */

public interface MWBHealthAttributeContent extends MWBContent {
    @NonNull
    String getValueString();

    @NonNull
    String getHeightString();

    @NonNull
    String getWeightString();

    @NonNull
    String getTestedOnString();

    @NonNull
    String getProofSectionTitle();

    @NonNull
    String getFastingGlucoseString();

    @NonNull
    String getRandomGlucoseString();

    @NonNull
    String getSystolicBloodPressureString();

    @NonNull
    String getDiastolicBloodPressureString();

    @NonNull
    String getTotalCholesterolString();

    @NonNull
    String getHDLCholesterolString();

    @NonNull
    String getLDLCholesterolString();

    @NonNull
    String getTriglyceridesCholesterolString();

    @NonNull
    String getHbA1cString();

    @NonNull
    String getUrinaryProteinString();

    @NonNull
    String getWaistCircumferenceString();

    @NonNull
    String getFieldName(int eventTypeKey);

    @NonNull
    String getUnitOfMeasureSymbol(UnitsOfMeasure unitsOfMeasure);

    @NonNull
    String getUnitOfMeasureDisplayName(UnitsOfMeasure unitOfMeasure);

    @NonNull
    String getBMIString();

    @NonNull
    String getGoodCholesterolText();

    @NonNull
    String getBadCholesterolText();

    @NonNull
    String getInHealthyRangeText();

    @NonNull
    String getOutOfHealthyRangeText();

    @NonNull
    String getUrinaryProteinNegativeString();

    @NonNull
    String getUrinaryProteinNeutralString();

    @NonNull
    String getUrinaryProteinPositiveString();

    @NonNull
    String getBloodPressureString();

    @NonNull
    String getValidOptionDescription(String validOptionValue);

    @NonNull
    String getFieldPropertyName(int healthAttributeTypeKey);

    @NonNull
    String getSelfSubmittedString();

    @NonNull
    String getValidationRangeBiggerString();

    @NonNull
    String getValidationRangeSmallerString();

    @NonNull
    String getValidationRange();

    @NonNull
    String getEarnMorePointsString();

    @NonNull
    String getMaxPointsEarnedString();

    @NonNull
    String getValidationRequiredString();

    @NonNull
    String getCholesterolRatio();

    @NonNull
    String getFootInchString();

    @NonNull
    String getStonePoundString();

    @NonNull
    String getValidationRequiredForPointsString();
}
