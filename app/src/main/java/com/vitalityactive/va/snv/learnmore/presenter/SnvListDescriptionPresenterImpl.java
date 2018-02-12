package com.vitalityactive.va.snv.learnmore.presenter;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.snv.dto.EventTypeDto;
import com.vitalityactive.va.snv.dto.GetPotentialPointsAndEventsCompletedPointsDto;
import com.vitalityactive.va.snv.learnmore.presenter.SnvListDescriptionPresenter;
import com.vitalityactive.va.snv.onboarding.interactor.ScreeningsAndVaccinationsInteractor;
import com.vitalityactive.va.snv.shared.SnvConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephen.rey.w.avila on 12/8/2017.
 */

public class SnvListDescriptionPresenterImpl<UserInterface extends SnvListDescriptionPresenter.UserInterface> extends BasePresenter<UserInterface> implements SnvListDescriptionPresenter<UserInterface>  {
    private ScreeningsAndVaccinationsInteractor interactor;

    public SnvListDescriptionPresenterImpl(ScreeningsAndVaccinationsInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        List<EventTypeDto> eventTypeDtos = new ArrayList<EventTypeDto>();

        GetPotentialPointsAndEventsCompletedPointsDto response = interactor.getResponseData();
        String action = userInterface.getAction();
        if (response != null) {
            for (EventTypeDto eventTypeDto: response.getEventTypes()) {
                if (eventTypeDto.getCategoryKey() == 23 && SnvConstants.HEALTH_ACTION_SCREENINGS.equalsIgnoreCase(action)) { // Screenings
                    eventTypeDtos.add(eventTypeDto);
                } else if (eventTypeDto.getCategoryKey() == 24 && SnvConstants.HEALTH_ACTION_VACCINATIONS.equalsIgnoreCase(action)) { // Vaccinations
                    eventTypeDtos.add(eventTypeDto);
                }
            }
        }
        userInterface.updateListItems(eventTypeDtos);
    }

}
