package com.vitalityactive.va.snv.learnmore.presenter;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.snv.dto.EventTypeDto;

import java.util.List;

/**
 * Created by stephen.rey.w.avila on 12/8/2017.
 */

public interface SnvListDescriptionPresenter<UserInterface extends SnvListDescriptionPresenter.UserInterface> extends Presenter<UserInterface> {
    interface UserInterface {
        void updateListItems(List<EventTypeDto> eventTypeDtos);
        String getAction();
    }
}
