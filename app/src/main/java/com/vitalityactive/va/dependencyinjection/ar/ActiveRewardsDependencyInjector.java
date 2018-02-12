package com.vitalityactive.va.dependencyinjection.ar;

import com.vitalityactive.va.activerewards.landing.ActiveRewardsLandingActivity;
import com.vitalityactive.va.activerewards.participatingpartners.ActiveRewardsParticipatingPartnersActivity;
import com.vitalityactive.va.activerewards.participatingpartners.BaseActiveRewardsParticipatingPartnersActivity;
import com.vitalityactive.va.activerewards.rewards.ActiveRewardsDataSharingConsentActivity;
import com.vitalityactive.va.activerewards.participatingpartners.ParticipatingPartnerDetailActivity;
import com.vitalityactive.va.activerewards.rewards.BaseCurrentRewardsFragment;
import com.vitalityactive.va.activerewards.rewards.BaseRewardVoucherActivity;
import com.vitalityactive.va.activerewards.rewards.CentralAssetWheelSpinConfirmationActivity;
import com.vitalityactive.va.activerewards.rewards.ChooseRewardActivity;
import com.vitalityactive.va.activerewards.rewards.CinemaRewardConfirmationActivity;
import com.vitalityactive.va.activerewards.rewards.CinemaRewardSelectionActivity;
import com.vitalityactive.va.activerewards.rewards.CurrentRewardsFragment;
import com.vitalityactive.va.activerewards.rewards.RewardsHistoryFragment;
import com.vitalityactive.va.activerewards.rewards.StarbucksDataSharingConsentActivity;
import com.vitalityactive.va.activerewards.rewards.StarbucksPartnerRegistrationActivity;
import com.vitalityactive.va.activerewards.rewards.WheelSpinActivity;

import dagger.Subcomponent;

@ActiveRewardsScope
@Subcomponent(modules = {ActiveRewardsModule.class})
public interface ActiveRewardsDependencyInjector {
    void inject(ActiveRewardsLandingActivity activeRewardsLandingActivity);

    void inject(BaseActiveRewardsParticipatingPartnersActivity activity);

    void inject(ActiveRewardsParticipatingPartnersActivity activity);

    void inject(ParticipatingPartnerDetailActivity activity);

    void inject(BaseCurrentRewardsFragment fragment);

    void inject(CurrentRewardsFragment currentRewardsFragment);

    void inject(RewardsHistoryFragment rewardsHistoryFragment);

    void inject(StarbucksPartnerRegistrationActivity starbucksPartnerRegistrationActivity);

    void inject(StarbucksDataSharingConsentActivity starbucksDataSharingConsentActivity);

    void inject(WheelSpinActivity wheelSpinActivity);

    void inject(CinemaRewardSelectionActivity cinemaRewardSelectionActivity);

    void inject(CinemaRewardConfirmationActivity cinemaRewardConfirmationActivity);

    void inject(BaseRewardVoucherActivity rewardVoucherActivity);

    void inject(CentralAssetWheelSpinConfirmationActivity centralAssetWheelSpinConfirmationActivity);

    void inject(ChooseRewardActivity chooseRewardActivity);

    void inject(ActiveRewardsDataSharingConsentActivity activeRewardsDataSharingConsentActivity);

}
