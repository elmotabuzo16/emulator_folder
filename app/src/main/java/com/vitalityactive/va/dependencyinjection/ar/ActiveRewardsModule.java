package com.vitalityactive.va.dependencyinjection.ar;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.activerewards.EventServiceClientUsingV2ProcessEvents;
import com.vitalityactive.va.activerewards.participatingpartners.presenters.ParticipatingPartnerDetailPresenter;
import com.vitalityactive.va.activerewards.participatingpartners.presenters.ParticipatingPartnerDetailPresenterImpl;
import com.vitalityactive.va.activerewards.rewards.interactor.RewardsInteractor;
import com.vitalityactive.va.activerewards.rewards.presenters.ActiveRewardsDataPrivacyPresenter;
import com.vitalityactive.va.activerewards.rewards.presenters.ActiveRewardsDataPrivacyPresenterImpl;
import com.vitalityactive.va.activerewards.rewards.presenters.CinemaRewardSelectionPresenter;
import com.vitalityactive.va.activerewards.rewards.presenters.CinemaRewardSelectionPresenterImpl;
import com.vitalityactive.va.activerewards.rewards.presenters.StarbucksDataPrivacyPresenter;
import com.vitalityactive.va.activerewards.rewards.presenters.StarbucksDataPrivacyPresenterImpl;
import com.vitalityactive.va.activerewards.rewards.service.RewardsService;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.partnerjourney.repository.PartnerRepository;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsEventConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractorImpl;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActiveRewardsModule {

    @Provides
    @ActiveRewardsScope
    static ActiveRewardsDataPrivacyPresenter provideActiveRewardsDataPrivacyPresenter(MainThreadScheduler scheduler,
                                                                                      @Named(DependencyNames.ACTIVE_REWARDS_REWARD_DATA_PRIVACY) TermsAndConditionsInteractor interactor,
                                                                                      EventDispatcher eventDispatcher,
                                                                                      @Named(DependencyNames.ACTIVE_REWARDS_REWARD_DATA_PRIVACY) TermsAndConditionsConsenter consenter,
                                                                                      RewardsInteractor rewardsInteractor) {
        ActiveRewardsDataPrivacyPresenterImpl presenter = new ActiveRewardsDataPrivacyPresenterImpl(scheduler,
                interactor,
                eventDispatcher,
                consenter,
                rewardsInteractor);
        interactor.setCallback(presenter);
        return presenter;
    }

    @Provides
    @ActiveRewardsScope
    static StarbucksDataPrivacyPresenter provideStarbucksDataPrivacyPresenter(MainThreadScheduler scheduler,
                                                                                  @Named(DependencyNames.ACTIVE_REWARDS_REWARD_DATA_PRIVACY) TermsAndConditionsInteractor interactor,
                                                                                  EventDispatcher eventDispatcher,
                                                                                  @Named(DependencyNames.ACTIVE_REWARDS_REWARD_DATA_PRIVACY) TermsAndConditionsConsenter consenter,
                                                                                  RewardsInteractor rewardsInteractor) {
        StarbucksDataPrivacyPresenterImpl presenter = new StarbucksDataPrivacyPresenterImpl(scheduler,
                interactor,
                eventDispatcher,
                consenter,
                rewardsInteractor);
        interactor.setCallback(presenter);
        return presenter;
    }

    @Provides
    @ActiveRewardsScope
    @Named(DependencyNames.ACTIVE_REWARDS_REWARD_DATA_PRIVACY)
    static TermsAndConditionsInteractor provideActiveRewardsDataPrivacyInteractor(CMSServiceClient serviceClient, EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient, eventDispatcher, appConfigRepository.getStarbucksPrivacyPolicyContentId(), appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @ActiveRewardsScope
    @Named(DependencyNames.ACTIVE_REWARDS_REWARD_DATA_PRIVACY)
    static TermsAndConditionsConsenter provideActiveRewardsDataPrivacyConsenter(EventDispatcher eventDispatcher, EventServiceClientUsingV2ProcessEvents eventServiceClient) {
        return new TermsAndConditionsEventConsenter(eventDispatcher, eventServiceClient, new long[]{EventType._REWARDDSAGREE}, new long[]{EventType._REWARDDSDISAGREE});
    }

    @Provides
    @ActiveRewardsScope
    RewardsService rewardsService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(RewardsService.class);
    }

    @Provides
    @ActiveRewardsScope
    CinemaRewardSelectionPresenter provideCinemaRewardSelectionPresenter(RewardsInteractor interactor,
                                                                         EventDispatcher eventDispatcher,
                                                                         MainThreadScheduler scheduler) {
        return new CinemaRewardSelectionPresenterImpl(interactor, eventDispatcher, scheduler);
    }

    @Provides
    @ActiveRewardsScope
    static PartnerRepository providePartnerRepository(@Named(DependencyNames.PARTNER_JOURNEY) DataStore dataStore) {
        return new PartnerRepository(dataStore);
    }

    @Provides
    @ActiveRewardsScope
    ParticipatingPartnerDetailPresenter provideParticipatingPartnerDetailPresenter(PartnerRepository partnerRepository) {
        return new ParticipatingPartnerDetailPresenterImpl(partnerRepository);
    }

}
