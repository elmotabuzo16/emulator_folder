package com.vitalityactive.va.vhc.addproof;

import com.vitalityactive.va.dto.ProofItemDTO;

import java.util.List;

public interface VHCProofItemRepository {
    ProofItemDTO persistUri(String uri);

    List<ProofItemDTO> getProofItems();

    void removeProofItem(ProofItemDTO uri);

    List<ProofItemDTO> getProofItemsThatHaveNotBeenSubmitted();

    void setProofItemReferenceId(ProofItemDTO proofItem, String referenceId);

    void removeAllProofItems();
}
