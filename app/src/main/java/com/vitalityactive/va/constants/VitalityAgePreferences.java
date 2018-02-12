package com.vitalityactive.va.constants;


public class VitalityAgePreferences {

    public enum Types {
        VITALITY_AGE_SHOWN_PERIOD("1"),
        VHC_IS_COMPLETED_FIRST_TIME("1");

        public final String value;

        Types(String value) {
            this.value = value;
        }
    }
}
