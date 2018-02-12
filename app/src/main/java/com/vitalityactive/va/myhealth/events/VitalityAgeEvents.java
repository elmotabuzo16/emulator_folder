package com.vitalityactive.va.myhealth.events;


import com.vitalityactive.va.myhealth.entity.VitalityAge;

public class VitalityAgeEvents {

    public static class VitalityAgeResponseEvent {
        public VitalityAge vitalityAge;

        public VitalityAgeResponseEvent(VitalityAge vitalityAge) {
            this.vitalityAge = vitalityAge;
        }
    }
}
