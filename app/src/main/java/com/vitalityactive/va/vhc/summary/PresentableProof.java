package com.vitalityactive.va.vhc.summary;

import com.vitalityactive.va.dto.ProofItemDTO;

import java.util.List;

public class PresentableProof {
    public final String title;
    public final List<ProofItemDTO> proofItemUris;

    public PresentableProof(String title, List<ProofItemDTO> proofItemUris) {
        this.title = title;
        this.proofItemUris = proofItemUris;
    }

    public String getTitle() {
        return title;
    }

    public List<ProofItemDTO> getProofItemUris() {
        return proofItemUris;
    }
}
