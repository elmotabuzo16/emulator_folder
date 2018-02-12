package com.vitalityactive.va.snv.confirmandsubmit.presenter;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.snv.dto.ConfirmAndSubmitItemDTO;
import com.vitalityactive.va.snv.dto.EventTypeDto;

import java.util.List;

public interface SNVSummaryPresenter<UserInterface extends SNVSummaryPresenter.UserInterface> extends Presenter<UserInterface> {

    void onUserConfirmed();

    interface UserInterface {
        void updateListItems(List<ConfirmAndSubmitItemDTO> screeningDTOs, List<ConfirmAndSubmitItemDTO> vaccinationDTOs, List<ProofItemDTO> proofItemDTOS);

        void navigateAfterUserConfirmed();

        void showLoadingIndicator();

        void showConnectionErrorMessage();

        void showGenericErrorMessage();

        void hideLoadingIndicator();
    }
}
