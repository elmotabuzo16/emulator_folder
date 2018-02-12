package com.vitalityactive.va.snv.confirmandsubmit.presenter;

import android.support.annotation.Nullable;

import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.snv.confirmandsubmit.interactor.ConfirmAndSubmitInteractor;

import java.util.List;


public class SNVAddProofPresenterImpl implements SNVAddProofPresenter {

    private UserInterface userInterface;
    private ConfirmAndSubmitInteractor interactor;
    private int itemsSelected;

    public SNVAddProofPresenterImpl(ConfirmAndSubmitInteractor interactor) {
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
    public void setUserInterface(UserInterface userInterface) {
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
            userInterface.onSNVAddProofTapped();
        } else {
            userInterface.showProofItemDetail(position);
        }
    }

    @Override
    public int getNumberOfItemsSelected() {
        itemsSelected = interactor.getScreeningItems().size() + interactor.getVaccinationItems().size();
        return itemsSelected;
    }

    private boolean isAddProofButton(int position) {
        int numberOfProofItems = interactor.getProofItemUris().size();
        return position == numberOfProofItems && numberOfProofItems < itemsSelected;
    }

    private void toggleAddProofButton(int numberOfProofItems) {
        if (numberOfProofItems == itemsSelected) {
            userInterface.hideAddProofButton();
        } else {
            userInterface.showAddProofButton();
        }
    }
}
