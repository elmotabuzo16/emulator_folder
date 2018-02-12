package com.vitalityactive.va.home.repository;

import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.vna.VNACardRepository;

public class VNACardRepositoryImpl extends BaseHomeCardRepositoryImpl implements VNACardRepository {
    public VNACardRepositoryImpl(HomeScreenCardSectionRepository repository) {
        super(repository, HomeCardType.VITALITY_NUTRITION_ASSESSMENT);
    }
}
