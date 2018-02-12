package com.vitalityactive.va.home.repository;

import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.mwb.MWBCardRepository;

/**
 * Created by paule.glenn.s.acuin on 1/29/2018.
 */
public class MWBCardRepositoryImpl extends BaseHomeCardRepositoryImpl implements MWBCardRepository {
    public MWBCardRepositoryImpl(HomeScreenCardSectionRepository repository) {
        super(repository, HomeCardType.MENTAL_WELLBIENG);
    }
}

