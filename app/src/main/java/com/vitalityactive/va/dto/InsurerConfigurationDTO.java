package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.InsurerConfiguration;

public class InsurerConfigurationDTO {
    public final long tenantId;

    public InsurerConfigurationDTO(InsurerConfiguration dataStoreModel) {
        this.tenantId = dataStoreModel.getTenantId();
    }

    public static class Mapper implements DataStore.ModelMapper<InsurerConfiguration, InsurerConfigurationDTO> {
        @Override
        public InsurerConfigurationDTO mapModel(InsurerConfiguration dataStoreModel) {
            return new InsurerConfigurationDTO(dataStoreModel);
        }
    }
}
