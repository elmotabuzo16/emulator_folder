package com.vitalityactive.va;

import com.vitalityactive.va.dto.VitalityMembershipDto;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.VitalityMembership;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/26/2017.
 */

public class VitalityMembershipRepositoryImpl implements VitalityMembershipRepository {
    private DataStore dataStore;

    public VitalityMembershipRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }
    @Override
    public List<VitalityMembershipDto> getVitalityMembership() {
        return dataStore.getModels(VitalityMembership.class,
                new DataStore.ModelListMapper<VitalityMembership, VitalityMembershipDto>() {
                    @Override
                    public List<VitalityMembershipDto> mapModels(List<VitalityMembership> models) {
                        List<VitalityMembershipDto> mappedModels = new ArrayList<>();
                        for (VitalityMembership vitalityMembership : models) {
                            mappedModels.add(new VitalityMembershipDto(vitalityMembership));
                        }
                        return mappedModels;
                    }
                });
    }
}
