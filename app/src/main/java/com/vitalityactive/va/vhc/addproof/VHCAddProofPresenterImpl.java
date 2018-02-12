package com.vitalityactive.va.vhc.addproof;

import android.support.annotation.Nullable;

import com.vitalityactive.va.dto.ProofItemDTO;

import java.util.List;

public class VHCAddProofPresenterImpl implements VHCAddProofPresenter {

    private final VHCAddProofInteractor interactor;
    private UserInterface userInterface;

    public VHCAddProofPresenterImpl(VHCAddProofInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {

    }

    @Override
    public void onUserInterfaceAppeared() {
        List<ProofItemDTO> proofItemUris = interactor.getProofItemUris();
        userInterface.showProofItems(proofItemUris);
        userInterface.updateProofItemCount(proofItemUris.size());
        toggleAddProofButton(proofItemUris.size());
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {

    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void setUserInterface(VHCAddProofPresenter.UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Nullable
    @Override
    public ProofItemDTO addProofItemFromUri(String uri) {
        ProofItemDTO proofItemDTO = interactor.addProofItemUri(uri);
        if (proofItemDTO == null) {
            return null;
        }
        int numberOfProofItems = interactor.getProofItemUris().size();
        userInterface.updateProofItemCount(numberOfProofItems);
        toggleAddProofButton(numberOfProofItems);
        return proofItemDTO;
    }

    @Override
    public void onUserSelectsItem(int position) {
        if (isAddProofButton(position)) {
            userInterface.onVHCAddProofTapped();
        } else {
            userInterface.showProofItemDetail(position);
        }
    }

    private boolean isAddProofButton(int position) {
        int numberOfProofItems = interactor.getProofItemUris().size();
        return position == numberOfProofItems && numberOfProofItems < 11;
    }

    private void toggleAddProofButton(int numberOfProofItems) {
        if (numberOfProofItems == 11) {
            userInterface.hideAddProofButton();
        } else {
            userInterface.showAddProofButton();
        }
    }
}
