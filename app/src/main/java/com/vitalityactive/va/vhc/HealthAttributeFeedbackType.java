package com.vitalityactive.va.vhc;

import com.vitalityactive.va.constants.HealthAttributeFeedback;

public enum HealthAttributeFeedbackType {
    UNKNOWN(-1, false),
    BMI_ABOVE(HealthAttributeFeedback._BMIABOVE, false),
    BMI_HEALTHY(HealthAttributeFeedback._BMIHEALTHY, true),
    BMI_BELOW(HealthAttributeFeedback._BMIBELOW, false),
    WEIGHT_ABOVE(HealthAttributeFeedback._WEIGHTABOVE, false),
    WEIGHT_HEALTHY(HealthAttributeFeedback._WEIGHTHEALTHY, true),
    WEIGHT_BELOW(HealthAttributeFeedback._WEIGHTBELOW, false),
    SYSTOLIC_ABOVE(HealthAttributeFeedback._SYSTOLICABOVE, false),
    SYSTOLIC_HEALTHY(HealthAttributeFeedback._SYSTOLICHEALTHY, true),
    DIASTOLIC_ABOVE(HealthAttributeFeedback._DIASTOLICABOVE, false),
    DIASTOLIC_HEALTHY(HealthAttributeFeedback._DIASTOLICHEALTHY, true),
    TOTAL_CHOLESTEROL_HIGH(HealthAttributeFeedback._TOTALCHOLESTEROLHIGH, false),
    TOTAL_CHOLESTEROL_GOOD(HealthAttributeFeedback._TOTALCHOLESTEROLGOOD, true),
    WAIST_CIRCUMFERENCE_ABOVE(HealthAttributeFeedback._WAISTCIRCUMABOVE, false),
    WAIST_CIRCUMFERENCE_HEALTHY(HealthAttributeFeedback._WAISTCIRCUMHEALTHY, true),
    HBA1C_IN_RANGE(HealthAttributeFeedback._HBA1CINRANGE, true),
    HBA1C_OUT_OF_RANGE(HealthAttributeFeedback._HBA1COUTRANGE, false),
    FASTING_GLUCOSE_IN_RANGE(HealthAttributeFeedback._FGLUCOSEINRANGE, true),
    FASTING_GLUCOSE_OUT_OF_RANGE(HealthAttributeFeedback._FGLUCOSEOUTRANGE, false),
    LDL_ABOVE(HealthAttributeFeedback._LDLABOVE, false),
    LDL_HEALTHY(HealthAttributeFeedback._LDLHEALTHY, true),
    LDL_BELOW(HealthAttributeFeedback._LDLBELOW, false),
    URINARY_PROTEIN_IN_RANGE(HealthAttributeFeedback._URINARYPROTEININ, true),
    URINARY_PROTEIN_OUT_OF_RANGE(HealthAttributeFeedback._URINARYPROTEINOUT, false),
    RANDOM_GLUCOSE_HIGH(HealthAttributeFeedback._RANDOMGLUCOSEHIGH, false),
    RANDOM_GLUCOSE_OK(HealthAttributeFeedback._RANDOMGLUCOSEOK, true),
    RANDOM_GLUCOSE_HEALTHY(HealthAttributeFeedback._RANDOMGLUCOSEHEALTHY, true),
    LIPID_RATIO_HEALTHY(HealthAttributeFeedback._LIPIDRATIOHEALTHY, true),
    LIPID_RATIO_HIGH(HealthAttributeFeedback._LIPIDRATIOHIGH, false);

    private final int typeKey;
    private final boolean inHealthyRange;

    HealthAttributeFeedbackType(int typeKey, boolean inHealthyRange) {
        this.typeKey = typeKey;
        this.inHealthyRange = inHealthyRange;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public boolean inHealthyRange() {
        return inHealthyRange;
    }

    public static HealthAttributeFeedbackType fromValue(int feedbackTypeKey) {
        switch (feedbackTypeKey) {
            case HealthAttributeFeedback._BMIABOVE:
                return BMI_ABOVE;
            case HealthAttributeFeedback._BMIHEALTHY:
                return BMI_HEALTHY;
            case HealthAttributeFeedback._BMIBELOW:
                return BMI_BELOW;
            case HealthAttributeFeedback._WEIGHTABOVE:
                return WEIGHT_ABOVE;
            case HealthAttributeFeedback._WEIGHTHEALTHY:
                return WEIGHT_HEALTHY;
            case HealthAttributeFeedback._WEIGHTBELOW:
                return WEIGHT_BELOW;
            case HealthAttributeFeedback._SYSTOLICABOVE:
                return SYSTOLIC_ABOVE;
            case HealthAttributeFeedback._SYSTOLICHEALTHY:
                return SYSTOLIC_HEALTHY;
            case HealthAttributeFeedback._DIASTOLICABOVE:
                return DIASTOLIC_ABOVE;
            case HealthAttributeFeedback._DIASTOLICHEALTHY:
                return DIASTOLIC_HEALTHY;
            case HealthAttributeFeedback._TOTALCHOLESTEROLHIGH:
                return TOTAL_CHOLESTEROL_HIGH;
            case HealthAttributeFeedback._TOTALCHOLESTEROLGOOD:
                return TOTAL_CHOLESTEROL_GOOD;
            case HealthAttributeFeedback._WAISTCIRCUMABOVE:
                return WAIST_CIRCUMFERENCE_ABOVE;
            case HealthAttributeFeedback._WAISTCIRCUMHEALTHY:
                return WAIST_CIRCUMFERENCE_HEALTHY;
            case HealthAttributeFeedback._HBA1CINRANGE:
                return HBA1C_IN_RANGE;
            case HealthAttributeFeedback._HBA1COUTRANGE:
                return HBA1C_OUT_OF_RANGE;
            case HealthAttributeFeedback._FGLUCOSEINRANGE:
                return FASTING_GLUCOSE_IN_RANGE;
            case HealthAttributeFeedback._FGLUCOSEOUTRANGE:
                return FASTING_GLUCOSE_OUT_OF_RANGE;
            case HealthAttributeFeedback._LDLABOVE:
                return LDL_ABOVE;
            case HealthAttributeFeedback._LDLHEALTHY:
                return LDL_HEALTHY;
            case HealthAttributeFeedback._LDLBELOW:
                return LDL_BELOW;
            case HealthAttributeFeedback._URINARYPROTEININ:
                return URINARY_PROTEIN_IN_RANGE;
            case HealthAttributeFeedback._URINARYPROTEINOUT:
                return URINARY_PROTEIN_OUT_OF_RANGE;
            case HealthAttributeFeedback._RANDOMGLUCOSEHEALTHY:
                return RANDOM_GLUCOSE_HEALTHY;
            case HealthAttributeFeedback._RANDOMGLUCOSEHIGH:
                return RANDOM_GLUCOSE_HIGH;
            case HealthAttributeFeedback._RANDOMGLUCOSEOK:
                return RANDOM_GLUCOSE_OK;
            case HealthAttributeFeedback._LIPIDRATIOHEALTHY:
                return LIPID_RATIO_HEALTHY;
            case HealthAttributeFeedback._LIPIDRATIOHIGH:
                return LIPID_RATIO_HIGH;
            default:
                return UNKNOWN;
        }
    }
}
