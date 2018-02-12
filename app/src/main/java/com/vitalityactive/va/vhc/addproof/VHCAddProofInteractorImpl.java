package com.vitalityactive.va.vhc.addproof;

import android.support.annotation.Nullable;

import com.vitalityactive.va.dto.ProofItemDTO;

import java.util.List;

public class VHCAddProofInteractorImpl implements VHCAddProofInteractor {

    private VHCProofItemRepository repository;

    public VHCAddProofInteractorImpl(VHCProofItemRepository repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public ProofItemDTO addProofItemUri(String uri) {
        if (getProofItemUris().size() == 11) {
            return null;
        }
        return repository.persistUri(uri);
    }

    @Override
    public List<ProofItemDTO> getProofItemUris() {
        return repository.getProofItems();
    }

    @Override
    public void removeProofItem(ProofItemDTO proofItem) {
        repository.removeProofItem(proofItem);
    }
}
