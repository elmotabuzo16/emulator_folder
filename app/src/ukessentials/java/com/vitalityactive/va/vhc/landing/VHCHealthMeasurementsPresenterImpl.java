package com.vitalityactive.va.vhc.landing;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.service.VitalityAgeServiceClient;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;
import com.vitalityactive.va.vhc.service.HealthAttributeServiceClient;
import com.vitalityactive.va.InsurerConfigurationRepository;

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

    }
}
