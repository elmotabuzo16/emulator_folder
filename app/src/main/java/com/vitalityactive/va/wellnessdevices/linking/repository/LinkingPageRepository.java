package com.vitalityactive.va.wellnessdevices.linking.repository;

import com.vitalityactive.va.wellnessdevices.dto.AssetsDto;
import com.vitalityactive.va.wellnessdevices.dto.PotentialPointsDto;

import java.util.Set;

public interface LinkingPageRepository {
    PotentialPointsDto getPotentialPoints(int typeKey);
    AssetsDto getAssets(String device);

    Set<Integer> getDeviceAvailableActivities(String device);
}
