package com.vitalityactive.va.wellnessdevices.pointsmonitor;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.wellnessdevices.dto.PotentialPointsDto;

public interface WellnessDevicesPointsPresenter extends Presenter<WellnessDevicesPointsPresenter.UserInterface> {
    PotentialPointsDto getPotentialPoints(int typeKey);
    MeasurementContentFromResourceString getUomStringsProvider();

    interface UserInterface {
    }
}