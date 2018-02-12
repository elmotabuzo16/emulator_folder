package com.vitalityactive.va.snv.onboarding.presenter;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.snv.dto.EventTypeDto;

import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/27/2017.
 */

public interface HealthActionsPresenter<UserInterface extends HealthActionsPresenter.UserInterface> extends Presenter<UserInterface> {

    interface UserInterface {
        void updateListItems(List<EventTypeDto> eventTypeDtos);
        String getAction();
    }
}
