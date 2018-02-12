package com.vitalityactive.va.snv.confirmandsubmit.interactor;

import android.support.annotation.Nullable;
import android.util.Log;

import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.persistence.models.ConfirmAndSubmitItem;
import com.vitalityactive.va.snv.confirmandsubmit.model.ConfirmAndSubmitItemUI;
import com.vitalityactive.va.snv.confirmandsubmit.repository.SNVItemRepository;
import com.vitalityactive.va.snv.dto.ConfirmAndSubmitItemDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class ConfirmAndSubmitInteractorImpl implements ConfirmAndSubmitInteractor {

    private SNVItemRepository repository;

    public ConfirmAndSubmitInteractorImpl(SNVItemRepository repository) {
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

    @Override
    public void addScreeningItems(List<ConfirmAndSubmitItemUI> screenings) {
        repository.addScreeningItems(removeDuplicateItems(screenings));
    }

    @Override
    public void addVaccinationItems(List<ConfirmAndSubmitItemUI> vaccinations) {
        repository.addVaccinationItems(removeDuplicateItems(vaccinations));
    }

    private List<ConfirmAndSubmitItemUI> removeDuplicateItems(List<ConfirmAndSubmitItemUI> items){
        List<ConfirmAndSubmitItemUI> deDupItems = new ArrayList<>(new LinkedHashSet<>(items));
        return deDupItems;
    }

    @Override
    public void clearScreeningItems() {
        repository.clearScreeningItems();
    }

    @Override
    public void clearVaccinationItems() {
        repository.clearVaccinationItems();
    }

    @Override
    public void clearAllItems() {
        repository.clearAllItems();
    }

    @Override
    public void clearProofItems() {
        repository.removeAllProofItems();
    }

    @Override
    public List<ConfirmAndSubmitItemDTO> getScreeningItems() {
        return repository.getScreeningItems();
    }

    @Override
    public List<ConfirmAndSubmitItemDTO> getVaccinationItems() {
        return repository.getVaccinationItems();
    }
}
