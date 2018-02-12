package com.vitalityactive.va.vitalitystatus.earningpoints;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.wellnessdevices.WdEventType;

import java.util.List;

public interface StatusPointsPresenter extends Presenter<StatusPointsPresenter.UserInterface> {
    void setProductFeatureKey(int productFeatureKey);

    WdEventType getWdEventType();

    interface UserInterface {
        void displayPointsContent(List<StatusPointsItem> list);
    }
}
