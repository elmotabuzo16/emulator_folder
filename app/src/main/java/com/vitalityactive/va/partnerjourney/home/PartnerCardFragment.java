package com.vitalityactive.va.partnerjourney.home;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.cards.HomeScreenCardFragment;
import com.vitalityactive.va.partnerjourney.PartnerType;

import javax.inject.Inject;

public abstract class PartnerCardFragment extends HomeScreenCardFragment {
    @Inject
    PartnerCardRepository repository;
    private HomeCardDTO card;
    private PartnerTypeHomeCardConfiguration rewardType;

    @Override
    protected void setupDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected void loadData() {
        rewardType = getRewardType(getPartnerType());
        card = repository.getHomeCard(getPartnerType());
    }

    @Override
    protected void onClicked() {
        navigationCoordinator.navigateAfterPartnerHomeCardTapped(getActivity(), getPartnerType());
    }

    @Override
    protected void configureView(View view) {
        view.<TextView>findViewById(R.id.heading).setText(rewardType.getHeaderText());
        view.<TextView>findViewById(R.id.title).setText(rewardType.getMainText());
        view.<TextView>findViewById(R.id.button).setTextColor(ContextCompat.getColor(view.getContext(), rewardType.getColor()));
        view.<TextView>findViewById(R.id.button).setText(rewardType.getActionText());
        showSmallLogo(view);
        showCompletedOnMainLogo(view, card.isDone());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.view_home_card_enjoy_rewards;
    }

    @NonNull
    protected abstract PartnerType getPartnerType();

    private PartnerTypeHomeCardConfiguration getRewardType(PartnerType partnerType) {
        switch (partnerType) {
            case HEALTH:
                return PartnerTypeHomeCardConfiguration.HEALTH_REWARDS;
            case WELLNESS:
                return PartnerTypeHomeCardConfiguration.WELLNESS_REWARDS;
            case REWARDS:
                return PartnerTypeHomeCardConfiguration.GREAT_REWARDS;
            case CORPORATE:
                return PartnerTypeHomeCardConfiguration.HEALTH_SERVICES;
        }
        return PartnerTypeHomeCardConfiguration.HEALTH_REWARDS;
    }

    private void showSmallLogo(View rootView) {
        Drawable drawable = getTintedDrawable(rewardType.getHeaderIcon(), rewardType.getColor());
        rootView.<ImageView>findViewById(R.id.small_logo).setColorFilter(ContextCompat.getColor(getContext(), rewardType.getColor()));
        setImage(rootView, R.id.small_logo, drawable);
    }

    private void showCompletedOnMainLogo(View rootView, boolean completed) {
        int tintColor = rewardType.getColor();
        Drawable drawable = completed ?
                getTintedDrawable(R.drawable.completed_main, tintColor) :
                getDrawable(rewardType.getMainIcon());
        setImage(rootView, R.id.large_logo, drawable);
    }

    public static class HealthPartnerCardFragment extends PartnerCardFragment {
        @NonNull
        @Override
        protected PartnerType getPartnerType() {
            return PartnerType.HEALTH;
        }
    }

    public static class RewardsPartnerCardFragment extends PartnerCardFragment {
        @NonNull
        @Override
        protected PartnerType getPartnerType() {
            return PartnerType.REWARDS;
        }
    }

    public static class WellnessPartnerCardFragment extends PartnerCardFragment {
        @NonNull
        @Override
        protected PartnerType getPartnerType() {
            return PartnerType.WELLNESS;
        }
    }

    public static class CorporatePartnerCardFragment extends PartnerCardFragment {
        @NonNull
        @Override
        protected PartnerType getPartnerType() {
            return PartnerType.CORPORATE;
        }
    }

    public static class Factory implements HomeScreenCardFragment.Factory {
        private final HomeCardType cardType;

        public Factory(HomeCardType cardType) {
            this.cardType = cardType;
        }

        @Override
        public HomeScreenCardFragment buildFragment() {
            switch (cardType) {
                case REWARD_PARTNERS:
                    return new RewardsPartnerCardFragment();
                case WELLNESS_PARTNERS:
                    return new WellnessPartnerCardFragment();
                case HEALTH_PARTNERS:
                    return new HealthPartnerCardFragment();
                case HEALTH_SERVICES:
                    return new CorporatePartnerCardFragment();
            }
            return new WellnessPartnerCardFragment();
        }
    }
}
