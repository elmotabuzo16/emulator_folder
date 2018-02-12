package com.vitalityactive.va.dependencyinjection;

import com.vitalityactive.va.EventServiceClient;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsEventConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractorImpl;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;
import com.vitalityactive.va.vhc.VHCPrivacyPolicyPresenter;
import com.vitalityactive.va.vhc.VHCSummaryPresenter;
import com.vitalityactive.va.vhc.VHCSummaryPresenterImpl;
import com.vitalityactive.va.vhc.addproof.VHCAddProofInteractor;
import com.vitalityactive.va.vhc.addproof.VHCAddProofInteractorImpl;
import com.vitalityactive.va.vhc.addproof.VHCAddProofPresenter;
import com.vitalityactive.va.vhc.addproof.VHCAddProofPresenterImpl;
import com.vitalityactive.va.vhc.addproof.VHCProofItemRepository;
import com.vitalityactive.va.vhc.addproof.VHCProofItemRepositoryImpl;
import com.vitalityactive.va.vhc.captureresults.CaptureResultsPresenter;
import com.vitalityactive.va.vhc.captureresults.CaptureResultsPresenterImpl;
import com.vitalityactive.va.vhc.captureresults.MeasurementPersister;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;
import com.vitalityactive.va.vhc.privacypolicy.VHCPrivacyPolicyPresenterImpl;
import com.vitalityactive.va.vhc.submission.VHCSubmitter;
import com.vitalityactive.va.vhc.submission.VHCSubmitterImpl;
import com.vitalityactive.va.myhealth.service.VitalityAgeServiceClient;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class VHCCaptureModule {

    @Provides
    @VHCCaptureScope
    VHCProofItemRepository provideVHCProofItemRepository(DataStore dataStore) {
        return new VHCProofItemRepositoryImpl(dataStore);
    }

    @Provides
    @VHCCaptureScope
    VHCAddProofInteractor provideVHCAddProofInteractor(VHCProofItemRepository repository) {
        return new VHCAddProofInteractorImpl(repository);
    }

    @Provides
    @VHCCaptureScope
    VHCAddProofPresenter provideVHCAddProofPresenter(VHCAddProofInteractor interactor) {
        return new VHCAddProofPresenterImpl(interactor);
    }

    @Provides
    @VHCCaptureScope
    @Named(DependencyNames.VHC_PRIVACY_POLICY)
    VHCPrivacyPolicyPresenter provideVHCPrivacyPolicyPresenter(MainThreadScheduler scheduler, @Named(DependencyNames.VHC_PRIVACY_POLICY) TermsAndConditionsInteractor interactor, @Named(DependencyNames.VHC_PRIVACY_POLICY) TermsAndConditionsConsenter consenter, EventDispatcher eventDispatcher, VHCSubmitter vhcSubmitter) {
        return new VHCPrivacyPolicyPresenterImpl(scheduler, interactor, consenter, eventDispatcher, vhcSubmitter);
    }

    @Provides
    @VHCCaptureScope
    @Named(DependencyNames.VHC_PRIVACY_POLICY)
    static TermsAndConditionsInteractor provideVHCPrivacyPolicyInteractor(CMSServiceClient serviceClient, EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient, eventDispatcher, appConfigRepository.getVHCPrivacyPolicyContentId(), appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @VHCCaptureScope
    @Named(DependencyNames.VHC_PRIVACY_POLICY)
    static TermsAndConditionsConsenter provideVHCPrivacyPolicyConsenter(EventDispatcher eventDispatcher, EventServiceClient eventServiceClient) {
        return new TermsAndConditionsEventConsenter(eventDispatcher, eventServiceClient, new long[]{EventType._VHCDATAPRIVACYAGREE}, new long[]{});
    }

    @Provides
    @VHCCaptureScope
    VHCSubmitter provideVHCSubmitter(EventServiceClient serviceClient, HealthAttributeRepository repository, EventDispatcher eventDispatcher, CMSServiceClient cmsServiceClient, VHCProofItemRepository proofItemUriRepository) {
        return new VHCSubmitterImpl(serviceClient, repository, eventDispatcher, cmsServiceClient, proofItemUriRepository);
    }

    @Provides
    @VHCCaptureScope
    VHCSummaryPresenter provideVHCSummaryPresenter(HealthAttributeRepository repository,
                                                   DateFormattingUtilities dateFormattingUtilities,
                                                   VHCProofItemRepository addProofRepository,
                                                   VHCSubmitter vhcSubmitter,
                                                   InsurerConfigurationRepository insurerConfigurationRepository,
                                                   EventDispatcher eventDispatcher,
                                                   MainThreadScheduler scheduler,
                                                   VHCHealthAttributeContent vhcHealthAttributeContent,VitalityAgeServiceClient vitalityAgeServiceClient ) {
        return new VHCSummaryPresenterImpl(repository,
                dateFormattingUtilities,
                addProofRepository,
                vhcSubmitter,
                insurerConfigurationRepository,
                eventDispatcher,
                scheduler,
                vhcHealthAttributeContent,vitalityAgeServiceClient);
    }

    @Provides
    @VHCCaptureScope
    MeasurementPersister providerMeasurementPersister(HealthAttributeRepository healthAttributeRepository, VHCHealthAttributeContent content, InsurerConfigurationRepository insurerConfigurationRepository) {
        return new MeasurementPersister(healthAttributeRepository, content, insurerConfigurationRepository);
    }

    @Provides
    @VHCCaptureScope
    CaptureResultsPresenter provideCaptureResultsPresenter(HealthAttributeRepository repository,
                                                           VHCHealthAttributeContent vhcHealthAttributeContent,
                                                           MeasurementPersister measurementPersister) {
        return new CaptureResultsPresenterImpl(repository, vhcHealthAttributeContent, measurementPersister);
    }
}
