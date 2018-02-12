package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.models.AppConfigFeatureParameter;

public class AppConfigFeatureParameterDTO {
    public final String name;
    public final String value;

    public AppConfigFeatureParameterDTO(AppConfigFeatureParameter appConfigFeatureParameter) {
        name = appConfigFeatureParameter.getName();
        value = appConfigFeatureParameter.getValue();
    }
}
