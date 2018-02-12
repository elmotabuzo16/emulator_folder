package com.vitalityactive.va.home.repository;

import android.support.annotation.NonNull;

import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.home.HomeCardType;

public class BaseHomeCardRepositoryImpl implements BaseHomeCardRepository {
    protected final HomeScreenCardSectionRepository repository;
    protected final HomeCardType cardType;

    public BaseHomeCardRepositoryImpl(HomeScreenCardSectionRepository repository, HomeCardType cardType) {
        this.cardType = cardType;
        this.repository = repository;
    }

    public HomeCardDTO getHomeCard() {
        return getHomeCard(cardType);
    }

    @NonNull
    protected HomeCardDTO getHomeCard(HomeCardType cardType) {
        HomeCardDTO homeCard = repository.getHomeCard(cardType);
        return homeCard == null ? new HomeCardDTO() : homeCard;
    }
}
