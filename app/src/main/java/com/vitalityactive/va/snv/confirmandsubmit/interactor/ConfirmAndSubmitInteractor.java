package com.vitalityactive.va.snv.confirmandsubmit.interactor;


import android.support.annotation.Nullable;

import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.snv.confirmandsubmit.model.ConfirmAndSubmitItemUI;
import com.vitalityactive.va.snv.dto.ConfirmAndSubmitItemDTO;

import java.util.List;

public interface ConfirmAndSubmitInteractor {
    @Nullable
    ProofItemDTO addProofItemUri(String uri);

    List<ProofItemDTO> getProofItemUris();

    void removeProofItem(ProofItemDTO proofItem);

    void addScreeningItems(List<ConfirmAndSubmitItemUI> screenings);

    void addVaccinationItems(List<ConfirmAndSubmitItemUI> vaccinations);

    void clearScreeningItems();

    void clearVaccinationItems();

    void clearAllItems();

    void clearProofItems();

    List<ConfirmAndSubmitItemDTO> getScreeningItems();

    List<ConfirmAndSubmitItemDTO> getVaccinationItems();
}
