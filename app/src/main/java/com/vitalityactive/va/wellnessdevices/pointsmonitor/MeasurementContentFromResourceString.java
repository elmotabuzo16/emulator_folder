package com.vitalityactive.va.wellnessdevices.pointsmonitor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Spanned;
import android.text.TextUtils;

import com.vitalityactive.va.R;
import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.shared.UnitOfMeasureStringLoader;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;
import com.vitalityactive.va.wellnessdevices.Condition;
import com.vitalityactive.va.wellnessdevices.Utils;
import com.vitalityactive.va.wellnessdevices.dto.PointsConditionsDto;

import java.math.BigDecimal;

import static com.vitalityactive.va.constants.EventMetadataType._AVERAGEHEARTRATE;
import static com.vitalityactive.va.constants.EventMetadataType._AVERAGESPEED;
import static com.vitalityactive.va.constants.EventMetadataType._DURATION;
import static com.vitalityactive.va.constants.EventMetadataType._ENERGYEXPENDITURE;
import static com.vitalityactive.va.constants.EventMetadataType._TOTALSTEPS;
import static com.vitalityactive.va.utilities.ViewUtilities.removeTrailingZeros;

public class MeasurementContentFromResourceString extends UnitOfMeasureStringLoader {

    public MeasurementContentFromResourceString(Context context) {
        super(context);
    }

    public String getHourMinuteSecondString(double seconds, @StringRes int dateStringId) {
        long secondsLong = Math.round(seconds);
        long hours = secondsLong / 3600;
        secondsLong %= 3600;
        long minutes = secondsLong / 60;
        secondsLong %= 60;
        return getString(dateStringId, hours, minutes, secondsLong);
    }

    public String getPointsConditionUOMString(PointsConditionsDto pointsConditionsDto) {
        UnitsOfMeasure unitsOfMeasure = TextUtilities.isNullOrWhitespace(pointsConditionsDto.getUnitOfMeasure()) ?
                getPointsConditionUnitsOfMeasureForEventMetadataTypeKey(pointsConditionsDto.getMetadataTypeKey()) :
                UnitsOfMeasure.fromValue(pointsConditionsDto.getUnitOfMeasure());

        return getUnitOfMeasureDisplayName(unitsOfMeasure);

    }

    @NonNull
    @Override
    public String getPercentageDisplayName() {
        return getString(R.string.assessment_unit_of_measure_percentage_abbreviation_9999);
    }

    private UnitsOfMeasure getPointsConditionUnitsOfMeasureForEventMetadataTypeKey(int eventMetadataTypeKey) {
        switch (eventMetadataTypeKey) {
            case _TOTALSTEPS:
                return UnitsOfMeasure.STEPS;
            case _DURATION:
                return UnitsOfMeasure.MINUTES;
            case _ENERGYEXPENDITURE:
                return UnitsOfMeasure.KILOCALORIES_PER_HOUR;
            case _AVERAGEHEARTRATE:
                return UnitsOfMeasure.AVERAGEHEARTRATE;
            case _AVERAGESPEED:
                return UnitsOfMeasure.KILOMETERS_PER_HOUR;
            default:
                return UnitsOfMeasure.UNKNOWN;
        }
    }

    @NonNull
    public String getString(int resourceId, Object... formatArgs) {
        return context.getString(resourceId, formatArgs);
    }

    public String concatenateWithUOM(UnitsOfMeasure unitOfMeasure, String value) {
        if (UnitsOfMeasure.UNKNOWN.equals(unitOfMeasure)) {
            return value;
        }
        String uomString = getUnitOfMeasureSymbol(unitOfMeasure);
        if (TextUtilities.isNullOrWhitespace(uomString)) {
            return value;
        } else {
            return getValueWithUnitOfMeasure(value, uomString);
        }
    }

    private String getValueWithUnitOfMeasure(String value, String uom) {
        return isUnitOfMeasureAfterValue() ? (uom + " " + value) : (value + " " + uom);
    }

    private boolean isUnitOfMeasureAfterValue() {
        return getString(R.string.current_locale).equals("ja-JP");
    }

    public String getDateString(long millisecondsSinceEpoch) {
        return DateFormattingUtilities.formatDateMonthYearHoursMinutes(context, millisecondsSinceEpoch);
    }

    private String getStringWithUom(@StringRes int stringId,
                                    String parameter,
                                    String uom) {
        return getString(stringId, getBoldString(getValueWithUom(parameter, uom)));
    }

    private String getBoldString(String text) {
        return "<B>" + text + "</B>";
    }

    private String getValueWithUom(String value, String uom) {
        return isUomAfterValue() ? (value + " " + uom) : (uom + " " + value);
    }

    private String getUomFromProvider(PointsConditionsDto pointsConditionsDto) {
        return getPointsConditionUOMString(pointsConditionsDto);
    }

