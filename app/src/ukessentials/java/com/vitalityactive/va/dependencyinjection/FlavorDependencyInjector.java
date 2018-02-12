package com.vitalityactive.va.dependencyinjection;

import com.vitalityactive.va.BaseUkeActivity;
import com.vitalityactive.va.feedback.BaseFeedbackFragment;
import com.vitalityactive.va.login.callback.LoginCallbackActivity;
import com.vitalityactive.va.login.basic.BasicLoginActivity;
import com.vitalityactive.va.profile.ChangeEntityNumberFragment;
import com.vitalityactive.va.userpreferences.learnmore.ShareVitalityStatusActivity;

import javax.inject.Singleton;

import dagger.Component;

// add uke-only activities and fragments here.
// add main activities and fragments to DependencyInjector

@Singleton
@Component(modules = {NetworkModule.class, PersistenceModule.class, DefaultModule.class, FlavorSpecificModule.class})
public interface FlavorDependencyInjector {
    void inject(BaseUkeActivity baseActivity);

    void inject(LoginCallbackActivity activity);

    void inject(BasicLoginActivity baseActivity);

    void inject(ChangeEntityNumberFragment fragment);

    void inject(ShareVitalityStatusActivity learnMoreActivity);

    void inject(BaseFeedbackFragment fragment);
}
