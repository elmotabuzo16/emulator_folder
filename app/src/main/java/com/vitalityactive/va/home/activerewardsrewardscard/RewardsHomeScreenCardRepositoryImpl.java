package com.vitalityactive.va.home.activerewardsrewardscard;

import com.vitalityactive.va.dto.RewardHomeCardDTO;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.RewardHomeCard;

import io.realm.RealmQuery;

public class RewardsHomeScreenCardRepositoryImpl implements RewardsHomeScreenCardRepository {

    private final DataStore dataStore;

    public RewardsHomeScreenCardRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public RewardHomeCardDTO getHomeCard(final String rewardId, final String awardedRewardId) {
        return dataStore.getModelInstance(RewardHomeCard.class,
                new DataStore.SingleModelQueryExecutor<RewardHomeCard, RealmQuery<RewardHomeCard>>() {
                    @Override
                    public RewardHomeCard executeQueries(RealmQuery<RewardHomeCard> initialQuery) {
                        return initialQuery
                                .equalTo("rewardId", rewardId)
                                .equalTo("awardedRewardId", awardedRewardId)
                                .findFirst();
                    }
                },
                new DataStore.ModelMapper<RewardHomeCard, RewardHomeCardDTO>() {
                    @Override
                    public RewardHomeCardDTO mapModel(RewardHomeCard model) {
                        return new RewardHomeCardDTO(model);
                    }
                });
    }
}
