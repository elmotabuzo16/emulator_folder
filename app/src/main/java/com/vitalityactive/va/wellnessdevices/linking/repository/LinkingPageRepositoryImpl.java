package com.vitalityactive.va.wellnessdevices.linking.repository;

import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.base.RealmInt;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesActivityMap;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesAssets;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesPotentialPoints;
import com.vitalityactive.va.wellnessdevices.dto.AssetsDto;
import com.vitalityactive.va.wellnessdevices.dto.PotentialPointsDto;

import java.util.HashSet;
import java.util.Set;

public class LinkingPageRepositoryImpl implements LinkingPageRepository {
    private final static String TYPE_KEY = "typeKey";

    private final DataStore dataStore;
    private final Persister persister;

    public LinkingPageRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
        this.persister = new Persister(dataStore);
    }

    @Override
    public PotentialPointsDto getPotentialPoints(int typeKey) {
        return dataStore.getModelInstance(WellnessDevicesPotentialPoints.class,
                new DataStore.ModelMapper<WellnessDevicesPotentialPoints, PotentialPointsDto>() {
                    @Override
                    public PotentialPointsDto mapModel(WellnessDevicesPotentialPoints model) {
                        return new PotentialPointsDto(model);
                    }
                }, "typeKey", typeKey);
    }

    @Override
    public AssetsDto getAssets(String device) {
        return dataStore.getModelInstance(WellnessDevicesAssets.class,
                new DataStore.ModelMapper<WellnessDevicesAssets, AssetsDto>() {
                    @Override
                    public AssetsDto mapModel(WellnessDevicesAssets model) {
                        return new AssetsDto(model);
                    }
                }, "device", device);
    }

    @Override
    public Set<Integer> getDeviceAvailableActivities(String device) {
        return dataStore.getModelInstance(WellnessDevicesActivityMap.class,
                new DataStore.ModelMapper<WellnessDevicesActivityMap, Set<Integer>>() {
                    @Override
                    public Set<Integer> mapModel(WellnessDevicesActivityMap model) {
                        Set<Integer> localRes = new HashSet<>();
                        for (RealmInt feature : model.getFeatures()) {
                            localRes.add(feature.getValue());
                        }
                        return localRes;
                    }
                }, "device", device);
    }
}
