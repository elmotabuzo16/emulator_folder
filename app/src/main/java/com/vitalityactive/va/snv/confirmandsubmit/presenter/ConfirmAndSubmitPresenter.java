package com.vitalityactive.va.snv.confirmandsubmit.presenter;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.snv.confirmandsubmit.model.ConfirmAndSubmitItemUI;
import com.vitalityactive.va.snv.dto.ConfirmAndSubmitItemDTO;
import com.vitalityactive.va.snv.dto.EventTypeDto;

import java.util.List;


public interface ConfirmAndSubmitPresenter<UserInterface extends ConfirmAndSubmitPresenter.UserInterface> extends Presenter<UserInterface> {

    void submit();

    void persistConfirmAndSubmitItems(List<ConfirmAndSubmitItemUI> screeningItems, List<ConfirmAndSubmitItemUI> vaccinationItems);

    interface UserInterface {
        void updateListItems(List<EventTypeDto> screeningDTOs, List<EventTypeDto> vaccinationDTOs, List<ConfirmAndSubmitItemDTO> screeningsItemsUi, List<ConfirmAndSubmitItemDTO> vaccinationItemsUi);

        void navigateAway();
    }
}
