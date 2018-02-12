package com.vitalityactive.va.wellnessdevices.landing.repository;

import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.base.RealmInt;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesActivityMap;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesAssets;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesPartner;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesPartnerLink;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesPointsConditions;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesPointsEntryDetails;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesPotentialPoints;
import com.vitalityactive.va.wellnessdevices.dto.PotentialPointsDto;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DeviceListRepositoryImpl implements DeviceListRepository {
    private final DataStore dataStore;
    private final Persister persister;

    public DeviceListRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
        this.persister = new Persister(dataStore);
    }

    @Override
    public boolean persistDeviceListResponse(GetFullListResponse response) {
        dataStore.removeAll(WellnessDevicesPartner.class);
        dataStore.removeAll(WellnessDevicesPartnerLink.class);
        dataStore.removeAll(WellnessDevicesAssets.class);

        List<GetFullListResponse.Partner> partners = new ArrayList<>();
        for (GetFullListResponse.Market market : response.markets) {
            partners.add(market.partner);
        }

        persister.addOrUpdateModels(partners, new Persister.InstanceCreator<Model, GetFullListResponse.Partner>() {
            @Override
            public Model create(GetFullListResponse.Partner partner) {
                return new WellnessDevicesPartner(partner);
            }
        });

        persister.addOrUpdateModels(response.markets, new Persister.InstanceCreator<Model, GetFullListResponse.Market>() {
            @Override
            public Model create(GetFullListResponse.Market market) {
                return new WellnessDevicesAssets(market);
            }
        });

        return true;
    }

    @Override
    public List<WellnessDevicesPartner> getPartners() {
        return dataStore.getModels(WellnessDevicesPartner.class,
                new DataStore.ModelListMapper<WellnessDevicesPartner, WellnessDevicesPartner>() {
                    @Override
                    public List<WellnessDevicesPartner> mapModels(List<WellnessDevicesPartner> models) {
                        return dataStore.copyFromDataStore(models);
                    }
                });
    }

    @Override
    public boolean persistPotentialPoints(EventType eventType) {
        if (eventType == null ||
                eventType.eventType == null ||
                eventType.eventType.isEmpty()) {
            return false;
        } else {
            dataStore.removeAll(WellnessDevicesPotentialPoints.class);
            dataStore.removeAll(WellnessDevicesPointsConditions.class);
            dataStore.removeAll(WellnessDevicesPointsEntryDetails.class);

            persister.addOrUpdateModels(eventType.eventType.get(0).eventType,
                    new Persister.InstanceCreator<Model, EventType>() {
                        @Override
                        public Model create(EventType eventTypeEntry) {
                            return new WellnessDevicesPotentialPoints(eventTypeEntry);
                        }
                    });
            return true;
        }
    }

    @Override
    public List<PotentialPointsDto> getPotentialPoints() {
        return dataStore.getModels(WellnessDevicesPotentialPoints.class,
                new DataStore.ModelListMapper<WellnessDevicesPotentialPoints, PotentialPointsDto>() {
                    @Override
                    public List<PotentialPointsDto> mapModels(List<WellnessDevicesPotentialPoints> models) {
                        List<PotentialPointsDto> mappedModels = new ArrayList<>();
                        for (WellnessDevicesPotentialPoints potentialPoints : models) {
                            mappedModels.add(new PotentialPointsDto(potentialPoints));
                        }
                        return mappedModels;
                    }
                });
    }

    @Override
    public boolean persistDeviceActivityMap(Map<String, int[]> activityMap) {
        dataStore.removeAll(WellnessDevicesActivityMap.class);

        persister.addOrUpdateModels(new ArrayList<>(activityMap.entrySet()),
                new Persister.InstanceCreator<Model, Map.Entry<String, int[]>>() {
                    @Override
                    public Model create(Map.Entry<String, int[]> src) {
                        return new WellnessDevicesActivityMap(src);
                    }
                });
        return true;
    }

    @Override
    public Map<String, Set<Integer>> getDeviceActivityMap() {
        Map<String, Set<Integer>> result = new HashMap<>();
        List<WellnessDevicesActivityMap> list = dataStore.getModels(WellnessDevicesActivityMap.class,
                new DataStore.ModelListMapper<WellnessDevicesActivityMap, WellnessDevicesActivityMap>() {
                    @Override
                    public List<WellnessDevicesActivityMap> mapModels(List<WellnessDevicesActivityMap> models) {
                        return models;
                    }
                });
        for(WellnessDevicesActivityMap entry : list){
            Set<Integer> features = new HashSet<>();
            for(RealmInt feature : entry.getFeatures()){
                features.add(feature.getValue());
            }
            result.put(entry.getDevice(), features);
        }
        return result;
    }

    @Override
    public boolean hasLinkedDevice() {
        for (WellnessDevicesPartner partner : getPartners()) {
            if (hasLinkedDevice(partner)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasLinkedDevice(WellnessDevicesPartner partner) {
        return !"UNLINKED".equals(partner.getPartnerLinkedStatus());
    }
}
