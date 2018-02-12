package com.vitalityactive.va.dependencyinjection.wda;

import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesDetailsActivity;
import com.vitalityactive.va.wellnessdevices.linking.privacypolicy.WellnessDevicesPrivacyPolicyActivity;

import dagger.Subcomponent;

@WDALinkingScope
@Subcomponent(modules = {WDALinkingModule.class})
public interface WDALinkingDependencyInjector {
    void inject(WellnessDevicesDetailsActivity activity);

    void inject(WellnessDevicesPrivacyPolicyActivity activity);
}
