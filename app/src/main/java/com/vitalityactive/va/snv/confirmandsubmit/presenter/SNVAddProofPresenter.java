package com.vitalityactive.va.snv.confirmandsubmit.presenter;

import android.support.annotation.Nullable;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.dto.ProofItemDTO;

import java.util.List;

public interface SNVAddProofPresenter extends Presenter<SNVAddProofPresenter.UserInterface> {

    @Nullable
    ProofItemDTO addProofItemFromUri(String uri);

    void onUserSelectsItem(int position);

    int getNumberOfItemsSelected();

    interface UserInterface {
        void showProofItems(List<ProofItemDTO> proofItemUris);

        void updateProofItemCount(int numberOfProofItems);

        void hideAddProofButton();

        void showAddProofButton();

        void onSNVAddProofTapped();

        void showProofItemDetail(int position);
    }
}
