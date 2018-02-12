package com.vitalityactive.va.vitalitystatus.earningpoints;


import android.content.Context;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.PointsEntryType;

import static com.vitalityactive.va.constants.PointsEntryType._BMI;

class PointsItemDetailProvider {
    static String getItemText(int pointsEntryType, Context context) {
        int placeholder;
        switch (pointsEntryType) {
            case _BMI:
                placeholder = R.string.Status_PointsEntry_BMI857;
                break;
            case PointsEntryType._BMIHR:
                placeholder = R.string.Status_PointsEntry_BMIHR858;
                break;
            case PointsEntryType._BLOODGLUCOSE:
                placeholder = R.string.Status_PointsEntry_BloodGlucose865;
                break;
            case PointsEntryType._BLOODPRESSURE:
                placeholder = R.string.Status_PointsEntry_BloodPressure859;
                break;
            case PointsEntryType._BLOODPRESSUREHR:
                placeholder = R.string.Status_PointsEntry_BloodPressureHR860;
                break;
            case PointsEntryType._CHOLESTEROLHR:
                placeholder = R.string.Status_PointsEntry_CholesterolHR861;
                break;
            case PointsEntryType._CYCLING:
                placeholder = R.string.Status_PointsEntry_Cycling_862;
                break;
            case PointsEntryType._DEVICELINKING:
                placeholder = R.string.Status_PointsEntry_DeviceLinking895;
                break;
            case PointsEntryType._DISTANCE:
                placeholder = R.string.Status_PointsEntry_Distance863;
                break;
            case PointsEntryType._ENERGYEXPEND:
                placeholder = R.string.Status_PointsEntry_EnergyExpend864;
                break;
            case PointsEntryType._GLUCOSEHR:
                placeholder = R.string.Status_PointsEntry_GlucoseHR866;
                break;
            case PointsEntryType._GYMVISIT:
                placeholder = R.string.Status_PointsEntry_GymVisit867;
                break;
            case PointsEntryType._HBA1C:
                placeholder = R.string.Status_PointsEntry_HbA1c868;
                break;
            case PointsEntryType._HBA1CHR:
                placeholder = R.string.Status_PointsEntry_HbA1cHR869;
                break;
            case PointsEntryType._HEARTRATE:
                placeholder = R.string.Status_PointsEntry_Heartrate871;
                break;
            case PointsEntryType._LDLCHOLESTEROL:
                placeholder = R.string.Status_PointsEntry_LDLCholesterol872;
                break;
            case PointsEntryType._LDLCHOLESTEROLHR:
                placeholder = R.string.Status_PointsEntry_LDLCholesterolHR873;
                break;
            case PointsEntryType._LIPIDRATIO:
                placeholder = R.string.Status_PointsEntry_LipidRatio874;
                break;
            case PointsEntryType._LIPIDRATIOHR:
                placeholder = R.string.Status_PointsEntry_LipidRatioHR875;
                break;
            case PointsEntryType._MANUALPOINTS:
                placeholder = R.string.Status_PointsEntry_ManualPointsAlloction879;
                break;
            case PointsEntryType._MEASUREDFITCOMPLETE:
                placeholder = R.string.Status_PointsEntry_MeasuredFitComplete880;
                break;
            case PointsEntryType._MWBPSYCHOLOGICAL:
                placeholder = R.string.Status_PointsEntry_MWBPsychological876;
                break;
            case PointsEntryType._MWBSOCIAL:
                placeholder = R.string.Status_PointsEntry_MWBSocial877;
                break;
            case PointsEntryType._MWBSTRESSOR:
                placeholder = R.string.Status_PointsEntry_MWBStressor878;
                break;
            case PointsEntryType._NONSMOKERSDECLRTN:
                placeholder = R.string.Status_PointsEntry_NonsmokersDeclarationn881;
                break;
            case PointsEntryType._OFE:
                placeholder = R.string.Status_PointsEntry_OFE882;
                break;
            case PointsEntryType._RUNNING:
                placeholder = R.string.Status_PointsEntry_Running883;
                break;
            case PointsEntryType._SPEED:
                placeholder = R.string.Status_PointsEntry_Speed884;
                break;
            case PointsEntryType._STEPS:
                placeholder = R.string.Status_PointsEntry_Steps885;
                break;
            case PointsEntryType._SWIMMING:
                placeholder = R.string.Status_PointsEntry_Swimming_886;
                break;
            case PointsEntryType._TOTCHOLESTEROL:
                placeholder = R.string.Status_PointsEntry_TotCholesterol887;
                break;
            case PointsEntryType._TRIATHLON:
                placeholder = R.string.Status_PointsEntry_Triathlon888;
                break;
            case PointsEntryType._URINEPROTIEN:
                placeholder = R.string.Status_PointsEntry_UrineProtien889;
                break;
            case PointsEntryType._URINEPROTIENHR:
                placeholder = R.string.Status_PointsEntry_UrineProtienHR_890;
                break;
            case PointsEntryType._VHRASSMNTCOMPLETED:
                placeholder = R.string.Status_PointsEntry_VHRAssmntCompleted891;
                break;
            case PointsEntryType._VNAASSMNTCOMPLETED:
                placeholder = R.string.Status_PointsEntry_VNAAssmntCompleted892;
                break;
            case PointsEntryType._WALKING:
                placeholder = R.string.Status_PointsEntry_Walking893;
                break;
            default:
                placeholder = 0;
        }

        return placeholder == 0 ? "" : context.getString(placeholder);
    }
}
