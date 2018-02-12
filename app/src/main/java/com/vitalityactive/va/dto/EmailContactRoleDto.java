package com.vitalityactive.va.dto;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.models.ContactRole;

public class EmailContactRoleDto {
    private String availabilityFrom;
    private String availabilityTo;
    private String rolePurposeType;
    private String roleType;
    private String effectiveFrom;
    private String effectiveTo;

    public EmailContactRoleDto(LoginServiceResponse.ContactRole contactRole) {
        availabilityFrom = contactRole.availabilityFrom;
        availabilityTo = contactRole.availabilityTo;
        rolePurposeType = contactRole.rolePurposeType;
        roleType = contactRole.roleType;
        effectiveFrom = contactRole.effectiveFrom;
        effectiveTo = contactRole.effectiveTo;
    }

    public EmailContactRoleDto(ContactRole contactRole) {
        availabilityFrom = contactRole.getAvailabilityFrom();
        availabilityTo = contactRole.getAvailabilityTo();
        rolePurposeType = contactRole.getRolePurposeType();
        roleType = contactRole.getRoleType();
        effectiveFrom = contactRole.getEffectiveFrom();
        effectiveTo = contactRole.getEffectiveTo();
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }

    public String getAvailabilityFrom() {
        return availabilityFrom;
    }

    public String getAvailabilityTo() {
        return availabilityTo;
    }

    public String getRolePurposeType() {
        return rolePurposeType;
    }

    public String getRoleType() {
        return roleType;
    }
}
