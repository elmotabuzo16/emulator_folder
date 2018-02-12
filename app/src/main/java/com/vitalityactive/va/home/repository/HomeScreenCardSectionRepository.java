package com.vitalityactive.va.home.repository;

import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.dto.RewardHomeCardDTO;
import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;

import java.util.List;

public interface HomeScreenCardSectionRepository {
    boolean persistCardSectionResponse(HomeScreenCardStatusResponse response);

    List<HomeCardDTO> getHomeCards();

    List<RewardHomeCardDTO> getRewardHomeCards(HomeCardType rewardCardType);

    HomeCardDTO getHomeCard(HomeCardType homeCardType);

}
