package com.vitalityactive.va.home.repository;

import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.dto.RewardHomeCardDTO;
import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.HomeCard;
import com.vitalityactive.va.persistence.models.RewardHomeCard;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenCardSectionRepositoryImpl implements HomeScreenCardSectionRepository {
    private final DataStore dataStore;
    private final Persister persister;

    public HomeScreenCardSectionRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
        persister = new Persister(dataStore);
    }

    @Override
    public boolean persistCardSectionResponse(HomeScreenCardStatusResponse response) {
        dataStore.removeAll(HomeCard.class);
        dataStore.removeAll(RewardHomeCard.class);
        for (final HomeScreenCardStatusResponse.Section section : response.sections) {
            persister.addModels(section.cards, new Persister.InstanceCreator<Model, HomeScreenCardStatusResponse.Card>() {
                @Override
                public Model create(HomeScreenCardStatusResponse.Card model) {
                    if ((HomeCardType.VOUCHERS.getTypeKey().equals(model.typeKey) ||
                            HomeCardType.REWARD_SELECTION.getTypeKey().equals(model.typeKey))) {
                        return RewardHomeCard.create(model);
                    } else {
                        return HomeCard.create(model, section.typeKey);
                    }
                }
            });
        }
        return true;
    }

    @Override
    public List<HomeCardDTO> getHomeCards() {
        return dataStore.getModels(HomeCard.class, new DataStore.ModelListMapper<HomeCard, HomeCardDTO>() {
            @Override
            public List<HomeCardDTO> mapModels(List<HomeCard> models) {
                List<HomeCardDTO> mappedModels = new ArrayList<>();
                for (HomeCard card : models) {
                    mappedModels.add(new HomeCardDTO(card));
                }
                return mappedModels;
            }
        });
    }

    @Override
    public List<RewardHomeCardDTO> getRewardHomeCards(HomeCardType rewardCardType) {
        return dataStore.getModels(RewardHomeCard.class, new DataStore.ModelListMapper<RewardHomeCard, RewardHomeCardDTO>() {
            @Override
            public List<RewardHomeCardDTO> mapModels(List<RewardHomeCard> models) {
                List<RewardHomeCardDTO> mappedModels = new ArrayList<>();
                for (RewardHomeCard card : models) {
                    mappedModels.add(new RewardHomeCardDTO(card));
                }
                return mappedModels;
            }
        }, "type", rewardCardType.getTypeKey());
    }

    @Override
    public HomeCardDTO getHomeCard(HomeCardType homeCardType) {
        return dataStore.getModelInstance(HomeCard.class, new DataStore.ModelMapper<HomeCard, HomeCardDTO>() {
            @Override
            public HomeCardDTO mapModel(HomeCard model) {
                return new HomeCardDTO(model);
            }
        }, "type", homeCardType.getTypeKey());
    }
}
