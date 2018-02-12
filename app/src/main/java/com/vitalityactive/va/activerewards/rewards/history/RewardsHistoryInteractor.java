package com.vitalityactive.va.activerewards.rewards.history;

import android.support.annotation.NonNull;

import com.vitalityactive.va.activerewards.rewards.history.dto.HistoricalRewardDTO;
import com.vitalityactive.va.activerewards.rewards.persistence.RewardsRepository;
import com.vitalityactive.va.activerewards.rewards.service.RewardsServiceClient;
import com.vitalityactive.va.activerewards.rewards.service.RewardsServiceResponse;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.WebServiceResponseParser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@ActiveRewardsScope
class RewardsHistoryInteractor implements WebServiceResponseParser<RewardsServiceResponse> {

    private RewardsServiceClient rewardsServiceClient;
    private RewardsRepository rewardsRepository;
    private EventDispatcher eventDispatcher;

    @Inject
    RewardsHistoryInteractor(RewardsServiceClient rewardsServiceClient,
                             RewardsRepository rewardsRepository,
                             EventDispatcher eventDispatcher) {
        this.rewardsServiceClient = rewardsServiceClient;
        this.rewardsRepository = rewardsRepository;
        this.eventDispatcher = eventDispatcher;
    }

    void fetchRewardsHistory() {
        if (rewardsServiceClient.isRewardsHistoryRequestInProgress()) {
            return;
        }
        rewardsServiceClient.fetchRewardsHistory(this);
    }

    @Override
    public void parseResponse(RewardsServiceResponse response) {
        if (rewardsRepository.persistRewardsHistory(response)) {
            eventDispatcher.dispatchEvent(RewardsHistoryRequestCompletedEvent.SUCCESSFUL);
        }
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {

    }

    @Override
    public void handleGenericError(Exception exception) {

    }

    @Override
    public void handleConnectionError() {

    }

    @NonNull
    List<HistoricalRewardDTO> getRewardsHistory() {
        return new ArrayList<>();
    }
}
