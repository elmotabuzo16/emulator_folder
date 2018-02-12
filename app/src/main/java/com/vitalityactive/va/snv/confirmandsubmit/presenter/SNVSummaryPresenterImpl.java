package com.vitalityactive.va.snv.confirmandsubmit.presenter;


import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.snv.confirmandsubmit.interactor.ConfirmAndSubmitInteractor;
import com.vitalityactive.va.snv.confirmandsubmit.service.SNVSubmitter;
import com.vitalityactive.va.snv.dto.ConfirmAndSubmitItemDTO;
import com.vitalityactive.va.snv.dto.EventTypeDto;
import com.vitalityactive.va.snv.dto.GetPotentialPointsAndEventsCompletedPointsDto;
import com.vitalityactive.va.snv.onboarding.interactor.ScreeningsAndVaccinationsInteractor;

import java.util.ArrayList;
import java.util.List;

public class SNVSummaryPresenterImpl<UserInterface extends SNVSummaryPresenter.UserInterface> extends BasePresenter<UserInterface> implements SNVSummaryPresenter<UserInterface> {

    private ScreeningsAndVaccinationsInteractor interactor;
    private ConfirmAndSubmitInteractor confirmAndSubmitInteractor;
    private InsurerConfigurationRepository insurerConfigurationRepository;
    private boolean hasShownRequestErrorMessage = false;
    private SNVSubmitter snvSubmitter;

    public SNVSummaryPresenterImpl(ScreeningsAndVaccinationsInteractor interactor, ConfirmAndSubmitInteractor confirmAndSubmitInteractor,  InsurerConfigurationRepository insurerConfigurationRepository) {
        this.interactor = interactor;
        this.confirmAndSubmitInteractor = confirmAndSubmitInteractor;
        this.insurerConfigurationRepository = insurerConfigurationRepository;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        List<ConfirmAndSubmitItemDTO> screeningsDTO = confirmAndSubmitInteractor.getScreeningItems();
        List<ConfirmAndSubmitItemDTO> vaccinationsDTO = confirmAndSubmitInteractor.getVaccinationItems();
        List<ProofItemDTO> proofItemDTOS = confirmAndSubmitInteractor.getProofItemUris();
        userInterface.updateListItems(screeningsDTO, vaccinationsDTO, proofItemDTOS);
    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }


    @Override
    public void onUserConfirmed() {
        hasShownRequestErrorMessage = false;
        if (insurerConfigurationRepository.shouldShowSNVPrivacyPolicy() ) {
            userInterface.navigateAfterUserConfirmed();
        } else {
            userInterface.showLoadingIndicator();
            snvSubmitter.submit();
        }
    }
}
