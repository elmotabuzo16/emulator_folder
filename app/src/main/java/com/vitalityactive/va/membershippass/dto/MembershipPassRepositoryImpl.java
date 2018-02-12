package com.vitalityactive.va.membershippass.dto;

import android.support.annotation.NonNull;

import com.vitalityactive.va.dto.MembershipPassDTO;
import com.vitalityactive.va.membershippass.model.MembershipPassResponse;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.Membership;

import io.realm.RealmQuery;


/**
 * Created by christian.j.p.capin on 11/21/2017.
 */

public class MembershipPassRepositoryImpl implements MembershipPassRepository {
    private final DataStore dataStore;
    private final Persister persister;

    public MembershipPassRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
        persister = new Persister(dataStore);
    }
    @Override
    public boolean persistMembershipPassResponse(MembershipPassResponse response) {
        dataStore.removeAll(Membership.class);
        return response.sections != null && persister.addModel(new Membership(response));
    }

    @Override
    public MembershipPassDTO getMembershipPass() {
        return dataStore.getFirstModelInstance(Membership.class, new DataStore.ModelMapper<Membership, MembershipPassDTO>() {
            @Override
            public MembershipPassDTO mapModel(Membership model) {
                return new MembershipPassDTO(model,model.getVitalityMembershipId(),model.getMembershipNumber(),
                        model.getCustomerNumber(), model.getVitalityStatus(), model.getMembershipStartDate(),model.getMembershipStatus());

            }
        });
    }

    @NonNull
    private DataStore.ModelMapper<Membership, MembershipPassDTO> getMembershipPassDTOModelMapper() {
        return new DataStore.ModelMapper<Membership, MembershipPassDTO>() {
            @Override
            public MembershipPassDTO mapModel(Membership model) {
                return model == null ? new MembershipPassDTO() : new MembershipPassDTO(model,model.getVitalityMembershipId(),model.getMembershipNumber(),
                        model.getCustomerNumber(), model.getVitalityStatus(), model.getMembershipStartDate(),model.getMembershipStatus());
            }
        };
    }

   /* @Override
    public MembershipPassDTO getCurrentStatus(final int statusKey) {
        return dataStore.getModelInstance(Membership.class, new DataStore.SingleModelQueryExecutor<Membership, RealmQuery<Membership>>() {
            @Override
            public Membership executeQueries(RealmQuery<Membership> initialQuery) {
                return initialQuery.equalTo("statusKey", statusKey).findFirst();
            }
        }, getLevelStatusDTOModelMapper());
    }*/
}
