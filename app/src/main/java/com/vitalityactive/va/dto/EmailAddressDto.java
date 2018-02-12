package com.vitalityactive.va.dto;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.models.ContactRole;
import com.vitalityactive.va.persistence.models.EmailAddress;

import java.util.ArrayList;
import java.util.List;

public class EmailAddressDto {
    private String value;
    private List<EmailContactRoleDto> contactRoles;
    private String effectiveFrom;
    private String effectiveTo;

    public EmailAddressDto(LoginServiceResponse.EmailAddress emailAddress) {
        value = emailAddress.value;
        effectiveFrom = emailAddress.effectiveFrom;
        effectiveTo = emailAddress.effectiveTo;
        contactRoles = new ArrayList<>();
        if (emailAddress.contactRoles != null &&
                !emailAddress.contactRoles.isEmpty()) {
            for (LoginServiceResponse.ContactRole contactRole : emailAddress.contactRoles) {
                contactRoles.add(new EmailContactRoleDto(contactRole));
            }
        }
    }

    public EmailAddressDto(EmailAddress emailAddress) {
        value = emailAddress.getValue();
        effectiveFrom = emailAddress.getEffectiveFrom();
        effectiveTo = emailAddress.getEffectiveTo();
        contactRoles = new ArrayList<>();
        if (emailAddress.getContactRoles() != null &&
                !emailAddress.getContactRoles().isEmpty()) {
            for (ContactRole contactRole : emailAddress.getContactRoles()) {
                contactRoles.add(new EmailContactRoleDto(contactRole));
            }
        }
    }

    public String getValue() {
        return value;
    }

    public List<EmailContactRoleDto> getContactRoles() {
        return contactRoles;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }
}
