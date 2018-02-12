package com.vitalityactive.va.activerewards.eventdetail;

import android.support.annotation.Nullable;

import com.vitalityactive.va.R;
import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.Date;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.MeasurementContentFromResourceString;

import static com.vitalityactive.va.constants.EventMetadataType._ACTIVEENERGYEXPEND;
import static com.vitalityactive.va.constants.EventMetadataType._AVERAGEHEARTRATE;
import static com.vitalityactive.va.constants.EventMetadataType._AVERAGEPACE;
import static com.vitalityactive.va.constants.EventMetadataType._AVERAGESPEED;
import static com.vitalityactive.va.constants.EventMetadataType._AVGACTIVEENERGYEXPEN;
import static com.vitalityactive.va.constants.EventMetadataType._AVGRESTENERGYEXPEND;
import static com.vitalityactive.va.constants.EventMetadataType._BMI;
import static com.vitalityactive.va.constants.EventMetadataType._BODYHEIGHT;
import static com.vitalityactive.va.constants.EventMetadataType._BODYWEIGHT;
import static com.vitalityactive.va.constants.EventMetadataType._DISTANCE;
import static com.vitalityactive.va.constants.EventMetadataType._DURATION;
import static com.vitalityactive.va.constants.EventMetadataType._ELEVATIONGAIN;
import static com.vitalityactive.va.constants.EventMetadataType._ELEVATIONLOSS;
import static com.vitalityactive.va.constants.EventMetadataType._ENDTIME;
import static com.vitalityactive.va.constants.EventMetadataType._ENERGYEXPENDITURE;
import static com.vitalityactive.va.constants.EventMetadataType._MAXIMUMHEARTRATE;
import static com.vitalityactive.va.constants.EventMetadataType._MAXIMUMPACE;
import static com.vitalityactive.va.constants.EventMetadataType._MAXIMUMPOWER;
import static com.vitalityactive.va.constants.EventMetadataType._MAXIMUMSPEED;
import static com.vitalityactive.va.constants.EventMetadataType._MINIMUMHEARTRATE;
import static com.vitalityactive.va.constants.EventMetadataType._MINIMUMPACE;
import static com.vitalityactive.va.constants.EventMetadataType._MINIMUMPOWER;
import static com.vitalityactive.va.constants.EventMetadataType._MINIMUMSPEED;
import static com.vitalityactive.va.constants.EventMetadataType._STARTTIME;

public class MetadataFormatter {

    public static String getFormattedValueWithUnitOfMeasure(@Nullable MeasurementContentFromResourceString uomProvider,
                                                            String uomId,
                                                            String value,
                                                            int eventMetadataTypeKey) {
        if (uomProvider == null) {
            return value;
        }
        switch (eventMetadataTypeKey) {
            case _DURATION:
                return uomProvider.getHourMinuteSecondString(TextUtilities.getDoubleFromString(value), R.string.AR_event_detail_duration_hh_mm_ss_851); // int value, no need to reformat
            case _STARTTIME:
            case _ENDTIME:
                return uomProvider.getDateString(new Date(value).getMillisecondsSinceEpoch());
            case _DISTANCE:
            case _ENERGYEXPENDITURE:
            case _ACTIVEENERGYEXPEND:
            case _AVGACTIVEENERGYEXPEN:
            case _AVGRESTENERGYEXPEND:
            case _AVERAGESPEED:
            case _MINIMUMSPEED:
            case _MAXIMUMSPEED:
            case _AVERAGEHEARTRATE:
            case _MINIMUMHEARTRATE:
            case _MAXIMUMHEARTRATE:
            case _AVERAGEPACE:
            case _MINIMUMPACE:
            case _MAXIMUMPACE:
            case _ELEVATIONLOSS:
            case _ELEVATIONGAIN:
            case _BODYWEIGHT:
            case _BMI:
            case _MAXIMUMPOWER:
            case _MINIMUMPOWER:
            case _BODYHEIGHT:
                String formattedValue = ViewUtilities.roundToTwoDecimalsRemoveTrailingZeros(value);
                return uomProvider.concatenateWithUOM(UnitsOfMeasure.fromValue(uomId), formattedValue);
        }
        return value;
    }

}
