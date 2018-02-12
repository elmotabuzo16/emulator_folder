package com.vitalityactive.va.wellnessdevices.landing.repository;

import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesPartner;
import com.vitalityactive.va.wellnessdevices.dto.PotentialPointsDto;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DeviceListRepository {
    boolean persistDeviceListResponse(GetFullListResponse response);
    List<WellnessDevicesPartner> getPartners();

    boolean persistPotentialPoints(EventType eventType);
    List<PotentialPointsDto> getPotentialPoints();

    boolean persistDeviceActivityMap(Map<String, int[]> activityMap);
    Map<String, Set<Integer>> getDeviceActivityMap();

    boolean hasLinkedDevice();
}
