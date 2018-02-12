package com.vitalityactive.va.constants;

public class GeneralPreferences {
    public enum Types {
        EMAIL_COMMUNICATION("1"),
        VITALITY_AGE_SHOWN_PERIOD("2"),
        VHC_IS_COMPLETED_FIRST_TIME("3"),
        SHARE_STATUS_WITH_EMP("4");

        public final String value;

        Types(String value) {
            this.value = value;
        }
    }
}
