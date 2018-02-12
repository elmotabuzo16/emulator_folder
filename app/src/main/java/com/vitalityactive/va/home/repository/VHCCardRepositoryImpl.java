package com.vitalityactive.va.home.repository;

import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.vhc.VHCCardRepository;

public class VHCCardRepositoryImpl extends BaseHomeCardRepositoryImpl implements VHCCardRepository {
    public VHCCardRepositoryImpl(HomeScreenCardSectionRepository repository) {
        super(repository, HomeCardType.VITALITY_HEALTH_CHECK);
    }
}
