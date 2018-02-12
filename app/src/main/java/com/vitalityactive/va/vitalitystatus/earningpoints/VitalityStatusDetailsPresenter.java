package com.vitalityactive.va.vitalitystatus.earningpoints;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.activerewards.dto.TitleAndSubtitle;

import java.util.List;

public interface VitalityStatusDetailsPresenter extends Presenter<VitalityStatusDetailsPresenter.UserInterface> {
    void setKey(int title);
    void setIsSubfeature(boolean b);

    interface UserInterface {
        void displayPointsInformation(List<PointsInformationDTO> pointsInformationDTOs, TitleAndSubtitle titleAndSubtitle);
        void setActionBarTitle(String title);
    }
}
