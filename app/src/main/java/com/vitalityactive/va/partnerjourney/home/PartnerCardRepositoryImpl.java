package com.vitalityactive.va.partnerjourney.home;

import android.support.annotation.NonNull;

import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.repository.BaseHomeCardRepositoryImpl;
import com.vitalityactive.va.home.repository.HomeScreenCardSectionRepository;
import com.vitalityactive.va.partnerjourney.PartnerType;

public class PartnerCardRepositoryImpl extends BaseHomeCardRepositoryImpl implements PartnerCardRepository {
    public PartnerCardRepositoryImpl(HomeScreenCardSectionRepository repository) {
        super(repository, HomeCardType.REWARD_PARTNERS);
    }

    @NonNull
    @Override
    public HomeCardDTO getHomeCard(PartnerType partnerType) {
        switch (partnerType) {
            case REWARDS:
                return getHomeCard(HomeCardType.REWARD_PARTNERS);
            case WELLNESS:
                return getHomeCard(HomeCardType.WELLNESS_PARTNERS);
            case HEALTH:
                return getHomeCard(HomeCardType.HEALTH_PARTNERS);
        }

        return getHomeCard(HomeCardType.WELLNESS_PARTNERS);
    }
}
