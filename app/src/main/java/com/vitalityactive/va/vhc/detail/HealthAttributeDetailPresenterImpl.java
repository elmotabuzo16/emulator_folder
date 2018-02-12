package com.vitalityactive.va.vhc.detail;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.vhc.dto.HealthAttributeDTO;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;

import java.util.List;

public class HealthAttributeDetailPresenterImpl implements HealthAttributeDetailPresenter {
    private final HealthAttributeRepository healthAttributeRepository;
    private final InsurerConfigurationRepository insurerConfigurationRepository;
    private UserInterface userInterface;
    private int healthAttributeGroupFeatureType;

    public HealthAttributeDetailPresenterImpl(HealthAttributeRepository healthAttributeRepository,
                                              InsurerConfigurationRepository insurerConfigurationRepository) {
        this.healthAttributeRepository = healthAttributeRepository;
        this.insurerConfigurationRepository = insurerConfigurationRepository;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        List<HealthAttributeDTO> healthAttributeDTOs = healthAttributeRepository.getHealthAttributesWithReadings(healthAttributeGroupFeatureType);

        String groupDescription = healthAttributeRepository.getGroupDescription(healthAttributeGroupFeatureType);
        userInterface.setActionBarTitle(groupDescription);

        userInterface.setUpRecyclerView(healthAttributeDTOs,
                insurerConfigurationRepository.getCurrentMembershipPeriodStart(),
                insurerConfigurationRepository.getCurrentMembershipPeriodEnd(),
                groupDescription);
    }

    @Override
    public void onUserInterfaceAppeared() {

    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {

    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void setHealthAttributeGroupFeatureType(int healthAttributeGroupFeatureType) {
        this.healthAttributeGroupFeatureType = healthAttributeGroupFeatureType;
    }
}