    private boolean isUomAfterValue() {
        return !context.getResources().getString(R.string.current_locale).equals("ja-JP");
    }

    public Spanned getConditionString(PointsConditionsDto pointsConditionsDto) {
        Condition condition = Condition.getConditionByKey(pointsConditionsDto.getMetadataTypeKey());
        String conditionText;

        boolean greaterStraight = false;
        boolean lessStraight = false;
        String greaterValue = "";
        String lessValue = "";

        if (TextUtils.isEmpty(pointsConditionsDto.getGreaterThanOrEqualTo()) &&
                !TextUtils.isEmpty(pointsConditionsDto.getGreaterThan())) {
            greaterStraight = true;
            greaterValue = pointsConditionsDto.getGreaterThan();
        } else if (!TextUtils.isEmpty(pointsConditionsDto.getGreaterThanOrEqualTo()) &&
                TextUtils.isEmpty(pointsConditionsDto.getGreaterThan())) {
            greaterStraight = false;
            greaterValue = pointsConditionsDto.getGreaterThanOrEqualTo();
        } else if (!TextUtils.isEmpty(pointsConditionsDto.getGreaterThanOrEqualTo()) &&
                !TextUtils.isEmpty(pointsConditionsDto.getGreaterThan())) {
            BigDecimal greater = new BigDecimal(pointsConditionsDto.getGreaterThan());
            BigDecimal greaterOrEqual = new BigDecimal(pointsConditionsDto.getGreaterThanOrEqualTo());
            if (greater.compareTo(greaterOrEqual) > 0) {
                greaterStraight = true;
                greaterValue = pointsConditionsDto.getGreaterThan();
            } else {
                greaterStraight = false;
                greaterValue = pointsConditionsDto.getGreaterThanOrEqualTo();
            }
        }

        if (TextUtils.isEmpty(pointsConditionsDto.getLessThanOrEqualTo()) &&
                !TextUtils.isEmpty(pointsConditionsDto.getLessThan())) {
            lessStraight = true;
            lessValue = pointsConditionsDto.getLessThan();
        } else if (!TextUtils.isEmpty(pointsConditionsDto.getLessThanOrEqualTo()) &&
                TextUtils.isEmpty(pointsConditionsDto.getLessThan())) {
            lessStraight = false;
            lessValue = pointsConditionsDto.getLessThanOrEqualTo();
        } else if (!TextUtils.isEmpty(pointsConditionsDto.getLessThanOrEqualTo()) &&
                !TextUtils.isEmpty(pointsConditionsDto.getLessThan())) {
            BigDecimal less = new BigDecimal(pointsConditionsDto.getLessThan());
            BigDecimal lessOrEqual = new BigDecimal(pointsConditionsDto.getLessThanOrEqualTo());
            if (less.compareTo(lessOrEqual) > 0) {
                lessStraight = true;
                lessValue = pointsConditionsDto.getLessThan();
            } else {
                lessStraight = false;
                lessValue = pointsConditionsDto.getLessThanOrEqualTo();
            }
        }

        int uomCondition;
        if (!TextUtils.isEmpty(greaterValue) && !TextUtils.isEmpty(lessValue)) {
            if (lessStraight && greaterStraight) {
                uomCondition = condition.getTextLessAndGreaterThan();
            } else if (lessStraight && !greaterStraight) {
                uomCondition = condition.getTextLessAndGreaterOrEqualThan();
            } else if (!lessStraight && greaterStraight) {
                uomCondition = condition.getTextLessOrEqualAndGreaterThan();
            } else {
                uomCondition = condition.getTextLessOrEqualAndGreaterOrEqualThan();
            }
            conditionText = getStringWithUom(uomCondition,
                    removeTrailingZeros(greaterValue),
                    removeTrailingZeros(lessValue),
                    getUomFromProvider(pointsConditionsDto));
        } else {
            if (!TextUtils.isEmpty(greaterValue)) {
                if (greaterStraight) {
                    uomCondition = condition.getTextGreaterThan();
                } else {
                    uomCondition = condition.getTextGreaterOrEqualThan();
                }
                conditionText = getStringWithUom(uomCondition,
                        removeTrailingZeros(greaterValue),
                        getUomFromProvider(pointsConditionsDto));
            } else {
                if (lessStraight) {
                    uomCondition = condition.getTextLessThan();
                } else {
                    uomCondition = condition.getTextLessOrEqualThan();
                }
                conditionText = getStringWithUom(uomCondition,
                        removeTrailingZeros(lessValue),
                        getUomFromProvider(pointsConditionsDto));
            }
        }

        return Utils.fromHtml(conditionText);
    }
    private String getStringWithUom(@StringRes int stringId,
                                    String parameter1,
                                    String parameter2,
                                    String uom) {
        return getString(stringId,
                getBoldString(getValueWithUom(parameter1, uom)),
                getBoldString(getValueWithUom(parameter2, uom)));
    }
}
