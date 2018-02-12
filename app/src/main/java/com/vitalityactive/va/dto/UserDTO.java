package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.User;

public class UserDTO {
    public final long partyId;
    public final String userName;
    public final String vitalityMembershipId;

    public UserDTO(User dataStoreModel) {
        this.partyId = dataStoreModel.getPartyId();
        this.userName = dataStoreModel.getUsername();
        this.vitalityMembershipId = dataStoreModel.getVitalityMembershipId();
    }

    public static class Mapper implements DataStore.ModelMapper<User, UserDTO> {
        @Override
        public UserDTO mapModel(User user) {
            return new UserDTO(user);
        }
    }
}
