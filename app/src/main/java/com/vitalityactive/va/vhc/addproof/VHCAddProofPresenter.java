package com.vitalityactive.va.vhc.addproof;

import android.support.annotation.Nullable;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.dto.ProofItemDTO;

import java.util.List;

public interface VHCAddProofPresenter extends Presenter<VHCAddProofPresenter.UserInterface> {
    @Nullable
    ProofItemDTO addProofItemFromUri(String uriString);

    void onUserSelectsItem(int position);

    interface UserInterface {
        void showProofItems(List<ProofItemDTO> proofItemUris);

        void updateProofItemCount(int numberOfProofItems);

        void hideAddProofButton();

        void showAddProofButton();

        void onVHCAddProofTapped();

        void showProofItemDetail(int position);
    }
}
