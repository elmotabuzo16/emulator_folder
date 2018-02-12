package com.vitalityactive.va.dependencyinjection.pns;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.R;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.settings.SettingChangePasswordInteractor;
import com.vitalityactive.va.settings.SettingChangePasswordPresenter;
import com.vitalityactive.va.settings.SettingChangePasswordPresenterImpl;
import com.vitalityactive.va.userpreferences.AnalyticsPreferencePresenter;
import com.vitalityactive.va.userpreferences.CrashReportsPreferencePresenter;
import com.vitalityactive.va.userpreferences.EmailPreferencePresenter;
import com.vitalityactive.va.userpreferences.EmailPreferenceServiceClient;
import com.vitalityactive.va.userpreferences.RememberMePreferencePresenter;
import com.vitalityactive.va.userpreferences.UserPreferencePresenter;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PNSCaptureModule {


    @Provides
    @PNSCaptureScope
    EmailPreferencePresenter provideEmailPreferencePresenter(EmailPreferenceServiceClient serviceClient,
                                                             EventDispatcher eventDispatcher,
                                                             MainThreadScheduler scheduler,
                                                             PartyInformationRepository repository) {
        return new EmailPreferencePresenter(R.string.email_field_placeholder_18,
                R.string.Settings_email_communication_985,
                false, R.drawable.icn_email, DefaultUserPreferencePresenter.Type.Email, true, false, serviceClient, eventDispatcher, scheduler, repository);
    }

    @Provides
    @PNSCaptureScope
    RememberMePreferencePresenter provideRememberMePreferencePresenter(PartyInformationRepository partyInformationRepository,
                                                                       DeviceSpecificPreferences deviceSpecificPreferences) {
        return new RememberMePreferencePresenter(R.string.user_prefs_remember_me_toggle_title_81,
                R.string.user_prefs_remember_me_toggle_message_82,
                deviceSpecificPreferences.isRememberMeOn(),
                R.drawable.icn_rememberme,
                UserPreferencePresenter.Type.RememberMe,
                true,
                false,
                partyInformationRepository,
                deviceSpecificPreferences);
    }

    @Provides
    @PNSCaptureScope
    AnalyticsPreferencePresenter provideAnalyticsPreferencePresenter(DeviceSpecificPreferences deviceSpecificPreferences) {
        return new AnalyticsPreferencePresenter(R.string.user_prefs_analytics_toggle_title_73,
                R.string.user_prefs_analytics_toggle_message_74,
                deviceSpecificPreferences.isAnalyticsEnabled(),
                R.drawable.icn_analytics, UserPreferencePresenter.Type.Analytics, true, false,
                deviceSpecificPreferences);
    }

    @Provides
    @PNSCaptureScope
    CrashReportsPreferencePresenter provideCrashReportsPreferencePresenter(DeviceSpecificPreferences deviceSpecificPreferences) {
        return new CrashReportsPreferencePresenter(R.string.user_prefs_crash_reports_toggle_title_75,
                R.string.user_prefs_crash_reports_toggle_message_76,
                deviceSpecificPreferences.isCrashReportsEnabled(),
                R.drawable.icn_crashreports, UserPreferencePresenter.Type.CrashReports, true, false,
                deviceSpecificPreferences);
    }

    @Provides
    @PNSCaptureScope
    SettingChangePasswordPresenter provideSettingChangePasswordPresenter(SettingChangePasswordInteractor passwordInteractor, PartyInformationRepository partyInformationRepository, EventDispatcher eventDispatcher, AccessTokenAuthorizationProvider accessTokenAuthorizationProvider ) {
        return new SettingChangePasswordPresenterImpl(passwordInteractor, partyInformationRepository, eventDispatcher,accessTokenAuthorizationProvider);
    }


}
