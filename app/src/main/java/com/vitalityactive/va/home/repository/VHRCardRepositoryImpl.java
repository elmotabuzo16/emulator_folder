package com.vitalityactive.va.home.repository;

import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.vhr.VHRCardRepository;

public class VHRCardRepositoryImpl extends BaseHomeCardRepositoryImpl implements VHRCardRepository {
    public VHRCardRepositoryImpl(HomeScreenCardSectionRepository repository) {
        super(repository, HomeCardType.VITALITY_HEALTH_REVIEW);
    }
}
