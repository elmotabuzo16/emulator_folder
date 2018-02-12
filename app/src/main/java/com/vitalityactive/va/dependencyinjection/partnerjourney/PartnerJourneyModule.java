package com.vitalityactive.va.dependencyinjection.partnerjourney;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.partnerjourney.PartnerDetailPresenter;
import com.vitalityactive.va.partnerjourney.PartnerListPresenterImpl;
import com.vitalityactive.va.partnerjourney.PartnerTermsAndConditionsPresenter;
import com.vitalityactive.va.partnerjourney.repository.PartnerRepository;
import com.vitalityactive.va.partnerjourney.service.PartnerRetrievalService;
import com.vitalityactive.va.partnerjourney.service.PartnerServiceClient;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractorImpl;
import com.vitalityactive.va.utilities.CMSImageLoader;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class PartnerJourneyModule {
    @Provides
    static PartnerListPresenterImpl provideHealthServicesPresenterImpl(PartnerServiceClient partnerServiceClient, CMSImageLoader cmsImageLoader, EventDispatcher eventDispatcher) {
        return new PartnerListPresenterImpl(partnerServiceClient, cmsImageLoader, eventDispatcher);
    }

    @Provides
    static PartnerDetailPresenter providePartnerDetailPresenter(PartnerRepository repository,
                                                                PartnerServiceClient partnerServiceClient,
                                                                EventDispatcher eventDispatcher) {
        return new PartnerDetailPresenter(partnerServiceClient, eventDispatcher, repository);
    }

    @Provides
    @PartnerJourneyScope
    static PartnerRetrievalService retrievalService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(PartnerRetrievalService.class);
    }

    @Provides
    @PartnerJourneyScope
    static PartnerRepository providePartnerRepository(@Named(DependencyNames.PARTNER_JOURNEY) DataStore dataStore) {
        return new PartnerRepository(dataStore);
    }

    @Provides
    @PartnerJourneyScope
    @Named(DependencyNames.PARTNER_TERMS_AND_CONDITIONS)
    static TermsAndConditionsInteractor providePartnerTermsAndConditionsInteractor(CMSServiceClient serviceClient,
                                                                                          EventDispatcher eventDispatcher,
                                                                                          AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient, eventDispatcher, appConfigRepository.getActiveRewardsDataPrivacyContentId(), appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @PartnerJourneyScope
    PartnerTermsAndConditionsPresenter providePartnerTermsAndConditionsPresenter(MainThreadScheduler scheduler,
                                                                                        @Named(DependencyNames.PARTNER_TERMS_AND_CONDITIONS) TermsAndConditionsInteractor interactor,
                                                                                        TermsAndConditionsConsenter consenter,
                                                                                        EventDispatcher eventDispatcher) {
        return new PartnerTermsAndConditionsPresenter(scheduler, interactor, consenter, eventDispatcher);
    }

}
