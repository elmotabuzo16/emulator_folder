package com.vitalityactive.va.activerewards.rewards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vitalityactive.va.BasePresentedFragment;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.history.RewardsHistoryPresenter;
import com.vitalityactive.va.activerewards.rewards.history.dto.HistoricalRewardDTO;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;

import java.util.List;

import javax.inject.Inject;

public class RewardsHistoryFragment extends BasePresentedFragment<RewardsHistoryPresenter.UserInterface, RewardsHistoryPresenter> implements RewardsHistoryPresenter.UserInterface {
    @Inject
    RewardsHistoryPresenter rewardsHistoryPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rewards_history, container, false);
    }

    @Override
    protected void activityCreated(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getActiveRewardsDependencyInjector().inject(this);
    }

    @Override
    protected RewardsHistoryPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected RewardsHistoryPresenter getPresenter() {
        return rewardsHistoryPresenter;
    }

    @Override
    public void showRewardsHistory(List<HistoricalRewardDTO> rewardsHistory) {
        // TODO:
    }

    @Override
    public void showGenericError() {
        // TODO:
    }

    @Override
    public void showConnectionError() {
        // TODO:
    }
}
