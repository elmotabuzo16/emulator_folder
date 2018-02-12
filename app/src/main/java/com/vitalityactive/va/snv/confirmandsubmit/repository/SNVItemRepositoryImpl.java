package com.vitalityactive.va.snv.confirmandsubmit.repository;


import android.support.annotation.Nullable;

import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.ConfirmAndSubmitItem;
import com.vitalityactive.va.persistence.models.ProofItem;
import com.vitalityactive.va.snv.confirmandsubmit.model.ConfirmAndSubmitItemUI;
import com.vitalityactive.va.snv.dto.ConfirmAndSubmitItemDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.RealmQuery;

public class SNVItemRepositoryImpl implements SNVItemRepository {

    private DataStore dataStore;

    public SNVItemRepositoryImpl(DataStore dataStore) {
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

    @Override
    public void addScreeningItems(List<ConfirmAndSubmitItemUI> screenings) {
        for (ConfirmAndSubmitItemUI itemsUi : screenings) {
            persistSingleSNVItem(itemsUi, ConfirmAndSubmitItemUI.SCREENING_TYPE);
        }
    }

    @Override
    public void addVaccinationItems(List<ConfirmAndSubmitItemUI> vaccinations) {
        for (ConfirmAndSubmitItemUI itemsUi : vaccinations) {
            persistSingleSNVItem(itemsUi, ConfirmAndSubmitItemUI.VACCINATION_TYPE);
        }
    }

    @Override
    public void clearScreeningItems() {
        for (ConfirmAndSubmitItemDTO itemToDelete : getVaccinationItems()) {
            removeSingleSNVItem(itemToDelete);
        }
    }

    @Override
    public void clearVaccinationItems() {
        for (ConfirmAndSubmitItemDTO itemToDelete : getScreeningItems()) {
            removeSingleSNVItem(itemToDelete);
        }
    }

    @Override
    public void clearAllItems() {
        dataStore.removeAll(ConfirmAndSubmitItem.class);
    }

    @Override
    public List<ConfirmAndSubmitItemDTO> getScreeningItems() {
        return dataStore.getModels(ConfirmAndSubmitItem.class,
                new DataStore.ModelListMapper<ConfirmAndSubmitItem, ConfirmAndSubmitItemDTO>() {
                    @Override
                    public List<ConfirmAndSubmitItemDTO> mapModels(List<ConfirmAndSubmitItem> models) {
                        List<ConfirmAndSubmitItemDTO> mappedModels = new ArrayList<>();
                        for (ConfirmAndSubmitItem items : models) {
                            if (items.getType() == ConfirmAndSubmitItemUI.SCREENING_TYPE) {
                                mappedModels.add(new ConfirmAndSubmitItemDTO(items));
                            }
                        }
                        return mappedModels;
                    }
                });
    }

    @Override
    public List<ConfirmAndSubmitItemDTO> getVaccinationItems() {
        return dataStore.getModels(ConfirmAndSubmitItem.class,
                new DataStore.ModelListMapper<ConfirmAndSubmitItem, ConfirmAndSubmitItemDTO>() {
                    @Override
                    public List<ConfirmAndSubmitItemDTO> mapModels(List<ConfirmAndSubmitItem> models) {
                        List<ConfirmAndSubmitItemDTO> mappedModels = new ArrayList<>();
                        for (ConfirmAndSubmitItem items : models) {
                            if (items.getType() == ConfirmAndSubmitItemUI.VACCINATION_TYPE) {
                                mappedModels.add(new ConfirmAndSubmitItemDTO(items));
                            }
                        }
                        return mappedModels;
                    }
                });
    }

    private void persistSingleSNVItem(ConfirmAndSubmitItemUI confirmAndSubmitItemUI, int type) {
        ConfirmAndSubmitItem confirmAndSubmitItem = new ConfirmAndSubmitItem(confirmAndSubmitItemUI, type, confirmAndSubmitItemUI.getTypeKey(), confirmAndSubmitItemUI.getDateTestedInLong());
        dataStore.addOrUpdate(Collections.singletonList(confirmAndSubmitItem));
    }

    private void removeSingleSNVItem(ConfirmAndSubmitItemDTO itemDTO) {
        dataStore.remove(ConfirmAndSubmitItem.class, "fieldTitle", itemDTO.getFieldTitle());
    }

}
