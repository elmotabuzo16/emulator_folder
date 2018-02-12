package com.vitalityactive.va.vhc.captureresults.models;

public enum GroupType {
    BODY_MASS_INDEX(0),
    HBA1C(1),
    CHOLESTEROL(2),
    BLOOD_GLUCOSE(3),
    BLOOD_PRESSURE(4),
    WAIST_CIRCUMFERENCE(5),
    URINE_PROTEIN(6),
    OTHER_BLOOD_LIPID(7),
    UNKNOWN(-1);

    private final int typeKey;

    GroupType(int typeKey) {
        this.typeKey = typeKey;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public static GroupType fromValue(int typeKey) {
        switch (typeKey) {
            case 0:
                return BODY_MASS_INDEX;
            case 1:
                return HBA1C;
            case 2:
                return CHOLESTEROL;
            case 3:
                return BLOOD_GLUCOSE;
            case 4:
                return BLOOD_PRESSURE;
            case 5:
                return WAIST_CIRCUMFERENCE;
            case 6:
                return URINE_PROTEIN;
            case 7:
                return OTHER_BLOOD_LIPID;
        }
        return GroupType.UNKNOWN;
    }
}
