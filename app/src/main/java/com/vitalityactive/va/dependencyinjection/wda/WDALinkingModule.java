package com.vitalityactive.va.dependencyinjection.wda;

import com.vitalityactive.va.EventServiceClient;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.connectivity.ConnectivityListener;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsEventConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractorImpl;
import com.vitalityactive.va.wellnessdevices.landing.service.WellnessDevicesServiceClient;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingInteractor;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingInteractorImpl;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingPresenter;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingPresenterImpl;
import com.vitalityactive.va.wellnessdevices.linking.cascaderequests.DelinkCascade;
import com.vitalityactive.va.wellnessdevices.linking.privacypolicy.PrivacyPolicyPresenter;
import com.vitalityactive.va.wellnessdevices.linking.privacypolicy.PrivacyPolicyPresenterImpl;
import com.vitalityactive.va.wellnessdevices.linking.refreshtoken.WellnessDevicesRefreshTokenInteractor;
import com.vitalityactive.va.wellnessdevices.linking.refreshtoken.WellnessDevicesRefreshTokenInteractorImpl;
import com.vitalityactive.va.wellnessdevices.linking.repository.LinkingPageRepository;
import com.vitalityactive.va.wellnessdevices.linking.service.DelinkDeviceService;
import com.vitalityactive.va.wellnessdevices.linking.service.LinkDeviceServiceClient;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class WDALinkingModule {
    @Provides
    @WDALinkingScope
    WellnessDevicesLinkingInteractor provideWellnessDevicesLinkingInteractor(EventDispatcher eventDispatcher,
                                                                             ConnectivityListener connectivityListener,
                                                                             LinkDeviceServiceClient linkRequestService,
                                                                             DelinkDeviceService delinkDeviceService,
                                                                             LinkingPageRepository linkingPageRepository) {
        return new WellnessDevicesLinkingInteractorImpl(eventDispatcher, connectivityListener,
                linkRequestService, delinkDeviceService, linkingPageRepository);
    }

    @Provides
    @WDALinkingScope
    WellnessDevicesRefreshTokenInteractor provideWellnessDevicesRefreshTokenInteractor(EventDispatcher eventDispatcher,
                                                                                       ConnectivityListener connectivityListener,
                                                                                       WellnessDevicesServiceClient wellnessDevicesServiceClient) {
        return new WellnessDevicesRefreshTokenInteractorImpl(eventDispatcher, connectivityListener,
                wellnessDevicesServiceClient);
    }

    @Provides
    @WDALinkingScope
    DelinkCascade provideDelinkCascade(WellnessDevicesLinkingInteractorImpl linkingInteractor,
                                       WellnessDevicesRefreshTokenInteractor tokenInteractor,
                                       EventDispatcher eventDispatcher,
                                       ConnectivityListener connectivityListener) {
        return new DelinkCascade(linkingInteractor, tokenInteractor, eventDispatcher, connectivityListener);
    }

    @Provides
    WellnessDevicesLinkingPresenter provideWellnessDevicesLinkingPresenter(WellnessDevicesLinkingInteractorImpl interactor,
                                                                           WellnessDevicesRefreshTokenInteractor tokenInteractor,
                                                                           DelinkCascade delinkCascade,
                                                                           EventDispatcher eventDispatcher,
                                                                           MainThreadScheduler scheduler,
                                                                           InsurerConfigurationRepository insurerConfigurationRepository) {
        return new WellnessDevicesLinkingPresenterImpl(interactor, tokenInteractor, delinkCascade,
                eventDispatcher, scheduler, insurerConfigurationRepository);
    }

    @Provides
    @WDALinkingScope
    @Named(DependencyNames.WD_PRIVACY_POLICY)
    static TermsAndConditionsConsenter provideWellnessDevicesPrivacyPolicyConsenter(EventDispatcher eventDispatcher,
                                                                                    EventServiceClient eventServiceClient) {
        return new TermsAndConditionsEventConsenter(eventDispatcher, eventServiceClient, new long[]{EventType._DPPOLICYAGREE}, new long[]{});
    }

    @Provides
    @WDALinkingScope
    @Named(DependencyNames.WD_PRIVACY_POLICY)
    static TermsAndConditionsInteractor provideWellnessDevicesPrivacyPolicyInteractor(CMSServiceClient serviceClient,
                                                                                      EventDispatcher eventDispatcher,
                                                                                      AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient,
                eventDispatcher,
                appConfigRepository.getWellnessDevicesPrivacyPolicyContentId(),
                appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @WDALinkingScope
    @Named(DependencyNames.WD_PRIVACY_POLICY)
    static PrivacyPolicyPresenter provideWellnessDevicesPrivacyPolicyPresenter(MainThreadScheduler scheduler,
                                                                               @Named(DependencyNames.WD_PRIVACY_POLICY) TermsAndConditionsInteractor interactor,
                                                                               WellnessDevicesLinkingInteractor wellnessDevicesLinkingInteractor,
                                                                               @Named(DependencyNames.WD_PRIVACY_POLICY) TermsAndConditionsConsenter consenter,
                                                                               EventDispatcher eventDispatcher) {
        return new PrivacyPolicyPresenterImpl(scheduler, interactor, wellnessDevicesLinkingInteractor, consenter, eventDispatcher);
    }
}
