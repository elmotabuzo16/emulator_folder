package com.vitalityactive.va.vhc.addproof;

import android.support.annotation.Nullable;

import com.vitalityactive.va.dto.ProofItemDTO;

import java.util.List;

public interface VHCAddProofInteractor {
    @Nullable
    ProofItemDTO addProofItemUri(String uri);

    List<ProofItemDTO> getProofItemUris();

    void removeProofItem(ProofItemDTO proofItem);
}
