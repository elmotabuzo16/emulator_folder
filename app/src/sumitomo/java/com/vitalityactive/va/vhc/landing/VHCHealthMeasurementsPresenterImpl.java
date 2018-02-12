package com.vitalityactive.va.vhc.landing;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.service.VitalityAgeServiceClient;
import com.vitalityactive.va.vhc.dto.HealthAttributeGroupDTO;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;
import com.vitalityactive.va.vhc.service.HealthAttributeServiceClient;
import com.vitalityactive.va.InsurerConfigurationRepository;

import java.util.Collections;
import java.util.Comparator;

public class VHCHealthMeasurementsPresenterImpl extends BaseVHCHealthMeasurementsPresenterImpl {

    public VHCHealthMeasurementsPresenterImpl(HealthAttributeServiceClient healthAttributeServiceClient,
                                                  HealthAttributeRepository healthAttributeRepository,
                                                  EventDispatcher eventDispatcher,
                                                  Scheduler scheduler,
                                                  InsurerConfigurationRepository insurerConfigurationRepository, VitalityAgeServiceClient vitalityAgeServiceClient, DeviceSpecificPreferences deviceSpecificPreferences) {
        super(healthAttributeServiceClient, healthAttributeRepository, eventDispatcher, scheduler, insurerConfigurationRepository, vitalityAgeServiceClient, deviceSpecificPreferences );
    }

    @Override
    protected void marketUiUpdate() {
        Collections.sort(incompleteGroups, new Comparator<HealthAttributeGroupDTO>() {
            @Override

            public int compare(HealthAttributeGroupDTO o1, HealthAttributeGroupDTO o2) {
                final int featureTypeKey1 = o1.getFeatureTypeKey();
                final int featureTypeKey2 = o2.getFeatureTypeKey();
                return Integer.compare(featureTypeKey1,featureTypeKey2);
            }
        });
    }
}
