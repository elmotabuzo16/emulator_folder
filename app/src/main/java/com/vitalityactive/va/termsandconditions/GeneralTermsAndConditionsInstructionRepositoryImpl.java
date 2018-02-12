package com.vitalityactive.va.termsandconditions;

import com.vitalityactive.va.constants.UserInstructions;
import com.vitalityactive.va.dto.UserInstructionDTO;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.UserInstruction;

import javax.inject.Inject;

public class GeneralTermsAndConditionsInstructionRepositoryImpl implements GeneralTermsAndConditionsInstructionRepository {
    private DataStore dataStore;

    @Inject
    public GeneralTermsAndConditionsInstructionRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public long getInstructionId() {
        UserInstructionDTO userInstruction = getInstruction();
        if (userInstruction == null) {
            return 0;
        }
        return userInstruction.id == null ? 0 : Long.parseLong(userInstruction.id);
    }

    private UserInstructionDTO getInstruction() {
        return dataStore.getModelInstance(UserInstruction.class, new UserInstructionDTO.Mapper(), "type", UserInstructions.Types.LOGIN_T_AND_C);
    }

    @Override
    public boolean hasInstruction() {
        return getInstruction() != null;
    }

    @Override
    public void removeInstruction() {
        dataStore.remove(UserInstruction.class, "type", UserInstructions.Types.LOGIN_T_AND_C);
    }
}
