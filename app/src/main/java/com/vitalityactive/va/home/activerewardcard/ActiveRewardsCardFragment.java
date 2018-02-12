package com.vitalityactive.va.home.activerewardcard;

import android.view.View;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.home.HomeFragment;
import com.vitalityactive.va.home.cards.HomeScreenCardFragment;
import com.vitalityactive.va.home.repository.BaseHomeCardRepository;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import javax.inject.Inject;
import javax.inject.Named;

public class ActiveRewardsCardFragment extends HomeScreenCardFragment {
    enum State {ACTIVATED_WILL_START_SOON, IN_PROGRESS, ACHIEVED, NOT_ACTIVATED_GET_STARTED, SHOULD_COMPLETE_VHR}

    @Inject
    @Named(DependencyNames.ACTIVE_REWARDS_HOME_CARD_REPOSITORY)
    BaseHomeCardRepository repository;
    @Inject
    InsurerConfigurationRepository insurerConfigurationRepository;
    @Inject
    DateFormattingUtilities dateFormattingUtilities;

    private HomeCardDTO card;

    private boolean shouldCompleteVhrBeforeActivation;
    private LocalDate startDate;
    private LocalDate endDate;
    public int points;
    public int requiredPoints;

    public State getState() {
        if (shouldCompleteVhrBeforeActivation) {
            return State.SHOULD_COMPLETE_VHR;
        }

        if (card.isActivated()) {
            return State.ACTIVATED_WILL_START_SOON;
        }

        if (card.hasNotStarted()) {
            return State.NOT_ACTIVATED_GET_STARTED;
        }

        if (card.isInProgress()) {
            return State.IN_PROGRESS;
        }

        if (card.isAchieved()) {
            return State.ACHIEVED;
        }

        return State.NOT_ACTIVATED_GET_STARTED;
    }

    @Override
    protected void setupDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected void loadData() {
        card = repository.getHomeCard();
        shouldCompleteVhrBeforeActivation = card.hasNotStarted() && hasArVhrProductFeature();
        points = (int)card.amountCompleted;
        requiredPoints = card.total;
        startDate = new LocalDate(card.goalStartDate);
        endDate = new LocalDate(card.goalEndDate);
    }

    private boolean hasArVhrProductFeature() {
        return insurerConfigurationRepository.requireVhrBeforeAr();
    }

    @Override
    protected void onClicked() {
        switch (getState()) {
            case SHOULD_COMPLETE_VHR:
                ((HomeFragment) getParentFragment()).startCheckingVhrStatus();
                break;
            case NOT_ACTIVATED_GET_STARTED:
                navigationCoordinator.navigateAfterActiveRewardsSelected(getActivity());
                break;
            case ACTIVATED_WILL_START_SOON:
            case IN_PROGRESS:
            case ACHIEVED:
                navigationCoordinator.navigateVitalityActiveRewardsFromHome(getActivity());
                break;
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.active_rewards_home_card;
    }

    @Override
    protected void configureView(View view) {
        ActiveRewardsProgressViewHolder viewHolder = new ActiveRewardsProgressViewHolder(view);
        viewHolder.layoutFor(this);
        viewHolder.bindWith(this);
    }

    public String getLongFormattedStartDate() {
        return dateFormattingUtilities.formatWeekdayDateMonthYear(startDate);
    }

    public String getLongFormattedRange(){
        return dateFormattingUtilities.formatRangeDateMonthAbbreviatedYearDay(startDate, endDate);
    }

    public static class Factory implements HomeScreenCardFragment.Factory {
        @Override
        public HomeScreenCardFragment buildFragment() {
            return new ActiveRewardsCardFragment();
        }
    }
}
