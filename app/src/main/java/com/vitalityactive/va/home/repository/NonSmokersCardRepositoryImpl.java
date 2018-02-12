package com.vitalityactive.va.home.repository;

import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.nonsmokerscard.NonSmokersCardRepository;

public class NonSmokersCardRepositoryImpl extends BaseHomeCardRepositoryImpl implements NonSmokersCardRepository {
    public NonSmokersCardRepositoryImpl(HomeScreenCardSectionRepository repository) {
        super(repository, HomeCardType.NON_SMOKERS_DECLARATION);
    }
}
