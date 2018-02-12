package com.vitalityactive.va.wellnessdevices.pointsmonitor;

import com.vitalityactive.va.wellnessdevices.dto.PotentialPointsDto;

public interface WellnessDevicesPointsInteractor {
    PotentialPointsDto getPotentialPoints(int typeKey);
}
