package com.vitalityactive.va.home.cards;

import android.support.annotation.NonNull;

import com.vitalityactive.va.R;
import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.HomeSectionType;
import com.vitalityactive.va.home.NotImplementedCardFragment;
import com.vitalityactive.va.home.activerewardcard.ActiveRewardsCardFragment;
import com.vitalityactive.va.home.activerewardsrewardscard.RewardsCardFragment;
import com.vitalityactive.va.home.mwb.MWBCardFragment;
import com.vitalityactive.va.home.nonsmokerscard.NonSmokersCardFragment;
import com.vitalityactive.va.home.snv.SNVCardFragment;
import com.vitalityactive.va.home.vhc.VHCCardFragment;
import com.vitalityactive.va.home.vhr.VHRCardFragment;
import com.vitalityactive.va.home.vna.VNACardFragment;
import com.vitalityactive.va.home.wellnessdevices.WellnessDevicesCardFragment;
import com.vitalityactive.va.partnerjourney.home.PartnerCardFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeScreenCardSection {
    public int nameResourceId;
    public List<HomeScreenCardFragment.Factory> cardFactories = new ArrayList<>();

    public HomeScreenCardSection(int resourceId) {
        nameResourceId = resourceId;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " with nameResourceId=" + nameResourceId;
    }

    public static class Builder {
        private Map<HomeSectionType, List<HomeScreenCardFragment.Factory>> factories = new HashMap<>();

        public Builder() {
            factories.put(HomeSectionType.KNOW_YOUR_HEALTH, new ArrayList<HomeScreenCardFragment.Factory>());
            factories.put(HomeSectionType.IMPROVE_YOUR_HEALTH, new ArrayList<HomeScreenCardFragment.Factory>());
            factories.put(HomeSectionType.GET_REWARDED, new ArrayList<HomeScreenCardFragment.Factory>());
        }

        public Builder add(HomeSectionType sectionType, HomeCardType cardType) {
            return add(sectionType, resolveFactoryForCardType(cardType));
        }

        public Builder add(HomeSectionType sectionType, HomeScreenCardFragment.Factory factory) {
            getFactories(sectionType).add(factory);
            return this;
        }

        public void addTestCards() {
            for (int i = 0; i < 1; i++) {
                add(HomeSectionType.GET_REWARDED, HomeCardType.UNKNOWN);
            }
            for (int i = 0; i < 2; i++) {
                add(HomeSectionType.IMPROVE_YOUR_HEALTH, HomeCardType.UNKNOWN);
            }
            for (int i = 0; i < 3; i++) {
                add(HomeSectionType.KNOW_YOUR_HEALTH, HomeCardType.UNKNOWN);
            }
        }

        public List<HomeScreenCardSection> build() {
            ArrayList<HomeScreenCardSection> sections = new ArrayList<>();
            if (!getFactories(HomeSectionType.KNOW_YOUR_HEALTH).isEmpty()) {
                sections.add(knowYourHealth(getFactories(HomeSectionType.KNOW_YOUR_HEALTH)));
            }
            if (!getFactories(HomeSectionType.IMPROVE_YOUR_HEALTH).isEmpty()) {
                sections.add(improveYourHealth(getFactories(HomeSectionType.IMPROVE_YOUR_HEALTH)));
            }
            if (!getFactories(HomeSectionType.GET_REWARDED).isEmpty()) {
                sections.add(getRewarded(getFactories(HomeSectionType.GET_REWARDED)));
            }
            return sections;
        }

        public List<HomeScreenCardFragment.Factory> getFactories(HomeSectionType sectionType) {
            List<HomeScreenCardFragment.Factory> sectionFactories = this.factories.get(sectionType);
            if (sectionFactories == null) {
                sectionFactories = new ArrayList<>();
                factories.put(HomeSectionType.GET_REWARDED, sectionFactories);
            }
            return sectionFactories;
        }

        @NonNull
        public static HomeScreenCardSection getRewarded() {
            ArrayList<HomeScreenCardFragment.Factory> factories = new ArrayList<>();
            factories.add(new ActiveRewardsCardFragment.Factory());
            factories.add(new VHCCardFragment.Factory());
            return getRewarded(factories);
        }

        @NonNull
        public static HomeScreenCardSection getRewarded(List<HomeScreenCardFragment.Factory> factories) {
            HomeScreenCardSection section = new HomeScreenCardSection(R.string.section_title_get_rewarded_278);
            section.cardFactories.addAll(factories);
            return section;
        }

        @NonNull
        public static HomeScreenCardSection improveYourHealth() {
            ArrayList<HomeScreenCardFragment.Factory> factories = new ArrayList<>();
            factories.add(new VHCCardFragment.Factory());
            return improveYourHealth(factories);
        }

        @NonNull
        public static HomeScreenCardSection improveYourHealth(List<HomeScreenCardFragment.Factory> factories) {
            HomeScreenCardSection section = new HomeScreenCardSection(R.string.section_title_improve_your_health_277);
            section.cardFactories.addAll(factories);
            return section;
        }

        @NonNull
        public static HomeScreenCardSection knowYourHealth() {
            ArrayList<HomeScreenCardFragment.Factory> factories = new ArrayList<>();
            factories.add(new NonSmokersCardFragment.Factory());
            return knowYourHealth(factories);
        }

        @NonNull
        public static HomeScreenCardSection knowYourHealth(List<HomeScreenCardFragment.Factory> factories) {
            HomeScreenCardSection section = new HomeScreenCardSection(R.string.section_title_know_your_health_276);
            section.cardFactories.addAll(factories);
            return section;
        }

        private HomeScreenCardFragment.Factory resolveFactoryForCardType(HomeCardType cardType) {
            switch (cardType) {
                case ACTIVE_REWARDS:
                    return new ActiveRewardsCardFragment.Factory();
                case NON_SMOKERS_DECLARATION:
                    return new NonSmokersCardFragment.Factory();
                case VITALITY_HEALTH_CHECK:
                    return new VHCCardFragment.Factory();
                case VITALITY_HEALTH_REVIEW:
                    return new VHRCardFragment.Factory();
                case SCREENNING_AND_VACCINATION:
                    return new SNVCardFragment.Factory();
                case MENTAL_WELLBIENG:
                    return new MWBCardFragment.Factory();
                case WELLNESS_DEVICES:
                    return new WellnessDevicesCardFragment.Factory();
                case VITALITY_NUTRITION_ASSESSMENT:
                    return new VNACardFragment.Factory();
                case HEALTH_PARTNERS:
                case WELLNESS_PARTNERS:
                case REWARD_PARTNERS:
                case HEALTH_SERVICES:
                    return new PartnerCardFragment.Factory(cardType);
                case REWARDS:
                    return new RewardsCardFragment.Factory();
            }
            return new NotImplementedCardFragment.Factory();
        }
    }
}
