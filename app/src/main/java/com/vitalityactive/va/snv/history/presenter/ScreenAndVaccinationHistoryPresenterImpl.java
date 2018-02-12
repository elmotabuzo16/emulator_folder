package com.vitalityactive.va.snv.history.presenter;

import android.content.Context;
import android.util.Log;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.persistence.models.screeningsandvaccinations.ScreeningVaccinationAssociatedEvents;
import com.vitalityactive.va.snv.dto.HistoryDetailDto;
import com.vitalityactive.va.snv.dto.ListHistoryListDto;
import com.vitalityactive.va.snv.history.interactor.ScreeningAndVaccinationsHistoryInteractor;
import com.vitalityactive.va.snv.shared.SnvConstants;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dharel.h.rosell on 12/1/2017.
 */

public class ScreenAndVaccinationHistoryPresenterImpl<UserInterface extends ScreenAndVaccinationHistoryPresenter.UserInterface>
        extends BasePresenter<UserInterface> implements ScreenAndVaccinationHistoryPresenter<UserInterface>{

    private EventDispatcher eventDispatcher;
    private ScreeningAndVaccinationsHistoryInteractor interactor;
    private final Scheduler scheduler;

    private final EventListener<GetScreenAndVaccinationHistorySuccessEvent> successEvent = new EventListener<GetScreenAndVaccinationHistorySuccessEvent>() {
        @Override
        public void onEvent(GetScreenAndVaccinationHistorySuccessEvent event) {
            getScreeningAndVaccinationHistoryList();
        }
    };

    private final EventListener<GetScreenAndVaccinationHistoryFailEvent> failEvent = new EventListener<GetScreenAndVaccinationHistoryFailEvent>() {
        @Override
        public void onEvent(GetScreenAndVaccinationHistoryFailEvent event) {
            getScreeningAndVaccinationHistoryListFailedEvent();
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


    public ScreenAndVaccinationHistoryPresenterImpl(EventDispatcher eventDispatcher,
                                                    ScreeningAndVaccinationsHistoryInteractor interactor,
                                                    final Scheduler schedulerParam) {
        this.eventDispatcher = eventDispatcher;
        this.interactor = interactor;
        this.scheduler = schedulerParam;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        Log.d("USER INTERFACE CREATED", "USER INTERFACE CREATED");
        List<ListHistoryListDto> listHistoryListDtos = new ArrayList<>();
        super.onUserInterfaceCreated(isNewNavigation);
        if (isNewNavigation) {
            removeEventListeners();
            addEventListeners();
            fetchData();
        }

    }

    private void fetchData() {
        interactor.fetchData();
        userInterface.showLoadingIndicator();
    }

    @Override
    public void setContext(Context context) {
        interactor.setCcontext(context);
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
       this.userInterface = userInterface;
    }

    private void getScreeningAndVaccinationHistoryList() {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    userInterface.hideLoadingIndicator();
                    List<ListHistoryListDto> listHistoryDto = interactor.getHistoryListDto();
                    userInterface.getHistoryListDto(listHistoryDto);
                }
            });
    }

    private void getScreeningAndVaccinationHistoryListFailedEvent() {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                userInterface.hideLoadingIndicator();
                userInterface.showEmptyListMessage();
            }
        });
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(GetScreenAndVaccinationHistorySuccessEvent.class, successEvent);
        eventDispatcher.addEventListener(GetScreenAndVaccinationHistoryFailEvent.class, failEvent);
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, alertDismissed);
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(GetScreenAndVaccinationHistorySuccessEvent.class, successEvent);
        eventDispatcher.removeEventListener(GetScreenAndVaccinationHistoryFailEvent.class, failEvent);
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, alertDismissed);
    }

}
