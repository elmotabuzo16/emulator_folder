package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.models.CurrentVitalityMembershipPeriod;

public class CurrentVitalityMembershipPeriodDTO {

    private String effectiveFrom = "";
    private String effectiveTo = "";

    public CurrentVitalityMembershipPeriodDTO() {

    }

    public CurrentVitalityMembershipPeriodDTO(CurrentVitalityMembershipPeriod membershipPeriod) {
        effectiveFrom = membershipPeriod.getEffectiveFrom();
        effectiveTo = membershipPeriod.getEffectiveTo();
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }
}