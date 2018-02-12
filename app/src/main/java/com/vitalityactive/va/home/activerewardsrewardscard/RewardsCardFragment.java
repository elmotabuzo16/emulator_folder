package com.vitalityactive.va.home.activerewardsrewardscard;

import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.RewardId;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.dto.HomeCardItemDTO;
import com.vitalityactive.va.dto.HomeCardItemMetadataDTO;
import com.vitalityactive.va.home.HomeCardItemType;
import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.cards.HomeScreenCardFragment;
import com.vitalityactive.va.home.repository.BaseHomeCardRepository;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.date.LocalDate;

import javax.inject.Inject;
import javax.inject.Named;

import static org.threeten.bp.temporal.ChronoUnit.DAYS;

public class RewardsCardFragment extends HomeScreenCardFragment {

    private static final int MIN_DAYS_TO_EXPIRY = 3;

    @Inject
    @Named(DependencyNames.AR_REWARDS_HOME_CARD_REPOSITORY)
    protected BaseHomeCardRepository repository;

    private long rewardUniqueId;
    private int rewardId;
    private RewardsState rewardsState = RewardsState.LEARN_MORE;
    private RewardsExpiryDTO rewardsExpiry = new RewardsExpiryDTO();

    private HomeCardDTO card;

    @Override
    protected void setupDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected void loadData() {
        card = repository.getHomeCard();
        if (card.cardItems == null || card.cardItems.isEmpty()) {
            rewardsState = RewardsState.LEARN_MORE;
        } else {
            rewardUniqueId = TextUtilities.getLongFromString(getAwardedRewardId());
            rewardId = TextUtilities.getIntegerFromString(getMetadataValue(HomeCardType.MetadataType.REWARD_ID, card));
            if (rewardId == RewardId._CHOOSEREWARD) {
                rewardsExpiry = getRewardAndExpiryCounts(HomeCardItemType.REWARD_CHOICE);
                if (rewardsExpiry.spinCount == 1) {
                    rewardsState = RewardsState.CHOOSE_REWARD;
                } else if (rewardsExpiry.spinCount > 1) {
                    rewardsState = RewardsState.CHOOSE_REWARDS;
                }
            } else if (rewardId == RewardId._WHEELSPIN) {
                rewardsExpiry = getRewardAndExpiryCounts(HomeCardItemType.WHEEL_SPIN);
                if (rewardsExpiry.spinCount == 1) {
                    rewardsState = RewardsState.SPIN_NOW;
                } else if (rewardsExpiry.spinCount > 1) {
                    rewardsState = RewardsState.VIEW_AVAILABLE_SPINS;
                }
            }
        }
    }

    private RewardsExpiryDTO getRewardAndExpiryCounts(HomeCardItemType cardItemType) {
        int spinCount = 0;
        int expiryCount = 0;
        for (HomeCardItemDTO cardItem : card.cardItems) {
            if (cardItem.isAvailable() && cardItem.type == cardItemType) {
                spinCount++;
                if (daysUntil(cardItem.validTo) <= MIN_DAYS_TO_EXPIRY) {
                    expiryCount++;
                }
            }
        }
        return new RewardsExpiryDTO(spinCount, expiryCount);
    }

    private long daysUntil(String date) {
        LocalDate expiryDate = new LocalDate(date);
        LocalDate today = LocalDate.now();
        return DAYS.between(today.getValue().atStartOfDay(), expiryDate.getValue().atStartOfDay());
    }

    @Override
    protected void onClicked() {
        switch (rewardsState) {
            case LEARN_MORE:
                navigationCoordinator.navigateAfterARRewardsLearnMoreCardTapped(getActivity());
                break;
            case CHOOSE_REWARD:
                navigationCoordinator.navigateAfterARRewardsChooseRewardCardTapped(getActivity(), rewardUniqueId, rewardId);
                break;
            case CHOOSE_REWARDS:
            case VIEW_AVAILABLE_SPINS:
                navigationCoordinator.navigateAfterARViewAvailableSpinsCardTapped(getActivity());
                break;
            case SPIN_NOW:
                navigationCoordinator.navigateAfterARRewardsSpinNowCardTapped(getActivity(), rewardUniqueId, rewardId);
                break;
        }
    }

    @Override
    protected void configureView(View view) {
        if (rewardsState != null) {
            TextView title = view.findViewById(R.id.title);
            TextView subtitle = view.findViewById(R.id.subtitle);
            TextView action = view.findViewById(R.id.action_button);

            title.setText(rewardsState.getMainText());
            if (rewardsExpiry.spinCount > 1) {
                title.setText(getString(rewardsState.getMainText(), rewardsExpiry.spinCount));
            }
            if (rewardsExpiry.expiryCount > 0) {
                subtitle.setVisibility(View.VISIBLE);
                subtitle.setText(getString(rewardsState.getSubText(), rewardsExpiry.expiryCount));
            }
            action.setText(rewardsState.getActionText());
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.active_rewards_home_card_rewards;
    }

    private String getAwardedRewardId() {
        for (HomeCardItemDTO cardItem : card.cardItems) {
            if (!cardItem.isAvailable()) {
                continue;
            }
            for (HomeCardItemMetadataDTO cardItemMetadata : cardItem.cardItemMetadatas) {
                if (cardItemMetadata.type == HomeCardItemType.MetadataType.AWARDED_REWARD_ID) {
                    return cardItemMetadata.value;
                }
            }
        }
        return "";
    }

    public static class Factory implements HomeScreenCardFragment.Factory {

        @Override
        public HomeScreenCardFragment buildFragment() {
            return new RewardsCardFragment();
        }
    }

}
