package com.vitalityactive.va.snv.learnmore.presenter;

import com.vitalityactive.va.snv.dto.EventTypeDto;
import com.vitalityactive.va.Presenter;
import java.util.List;

/**
 * Created by stephen.rey.w.avila on 12/4/2017.
 */

public interface SnvLearnMorePresenter<UserInterface extends SnvLearnMorePresenter.UserInterface> extends Presenter<UserInterface> {
    interface UserInterface {
        void updateListItems(List<EventTypeDto> eventTypeDtos);
        String getAction();
    }

}
