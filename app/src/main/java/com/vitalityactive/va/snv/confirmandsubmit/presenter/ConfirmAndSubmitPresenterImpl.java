package com.vitalityactive.va.snv.confirmandsubmit.presenter;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.snv.confirmandsubmit.interactor.ConfirmAndSubmitInteractor;
import com.vitalityactive.va.snv.confirmandsubmit.model.ConfirmAndSubmitItemUI;
import com.vitalityactive.va.snv.dto.ConfirmAndSubmitItemDTO;
import com.vitalityactive.va.snv.dto.EventTypeDto;
import com.vitalityactive.va.snv.dto.GetPotentialPointsAndEventsCompletedPointsDto;
import com.vitalityactive.va.snv.onboarding.interactor.ScreeningsAndVaccinationsInteractor;

import java.util.ArrayList;
import java.util.List;

public class ConfirmAndSubmitPresenterImpl<UserInterface extends ConfirmAndSubmitPresenter.UserInterface> extends BasePresenter<UserInterface> implements ConfirmAndSubmitPresenter<UserInterface> {
    private ScreeningsAndVaccinationsInteractor interactor;
    private ConfirmAndSubmitInteractor confirmAndSubmitInteractor;

    public ConfirmAndSubmitPresenterImpl(ScreeningsAndVaccinationsInteractor interactor, ConfirmAndSubmitInteractor confirmAndSubmitInteractor) {
        this.interactor = interactor;
        this.confirmAndSubmitInteractor = confirmAndSubmitInteractor;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        updateUserInterface();
    }

    public void updateUserInterface(){
        List<EventTypeDto> screeningsDTO = new ArrayList<>();
        List<EventTypeDto> vaccinationsDTO = new ArrayList<>();
        List<ConfirmAndSubmitItemDTO> screeningsItemsUi = confirmAndSubmitInteractor.getScreeningItems();
        List<ConfirmAndSubmitItemDTO> vaccinationItemsUi = confirmAndSubmitInteractor.getVaccinationItems();

        GetPotentialPointsAndEventsCompletedPointsDto response = interactor.getResponseData();

        if (response != null) {
            for (EventTypeDto eventTypeDto : response.getEventTypes()) {
                if (eventTypeDto.getCategoryKey() == 23) { // Screenings
                    screeningsDTO.add(eventTypeDto);
                } else if (eventTypeDto.getCategoryKey() == 24) { // Vaccinations
                    vaccinationsDTO.add(eventTypeDto);
                }
            }
        }

        userInterface.updateListItems(screeningsDTO, vaccinationsDTO, screeningsItemsUi, vaccinationItemsUi);
    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void submit() {
        userInterface.navigateAway();
    }

    @Override
    public void persistConfirmAndSubmitItems(List<ConfirmAndSubmitItemUI> screeningItems, List<ConfirmAndSubmitItemUI> vaccinationItems ){
        confirmAndSubmitInteractor.addScreeningItems(screeningItems);
        confirmAndSubmitInteractor.addVaccinationItems(vaccinationItems);
    }
}
