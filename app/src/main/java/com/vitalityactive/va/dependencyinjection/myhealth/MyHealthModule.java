package com.vitalityactive.va.dependencyinjection.myhealth;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.disclaimer.MyHealthDisclaimerPresenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractorImpl;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MyHealthModule {

    @Provides
    @MyHealthScope
    @Named(DependencyNames.MY_HEALTH)
    static TermsAndConditionsInteractor provideMyHealthDisclaimerInteractor(
            CMSServiceClient serviceClient,
            EventDispatcher eventDispatcher,
            AppConfigRepository appConfigRepository) {

        return new TermsAndConditionsInteractorImpl(
                serviceClient,
                eventDispatcher,
                appConfigRepository.getVHRDisclaimerContentId(),
                appConfigRepository.getLiferayGroupId()
        );
    }

    @Provides
    @MyHealthScope
    @Named(DependencyNames.MY_HEALTH)
    MyHealthDisclaimerPresenter provideMyHealthDisclaimerPresenter(
            MainThreadScheduler scheduler,
            @Named(DependencyNames.MY_HEALTH) TermsAndConditionsInteractor interactor,
            TermsAndConditionsConsenter consenter,
            EventDispatcher eventDispatcher) {

        return new MyHealthDisclaimerPresenter(scheduler, interactor, consenter, eventDispatcher);
    }
}