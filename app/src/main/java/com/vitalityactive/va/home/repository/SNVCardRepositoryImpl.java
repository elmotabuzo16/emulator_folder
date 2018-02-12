package com.vitalityactive.va.home.repository;

import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.snv.SNVCardRepository;


/**
 * Created by paule.glenn.s.acuin on 11/23/2017.
 */

public class SNVCardRepositoryImpl extends BaseHomeCardRepositoryImpl implements SNVCardRepository {
    public SNVCardRepositoryImpl(HomeScreenCardSectionRepository repository) {
        super(repository, HomeCardType.SCREENNING_AND_VACCINATION);
    }

}
