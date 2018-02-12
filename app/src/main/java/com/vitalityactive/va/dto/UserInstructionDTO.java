package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.UserInstruction;

public class UserInstructionDTO {
    public final String id;

    public UserInstructionDTO(UserInstruction dataStoreModel) {
        this.id = dataStoreModel.getId();
    }

    public static class Mapper implements DataStore.ModelMapper<UserInstruction, UserInstructionDTO> {
        @Override
        public UserInstructionDTO mapModel(UserInstruction dataStoreModel) {
            return new UserInstructionDTO(dataStoreModel);
        }
    }
}
