package com.vitalityactive.va.vhc.addproof;

import android.support.annotation.Nullable;

import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.ProofItem;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmQuery;

public class VHCProofItemRepositoryImpl implements VHCProofItemRepository {
    private DataStore dataStore;

    public VHCProofItemRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ProofItemDTO persistUri(String uri) {
        ProofItem proofItem = new ProofItem(uri);
        dataStore.add(proofItem);
        return dataStore.getModelInstance(ProofItem.class, new DataStore.ModelMapper<ProofItem, ProofItemDTO>() {
            @Override
            public ProofItemDTO mapModel(ProofItem model) {
                return new ProofItemDTO(model);
            }
        }, "id", proofItem.getId());
    }

    @Override
    public List<ProofItemDTO> getProofItems() {
        return getProofItemUris(new DataStore.QueryExecutor<ProofItem, RealmQuery<ProofItem>>() {
            @Override
            public List<ProofItem> executeQueries(RealmQuery<ProofItem> initialQuery) {
                return initialQuery.findAll();
            }
        });
    }

    @Override
    public void removeProofItem(ProofItemDTO proofItem) {
        dataStore.remove(ProofItem.class, "id", proofItem.getId());
    }

    @Override
    public List<ProofItemDTO> getProofItemsThatHaveNotBeenSubmitted() {
        return getProofItemUris(new DataStore.QueryExecutor<ProofItem, RealmQuery<ProofItem>>() {
            @Override
            public List<ProofItem> executeQueries(RealmQuery<ProofItem> initialQuery) {
                return initialQuery.isNull("referenceId").or().isEmpty("referenceId").findAll();
            }
        });
    }

    private List<ProofItemDTO> getProofItemUris(DataStore.QueryExecutor<ProofItem, RealmQuery<ProofItem>> queryExecutor) {
        return dataStore.getModels(ProofItem.class, queryExecutor, new DataStore.ModelListMapper<ProofItem, ProofItemDTO>() {
            @Override
            public List<ProofItemDTO> mapModels(List<ProofItem> models) {
                List<ProofItemDTO> uris = new ArrayList<>();
                for (ProofItem proofItem : models) {
                    uris.add(new ProofItemDTO(proofItem));
                }
                return uris;
            }
        });
    }

    @Override
    public void setProofItemReferenceId(ProofItemDTO proofItem, final String referenceId) {
        dataStore.setFieldValue(ProofItem.class, "id", proofItem.getId(), new DataStore.FieldUpdater<ProofItem>() {
            @Override
            public void updateField(@Nullable ProofItem model) {
                if (model != null) {
                    model.setReferenceId(referenceId);
                }
            }
        });
    }

    @Override
    public void removeAllProofItems() {
        dataStore.removeAll(ProofItem.class);
    }
}
