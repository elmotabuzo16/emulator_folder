package com.vitalityactive.va.snv.confirmandsubmit.repository;

import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.persistence.models.ConfirmAndSubmitItem;
import com.vitalityactive.va.snv.confirmandsubmit.model.ConfirmAndSubmitItemUI;
import com.vitalityactive.va.snv.dto.ConfirmAndSubmitItemDTO;

import java.util.List;

public interface SNVItemRepository {

    ProofItemDTO persistUri(String uri);

    List<ProofItemDTO> getProofItems();

    void removeProofItem(ProofItemDTO uri);

    List<ProofItemDTO> getProofItemsThatHaveNotBeenSubmitted();

    void setProofItemReferenceId(ProofItemDTO proofItem, String referenceId);

    void removeAllProofItems();

    void addScreeningItems(List<ConfirmAndSubmitItemUI> screenings);

    void addVaccinationItems(List<ConfirmAndSubmitItemUI> vaccinations);

    void clearScreeningItems();

    void clearVaccinationItems();

    void clearAllItems();

    List<ConfirmAndSubmitItemDTO> getScreeningItems();

    List<ConfirmAndSubmitItemDTO> getVaccinationItems();
}
