package com.vitalityactive.va.snv.onboarding.presenter;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.snv.confirmandsubmit.interactor.ConfirmAndSubmitInteractor;
import com.vitalityactive.va.snv.dto.EventTypeDto;
import com.vitalityactive.va.snv.dto.GetPotentialPointsAndEventsCompletedPointsDto;
import com.vitalityactive.va.snv.onboarding.interactor.ScreeningsAndVaccinationsInteractor;
import com.vitalityactive.va.snv.shared.SnvConstants;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;

public class ScreeningsAndVaccinationsOnboardingPresenterImpl<UserInterface extends ScreeningsAndVaccinationsOnboardingPresenter.UserInterface> extends BasePresenter<UserInterface> implements ScreeningsAndVaccinationsOnboardingPresenter<UserInterface> {
    private EventDispatcher eventDispatcher;
    private ScreeningsAndVaccinationsInteractor interactor;
    private ConfirmAndSubmitInteractor confirmAndSubmitInteractor;

    private final EventListener<GetPotentialPointsAndEventsCompletedPointSuccessEvent> successtEventListener = new EventListener<GetPotentialPointsAndEventsCompletedPointSuccessEvent>() {
        @Override
        public void onEvent(GetPotentialPointsAndEventsCompletedPointSuccessEvent getPotentialPointsAndEventsCompletedPointSuccessEvent) {
            onGetPotentialPointsAndEventsCompletedPointSuccessEvent();
        }
    };

    private final EventListener<GetPotentialPointsAndEventsCompletedPointFailedEvent> failedEventListener = new EventListener<GetPotentialPointsAndEventsCompletedPointFailedEvent>() {
        @Override
        public void onEvent(GetPotentialPointsAndEventsCompletedPointFailedEvent getPotentialPointsAndEventsCompletedPointSuccessEvent) {
            onGetPotentialPointsAndEventsCompletedPointFailedEvent();
        }
    };

    private final EventListener<AlertDialogFragment.DismissedEvent> alertDismissed = new EventListener<AlertDialogFragment.DismissedEvent>() {
        @Override
        public void onEvent(AlertDialogFragment.DismissedEvent event) {
            if ((event.getType().equals(SnvConstants.SNV_CONNECTION_ERROR_ALERT) || event.getType().equals(SnvConstants.SNV_GENERIC_ERROR_ALERT))
                    && event.getClickedButtonType() == AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive) {
                fetchData();
            }
        }
    };

    public ScreeningsAndVaccinationsOnboardingPresenterImpl(EventDispatcher eventDispatcher, ScreeningsAndVaccinationsInteractor interactor, ConfirmAndSubmitInteractor confirmAndSubmitInteractor) {
        this.eventDispatcher = eventDispatcher;
        this.interactor = interactor;
        this.confirmAndSubmitInteractor = confirmAndSubmitInteractor;
    }
    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        super.onUserInterfaceCreated(isNewNavigation);
        if (isNewNavigation) {
            removeEventListeners();
            addEventListeners();
            fetchData();
        }
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        clearAllConfirmAndSubmitItems();
    }

    private void clearAllConfirmAndSubmitItems(){
        confirmAndSubmitInteractor.clearAllItems();
        confirmAndSubmitInteractor.clearProofItems();
    }

    private void fetchData() {
        interactor.fetchData();
        userInterface.showLoadingIndicator();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        if (isFinishing) {
            removeEventListeners();
        }
    }

    private void onGetPotentialPointsAndEventsCompletedPointSuccessEvent() {
        GetPotentialPointsAndEventsCompletedPointsDto response = interactor.getResponseData();
        int screeningsTotalPotentialPoints = 0;
        int vaccinationsTotalPotentialPoints = 0;

        if (response != null) {
            for (EventTypeDto eventTypeDto: response.getEventTypes()) {
                if (eventTypeDto.getCategoryKey() == 23) { // Screenings
                    screeningsTotalPotentialPoints += eventTypeDto.getTotalPotentialPoints();
                } else if (eventTypeDto.getCategoryKey() == 24) { // Vaccinations
                    vaccinationsTotalPotentialPoints += eventTypeDto.getTotalPotentialPoints();
                }
            }
        }

        userInterface.updateScreeningsPoints(screeningsTotalPotentialPoints);
        userInterface.updateVaccinationsPoints(vaccinationsTotalPotentialPoints);
        userInterface.hideLoadingIndicator();
    }

    private void onGetPotentialPointsAndEventsCompletedPointFailedEvent() {
        userInterface.hideLoadingIndicator();
        userInterface.showConnectionContentRequestErrorMessage();
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(GetPotentialPointsAndEventsCompletedPointSuccessEvent.class, successtEventListener);
        eventDispatcher.addEventListener(GetPotentialPointsAndEventsCompletedPointFailedEvent.class, failedEventListener);
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, alertDismissed);
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(GetPotentialPointsAndEventsCompletedPointSuccessEvent.class, successtEventListener);
        eventDispatcher.removeEventListener(GetPotentialPointsAndEventsCompletedPointFailedEvent.class, failedEventListener);
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, alertDismissed);
    }

}
