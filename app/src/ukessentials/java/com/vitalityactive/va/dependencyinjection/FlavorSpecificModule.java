package com.vitalityactive.va.dependencyinjection;

import android.content.Context;

import com.google.gson.Gson;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.authentication.BasicAuthorizationProvider;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.login.LoginInteractorImpl;
import com.vitalityactive.va.login.UKEURLSwitcher;
import com.vitalityactive.va.login.callback.LoginCallbackPresenter;
import com.vitalityactive.va.login.callback.UKELoginInteractor;
import com.vitalityactive.va.login.getstarted.UKEGetStartedPresenter;
import com.vitalityactive.va.login.getstarted.UKEWebBreakoutController;
import com.vitalityactive.va.login.service.UKERegisterService;
import com.vitalityactive.va.login.service.UKERegisterServiceClient;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.profile.ChangeEntityNumberClient;
import com.vitalityactive.va.profile.ChangeEntityNumberInteractor;
import com.vitalityactive.va.profile.ChangeEntityNumberPresenter;
import com.vitalityactive.va.userpreferences.learnmore.interactor.ShareVitalityStatusInteractor;
import com.vitalityactive.va.userpreferences.learnmore.interactor.ShareVitalityStatusInteractorImpl;
import com.vitalityactive.va.userpreferences.learnmore.presenter.ShareVitalityStatusLearnMorePresenter;
import com.vitalityactive.va.userpreferences.learnmore.presenter.ShareVitalityStatusLearnMorePresenterImpl;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FlavorSpecificModule {

    @Provides
    @Singleton
    UKEGetStartedPresenter provideUKEGetStartedPresenter(UKEWebBreakoutController webBreakoutController) {
        return new UKEGetStartedPresenter(webBreakoutController);
    }

    @Provides
    @Singleton
    UKEWebBreakoutController provideWebBreakoutController(Context context) {
        return new UKEWebBreakoutController(context);
    }

    @Provides
    @Singleton
    UKERegisterService provideUKERegisterService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(UKERegisterService.class);
    }

    @Provides
    @Singleton
    UKEURLSwitcher provideUKEURLSwitcher(DeviceSpecificPreferences devicePreferences, ServiceGenerator serviceGenerator) {
        return new UKEURLSwitcher(devicePreferences, serviceGenerator);
    }


    @Provides
    @Singleton
    UKELoginInteractor provideUKELoginAndRegistrationInteractor(DeviceSpecificPreferences preferences, UKERegisterService service, Gson gson, BasicAuthorizationProvider authorizationProvider, WebServiceClient webServiceClient, LoginInteractorImpl loginInteractor, EventDispatcher eventDispatcher) {
        UKERegisterServiceClient registerServiceClient = new UKERegisterServiceClient(service, gson, authorizationProvider, webServiceClient);
        return new UKELoginInteractor(preferences, registerServiceClient, loginInteractor, eventDispatcher);
    }

    @Provides
    @Singleton
    LoginCallbackPresenter provideLoginCallbackPresenter(UKELoginInteractor ukeLoginInteractor, EventDispatcher eventDispatcher) {
        return new LoginCallbackPresenter(ukeLoginInteractor, eventDispatcher);
    }

    @Provides
    @Singleton
    ChangeEntityNumberPresenter provideChangeEntityNumberPresenter(EventDispatcher eventDispatcher, ChangeEntityNumberInteractor changeEntityNumberInteractor, PartyInformationRepository partyInformationRepository, DateFormattingUtilities dateFormattingUtil) {
        return new ChangeEntityNumberPresenter(eventDispatcher, changeEntityNumberInteractor, partyInformationRepository, dateFormattingUtil);
    }

    @Provides
    @Singleton
    ChangeEntityNumberInteractor provideChangeEntityNumberInteractor(EventDispatcher eventDispatcher, ChangeEntityNumberClient changeEntityNumberClient, PartyInformationRepository partyInformationRepository, DeviceSpecificPreferences deviceSpecificPreferences) {
        return new ChangeEntityNumberInteractor(eventDispatcher, changeEntityNumberClient, partyInformationRepository, deviceSpecificPreferences);
    }


    @Provides
    @Singleton
    ShareVitalityStatusInteractor provideShareVitalityStatusInteractor(CMSServiceClient serviceClient,
                                                                       EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new ShareVitalityStatusInteractorImpl(serviceClient, eventDispatcher, appConfigRepository.getShareVitalityStatusContentId(),
                appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @Singleton
    ShareVitalityStatusLearnMorePresenter provideShareVitalityStatusLearnMorePresenter(EventDispatcher eventDispatcher,
                                                                                       ShareVitalityStatusInteractor interactor, MainThreadScheduler scheduler) {

        return new ShareVitalityStatusLearnMorePresenterImpl(scheduler, eventDispatcher, interactor);
    }
}
